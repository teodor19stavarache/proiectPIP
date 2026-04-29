import serpapi  # type: ignore # SerpApi client library for querying Google Hotels
import csv
import json
import os
import re

INPUT_FILE    = r"C:\Users\blue\Desktop\ProiectPIP\hotelAPI\input\input.txt"
output_folder = "output"
os.makedirs(output_folder, exist_ok=True)

def clean_price(price):
    #Remove unusual characters and ensure a space between number and currency.
    if price == "N/A":
        return price
    cleaned = re.sub(r"[^0-9\s,]", "", price)  # keep ONLY numbers, strip all letters and symbols
    cleaned = " ".join(cleaned.split())        # normalize whitespace
    return f"{cleaned} {currency}"

# --- Search Parameters ---
def read_input(filepath=INPUT_FILE):
    """Read search parameters from a txt file written by the Java GUI."""
    params = {}
    with open(filepath, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if "=" in line:  # skip empty lines or malformed ones
                key, value = line.split("=", 1)  # split only on first "="
                params[key.strip()] = value.strip()
    return params

# --- Read parameters from input.txt instead of hardcoding them ---
params = read_input()

city      = params.get("city", "Bucuresti")
check_in  = params.get("check_in", "2026-08-25")
check_out = params.get("check_out", "2026-08-29")
adults    = params.get("adults", "2")
children  = params.get("children", "0")
currency  = params.get("currency", "EUR")  # Default to RON if not specified 

# --- Initialize SerpApi client with API key ---
client = serpapi.Client(api_key="039a4b7a86b50c45f3479359506f36aa991680a4f1e2bdc9c32f8b39d257bdcc")

# --- Send search request to Google Hotels engine ---
results = client.search({
    "engine": "google_hotels",
    "q": city,
    "check_in_date": check_in,
    "check_out_date": check_out,
    "adults": adults,
    "children": children,
    "children_ages": params.get("children_ages", "0"),
    "currency": currency,
    "gl": "ro",
    "hl": "en"
})

# --- Convert the SerpApi result object to a standard Python dictionary ---
# This is necessary because the raw result object doesn't support direct JSON serialization
data = results.as_dict()

# --- Extract the list of hotel properties from the result ---
# Using .get() with a fallback of [] to avoid crashes if "properties" key is missing
properties = data.get("properties", [])[:30]  # ← only keep first 30 results

# --- Save the full raw API response to a JSON file for reference or debugging ---
with open(os.path.join(output_folder, "hotels_raw.json"), "w", encoding="utf-8") as f:
    json.dump(data, f, indent=2, ensure_ascii=False)

# --- Build a filtered list and write CSV at the same time ---
filtered_hotels = []  # Will hold only the fields we care about

with open(os.path.join(output_folder, "data.csv"), mode="w", newline="", encoding="utf-8") as file:
    writer = csv.writer(file)

    # Write the header row
    writer.writerow(["NAME", "RATE_PER_NIGHT", "TOTAL_RATE", "LINK", "RATING", "HOTEL_CLASS"])

    # Iterate over each hotel property in the results
    for ptop in properties:

        # Safely extract the nightly rate (nested under "rate_per_night" -> "lowest")
        rate_per_night = ptop.get("rate_per_night", {}).get("lowest", "N/A")
        rate_per_night = clean_price(rate_per_night)

        # Safely extract the total rate for the full stay (nested under "total_rate" -> "lowest")
        total_rate = ptop.get("total_rate", {}).get("lowest", "N/A")
        total_rate = clean_price(total_rate)

        # Safely extract the booking link
        # Not all hotels have a direct link, so .get() prevents a KeyError crash
        link = ptop.get("link", "N/A")

        rating = ptop.get("overall_rating", "N/A")  # Extract rating if available, otherwise "N/A"
        
        hotel_class = ptop.get("hotel_class", "N/A")  # Extract hotel class if available, otherwise "N/A"

        # Build a clean dictionary with only the selected fields
        hotel_entry = {
            "name": ptop.get("name", "N/A"),
            "rate_per_night": rate_per_night,
            "total_rate": total_rate,
            "link": link,
            "rating": rating,
            "hotel_class": hotel_class,
            "nearby_places": ptop.get("nearby_places", [])
        }

        # Add to the filtered list for JSON export
        if hotel_entry["name"] != "N/A" and link != "N/A" and hotel_class != "N/A":  # Only include entries that have a name, a link, and a hotel class
            filtered_hotels.append(hotel_entry)

        # Write one row per hotel to the CSV
        if hotel_entry["name"] != "N/A" and link != "N/A" and hotel_class != "N/A":  # Only include entries that have a name, a link, and a hotel class
            writer.writerow([hotel_entry["name"], rate_per_night, total_rate, link, rating, hotel_class])

# --- Save only the selected fields to a clean JSON file ---
with open(os.path.join(output_folder, "hotels_filtered.json"), "w", encoding="utf-8") as f:
    json.dump(filtered_hotels, f, indent=2, ensure_ascii=False)

print("Done! Files created:")
print("  - hotels_raw.json      (full API response)")
print("  - hotels_filtered.json (selected fields only)")
print("  - data.csv             (selected fields only)")