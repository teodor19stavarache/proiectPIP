import serpapi  # type: ignore # SerpApi client library for querying Google Hotels
import csv
import json
import os
import re
import sys

# --- File paths ---
INPUT_FILE    = r"C:\Users\blue\Desktop\ProiectPIP\hotelAPI\input\input.txt"
output_folder = "output"
os.makedirs(output_folder, exist_ok=True)


def clean_price(price):
    """
    Cleans a raw price string returned by the API by removing currency symbols
    and special characters, keeping only digits and formatting, then appending
    the global currency code at the end.

    Parameters:
        price (str): Raw price string from the API (e.g. "€167" or "N/A").

    Returns:
        str: Cleaned price string with currency appended (e.g. "167 EUR"),
             or "N/A" if no valid price was provided.
    """
    if price == "N/A":
        return price
    cleaned = re.sub(r"[^0-9\s,]", "", price)  # keep ONLY numbers, strip all letters and symbols
    cleaned = " ".join(cleaned.split())         # normalize whitespace
    return f"{cleaned} {currency}"


def write_input(city, check_in, check_out, adults, children, currency, children_ages=None, filepath=INPUT_FILE):
    """
    Validates and writes the customer's search parameters to the input.txt file
    so that the main script can read them and send the correct API request.

    Parameters:
        city          (str): The destination city to search hotels in.
        check_in      (str): Check-in date in YYYY-MM-DD format.
        check_out     (str): Check-out date in YYYY-MM-DD format.
        adults        (int): Number of adult guests.
        children      (int): Number of child guests.
        currency      (str): Currency code for prices (e.g. "EUR", "RON").
        children_ages (str, optional): Comma-separated ages of children (e.g. "5,8,12").
                                       Required if children > 0.
        filepath      (str): Path to the input file. Defaults to INPUT_FILE.

    Returns:
        None

    Raises:
        ValueError: If children > 0 but no children_ages provided.
        ValueError: If the number of ages doesn't match the number of children.
        ValueError: If any child age is outside the valid range of 0-17.
        OSError:    If the file cannot be written due to permission or path issues.
    """
    if int(children) > 0 and children_ages is None:
        raise ValueError("children_ages must be provided when children > 0")

    if int(children) > 0 and children_ages:
        ages_list = str(children_ages).split(",")
        if len(ages_list) != int(children):
            raise ValueError(f"Number of ages ({len(ages_list)}) must match number of children ({children})")
        for age in ages_list:
            if not 0 <= int(age.strip()) <= 17:
                raise ValueError(f"Each child age must be between 0 and 17, got: {age.strip()}")

    try:
        os.makedirs(os.path.dirname(filepath), exist_ok=True)
        with open(filepath, "w", encoding="utf-8") as f:
            f.write(f"city={city}\n")
            f.write(f"check_in={check_in}\n")
            f.write(f"check_out={check_out}\n")
            f.write(f"adults={adults}\n")
            f.write(f"children={children}\n")
            if int(children) > 0 and children_ages:
                f.write(f"children_ages={children_ages}\n")
            f.write(f"currency={currency}\n")
    except OSError as e:
        print(f"[ERROR] Could not write input file: {e}")
        sys.exit(1)


def read_input(filepath=INPUT_FILE):
    """
    Reads the search parameters from the input.txt file written by the Java GUI
    or the write_input() function, and returns them as a dictionary.

    Parameters:
        filepath (str): Path to the input file. Defaults to INPUT_FILE.

    Returns:
        dict: A dictionary of key-value pairs representing the search parameters.
              Example: {"city": "Paris", "check_in": "2026-07-15", ...}

    Raises:
        SystemExit: If the file is not found or cannot be read.
    """
    params = {}
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if "=" in line:  # skip empty lines or malformed ones
                    key, value = line.split("=", 1)  # split only on first "="
                    params[key.strip()] = value.strip()
    except FileNotFoundError:
        print(f"[ERROR] Input file not found: {filepath}")
        sys.exit(1)
    except OSError as e:
        print(f"[ERROR] Could not read input file: {e}")
        sys.exit(1)
    return params


def search_hotels(client, search_params):
    """
    Sends a search request to the SerpApi Google Hotels engine using the
    provided client and parameters.

    Parameters:
        client        (serpapi.Client): An initialized SerpApi client instance.
        search_params (dict): Dictionary of search parameters to send to the API.
                              Must include at minimum: engine, q, check_in_date,
                              check_out_date, adults, children, currency, gl, hl.

    Returns:
        dict: The full API response as a Python dictionary.

    Raises:
        SystemExit: If the API request fails due to a bad request, invalid API key,
                    network issues, or any other exception.
    """
    try:
        results = client.search(search_params)
        return results.as_dict()
    except Exception as e:
        print(f"[ERROR] API request failed: {e}")
        print("[HINT] Check your API key, date format, and search parameters.")
        sys.exit(1)


def save_json(data, filepath):
    """
    Saves a Python dictionary or list to a JSON file with proper formatting
    and UTF-8 encoding to support international characters.

    Parameters:
        data     (dict | list): The data to serialize and save.
        filepath (str): The full path of the output JSON file.

    Returns:
        None

    Raises:
        SystemExit: If the file cannot be written due to permission or path issues.
    """
    try:
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2, ensure_ascii=False)
    except OSError as e:
        print(f"[ERROR] Could not write JSON file '{filepath}': {e}")
        sys.exit(1)


def extract_hotels(properties):
    """
    Iterates over the raw list of hotel properties returned by the API and
    extracts only the fields needed for the application, including cleaning
    prices and extracting the first available image thumbnail.

    Parameters:
        properties (list): List of hotel property dictionaries from the API response.

    Returns:
        list: A list of filtered hotel dictionaries, each containing:
              - name          (str): Hotel name.
              - rate_per_night(str): Cleaned nightly rate with currency.
              - total_rate    (str): Cleaned total rate with currency.
              - link          (str): URL to the hotel's booking page.
              - rating        (float | str): Overall guest rating or "N/A".
              - hotel_class   (str): Star classification (e.g. "3-star hotel") or "N/A".
              - nearby_places (list): List of nearby places with transportation info.
              - image         (str): URL of the first thumbnail image or "N/A".
    """
    filtered = []
    for ptop in properties:
        try:
            rate_per_night = ptop.get("rate_per_night", {}).get("lowest", "N/A")
            rate_per_night = clean_price(rate_per_night)

            total_rate = ptop.get("total_rate", {}).get("lowest", "N/A")
            total_rate = clean_price(total_rate)

            link        = ptop.get("link", "N/A")
            rating      = ptop.get("overall_rating", "N/A")
            hotel_class = ptop.get("hotel_class", "N/A")

            images      = ptop.get("images", [])
            first_image = images[0].get("thumbnail", "N/A") if images else "N/A"

            hotel_entry = {
                "name":           ptop.get("name", "N/A"),
                "rate_per_night": rate_per_night,
                "total_rate":     total_rate,
                "link":           link,
                "rating":         rating,
                "hotel_class":    hotel_class,
                "nearby_places":  ptop.get("nearby_places", []),
                "image":          first_image
            }

            # Only include entries that have a name, a link, and a hotel class
            if hotel_entry["name"] != "N/A" and link != "N/A" and hotel_class != "N/A":
                filtered.append(hotel_entry)

        except Exception as e:
            print(f"[WARNING] Skipping hotel due to extraction error: {e}")
            continue

    return filtered


def save_csv(hotels, filepath):
    """
    Writes the filtered list of hotels to a CSV file with a header row,
    one hotel per row, including all extracted fields.

    Parameters:
        hotels   (list): List of filtered hotel dictionaries from extract_hotels().
        filepath (str): The full path of the output CSV file.

    Returns:
        None

    Raises:
        SystemExit: If the file cannot be written due to permission or path issues.
    """
    try:
        with open(filepath, mode="w", newline="", encoding="utf-8") as file:
            writer = csv.writer(file)
            writer.writerow(["NAME", "RATE_PER_NIGHT", "TOTAL_RATE", "LINK", "RATING", "HOTEL_CLASS", "IMAGE"])
            for h in hotels:
                writer.writerow([
                    h["name"],
                    h["rate_per_night"],
                    h["total_rate"],
                    h["link"],
                    h["rating"],
                    h["hotel_class"],
                    h["image"]
                ])
    except OSError as e:
        print(f"[ERROR] Could not write CSV file '{filepath}': {e}")
        sys.exit(1)


# ---------------------------------------------------------------------------
# MAIN
# ---------------------------------------------------------------------------

# --- Read parameters from input.txt ---
params = read_input()

city          = params.get("city", "Bucuresti")
check_in      = params.get("check_in", "2026-08-25")
check_out     = params.get("check_out", "2026-08-29")
adults        = params.get("adults", "2")
children      = params.get("children", "0")
children_ages = params.get("children_ages", "")
currency      = params.get("currency", "EUR")

# --- Initialize SerpApi client ---
client = serpapi.Client(api_key="039a4b7a86b50c45f3479359506f36aa991680a4f1e2bdc9c32f8b39d257bdcc")

# --- Build search parameters ---
search_params = {
    "engine":         "google_hotels",
    "q":              city,
    "check_in_date":  check_in,
    "check_out_date": check_out,
    "adults":         adults,
    "children":       children,
    "currency":       currency,
    "gl":             "ro",
    "hl":             "en"
}

# Only add children_ages if there are actual children
if int(children) > 0 and children_ages:
    search_params["children_ages"] = children_ages

# --- Send API request ---
print(f"[INFO] Searching hotels in '{city}' from {check_in} to {check_out}...")
data = search_hotels(client, search_params)

# --- Extract properties ---
properties     = data.get("properties", [])[:50]
filtered_hotels = extract_hotels(properties)

if not filtered_hotels:
    print("[WARNING] No hotels found for the given search parameters.")
    sys.exit(0)

print(f"[INFO] Found {len(filtered_hotels)} hotels.")

# --- Save outputs ---
save_json(data,             os.path.join(output_folder, "hotels_raw.json"))
save_json(filtered_hotels,  os.path.join(output_folder, "hotels_filtered.json"))
save_csv(filtered_hotels,   os.path.join(output_folder, "data.csv"))

print("Done! Files created:")
print("  - hotels_raw.json      (full API response)")
print("  - hotels_filtered.json (selected fields only)")
print("  - data.csv             (selected fields only)")
