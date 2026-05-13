import requests
import urllib.parse
import json
import os
import sys
import sqlite3
import time

sys.stdout.reconfigure(encoding="utf-8")

# --- File paths ---
INPUT_FILE    = r"input\overpass_input.txt"
OUTPUT_FOLDER = "output"
DB_FILE       = os.path.join(OUTPUT_FOLDER, "atractii.db")
os.makedirs(OUTPUT_FOLDER, exist_ok=True)

OVERPASS_URL    = "https://overpass-api.de/api/interpreter"
NOMINATIM_URL   = "https://nominatim.openstreetmap.org/search"
WIKIPEDIA_RO    = "https://ro.wikipedia.org/w/api.php"
WIKIPEDIA_EN    = "https://en.wikipedia.org/w/api.php"

MAX_RESULTS     = 10   # numărul maxim de atracții / restaurante returnate

# ---------------------------------------------------------------------------
# TAG DICTIONARY
# Maps each user preference tag to Overpass OSM key=value filters.
# Add new tags here to extend the system without touching any other code.
# ---------------------------------------------------------------------------
TAG_DICTIONARY = {
    "istorie": [
        '["historic"]',
        '["tourism"="museum"]',
        '["tourism"="monument"]',
        '["tourism"="archaeological_site"]',
        '["historic"="castle"]',
        '["historic"="ruins"]',
        '["historic"="memorial"]',
        '["historic"="fort"]',
    ],
    "natura": [
        '["leisure"="park"]',
        '["leisure"="nature_reserve"]',
        '["leisure"="garden"]',
        '["tourism"="viewpoint"]',
        '["natural"="peak"]',
        '["natural"="waterfall"]',
        '["natural"="cave_entrance"]',
    ],
    "aventura": [
        '["tourism"="theme_park"]',
        '["leisure"="water_park"]',
        '["leisure"="sports_centre"]',
        '["sport"="climbing"]',
        '["leisure"="miniature_golf"]',
        '["leisure"="escape_game"]',
        '["leisure"="trampoline_park"]',
    ],
    "cultura": [
        '["tourism"="gallery"]',
        '["amenity"="theatre"]',
        '["amenity"="cinema"]',
        '["amenity"="arts_centre"]',
        '["amenity"="library"]',
        '["tourism"="museum"]',
        '["amenity"="community_centre"]',
    ],
    "religie": [
        '["amenity"="place_of_worship"]',
        '["historic"="wayside_shrine"]',
        '["historic"="monastery"]',
        '["historic"="chapel"]',
    ],
    "arhitectura": [
        '["tourism"="monument"]',
        '["tourism"="artwork"]',
        '["man_made"="tower"]',
        '["historic"="building"]',
        '["historic"="castle"]',
        '["tourism"="attraction"]',
    ],
    "distractie": [
        '["leisure"="amusement_arcade"]',
        '["tourism"="theme_park"]',
        '["leisure"="water_park"]',
        '["amenity"="nightclub"]',
        '["leisure"="bowling_alley"]',
        '["leisure"="escape_game"]',
    ],
    "sport": [
        '["leisure"="sports_centre"]',
        '["leisure"="stadium"]',
        '["leisure"="swimming_pool"]',
        '["sport"="climbing"]',
        '["sport"="cycling"]',
        '["sport"="tennis"]',
    ],
    "cumparaturi": [
        '["shop"="mall"]',
        '["shop"="department_store"]',
        '["amenity"="marketplace"]',
        '["shop"="supermarket"]',
    ],
    "relaxare": [
        '["leisure"="spa"]',
        '["amenity"="spa"]',
        '["leisure"="sauna"]',
        '["amenity"="massage"]',
        '["leisure"="beach"]',
        '["natural"="beach"]',
    ],
}

# Restaurants are always queried separately, independent of user tags
RESTAURANT_FILTERS = [
    '["amenity"="restaurant"]',
    '["amenity"="cafe"]',
    '["amenity"="fast_food"]',
    '["amenity"="food_court"]',
]


# ---------------------------------------------------------------------------
# INPUT
# ---------------------------------------------------------------------------

def write_input(city, tags, filepath=INPUT_FILE):
    """
    Writes search parameters to the input file so the Java GUI can call
    this script without modifying it directly.

    Parameters:
        city     (str): Destination city name (e.g. "Cluj-Napoca").
        tags     (list[str]): List of user preference tags from TAG_DICTIONARY.
        filepath (str): Path to input file. Defaults to INPUT_FILE.

    Raises:
        ValueError: If tags list is empty or contains unrecognized values.
        OSError:    If the file cannot be written.
    """
    if not tags:
        raise ValueError("At least one tag must be provided.")

    invalid = [t for t in tags if t not in TAG_DICTIONARY]
    if invalid:
        raise ValueError(f"Unrecognized tags: {invalid}. Valid tags: {list(TAG_DICTIONARY.keys())}")

    try:
        os.makedirs(os.path.dirname(os.path.abspath(filepath)), exist_ok=True)
        with open(filepath, "w", encoding="utf-8") as f:
            f.write(f"city={city}\n")
            f.write(f"tags={','.join(tags)}\n")
    except OSError as e:
        print(f"[ERROR] Could not write input file: {e}")
        sys.exit(1)


def read_input(filepath=INPUT_FILE):
    """
    Reads city and tags from the input file.

    Returns:
        dict: Keys 'city' (str) and 'tags' (list[str]).

    Raises:
        SystemExit: If the file is not found or is missing required keys.
    """
    params = {}
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            for line in f:
                line = line.strip()
                if "=" in line:
                    key, value = line.split("=", 1)
                    params[key.strip()] = value.strip()
    except FileNotFoundError:
        print(f"[ERROR] Input file not found: {filepath}")
        sys.exit(1)
    except OSError as e:
        print(f"[ERROR] Could not read input file: {e}")
        sys.exit(1)

    if "city" not in params:
        print("[ERROR] Input file missing required field: city")
        sys.exit(1)

    tags_raw = params.get("tags", "")
    tags = [t.strip() for t in tags_raw.split(",") if t.strip()]
    return {"city": params["city"], "tags": tags}


# ---------------------------------------------------------------------------
# GEOCODING
# ---------------------------------------------------------------------------

def get_bounding_box(city):
    """
    Uses Nominatim (OpenStreetMap) to resolve a city name into a bounding box
    used by Overpass API to scope the search geographically.

    Parameters:
        city (str): City name to geocode.

    Returns:
        tuple: (south, west, north, east) as floats, or None on failure.
    """
    try:
        response = requests.get(
            NOMINATIM_URL,
            params={"q": city, "format": "json", "limit": 1},
            headers={"User-Agent": "TripPlannerPIP/1.0"},
            timeout=10
        )
        response.raise_for_status()
        results = response.json()
        if not results:
            print(f"[ERROR] City '{city}' not found via Nominatim.")
            return None
        bb = results[0].get("boundingbox")
        south, north, west, east = float(bb[0]), float(bb[1]), float(bb[2]), float(bb[3])
        print(f"[INFO] Bounding box for '{city}': S={south}, N={north}, W={west}, E={east}")
        return south, west, north, east
    except requests.exceptions.RequestException as e:
        print(f"[ERROR] Nominatim geocoding failed: {e}")
        return None


# ---------------------------------------------------------------------------
# OVERPASS QUERY BUILDER
# ---------------------------------------------------------------------------

def build_overpass_query(bbox, filters, max_results=50):
    """
    Builds an Overpass QL query for given bounding box and OSM filters.

    Parameters:
        bbox       (tuple): (south, west, north, east) floats.
        filters    (list[str]): List of Overpass filter strings e.g. '["tourism"="museum"]'.
        max_results(int): Maximum number of results to return.

    Returns:
        str: Complete Overpass QL query string.
    """
    south, west, north, east = bbox
    bbox_str = f"{south},{west},{north},{east}"

    node_ways = ""
    for f in filters:
        node_ways += f'  node{f}({bbox_str});\n'
        node_ways += f'  way{f}({bbox_str});\n'

    query = f"""[out:json][timeout:30];
(
{node_ways}
);
out body center {max_results};
"""
    return query


def build_filters_for_tags(selected_tags):
    """
    Collects unique Overpass filters for the given user preference tags.

    Parameters:
        selected_tags (list[str]): Tags selected by the user.

    Returns:
        list[str]: Deduplicated list of Overpass filter strings.
    """
    filters = []
    seen = set()
    for tag in selected_tags:
        tag = tag.strip().lower()
        if tag not in TAG_DICTIONARY:
            print(f"[WARNING] Unknown tag '{tag}' — skipping.")
            continue
        for f in TAG_DICTIONARY[tag]:
            if f not in seen:
                seen.add(f)
                filters.append(f)
    return filters


# ---------------------------------------------------------------------------
# OVERPASS API CALL
# ---------------------------------------------------------------------------

def query_overpass(query):
    """
    Sends a GET request to the Overpass API and returns parsed JSON.
    Uses GET with URL-encoded query because some server configurations
    reject POST requests with 406.

    Parameters:
        query (str): Overpass QL query string.

    Returns:
        dict: Parsed API response.

    Raises:
        SystemExit: On timeout or request failure.
    """
    try:
        encoded = urllib.parse.quote(query)
        url = f"{OVERPASS_URL}?data={encoded}"
        response = requests.get(
            url,
            headers={"User-Agent": "TripPlannerPIP/1.0 (university project)"},
            timeout=30
        )
        response.raise_for_status()
        response.encoding = "utf-8"
        return response.json()
    except requests.exceptions.Timeout:
        print("[ERROR] Overpass API request timed out.")
        sys.exit(1)
    except requests.exceptions.RequestException as e:
        print(f"[ERROR] Overpass API request failed: {e}")
        sys.exit(1)


# ---------------------------------------------------------------------------
# DATA EXTRACTION
# ---------------------------------------------------------------------------

def extract_places(elements, category_label="attraction"):
    """
    Extracts relevant fields from raw Overpass API elements.
    Skips elements without a name tag.

    Parameters:
        elements       (list): Raw 'elements' list from Overpass response.
        category_label (str): Label to assign when OSM tags don't specify one.

    Returns:
        list[dict]: Cleaned list of place dictionaries.
    """
    places = []
    seen_names = set()

    for el in elements:
        tags = el.get("tags", {})

        name = (
            tags.get("name")
            or tags.get("name:ro")
            or tags.get("name:en")
        )
        if not name or name in seen_names:
            continue
        seen_names.add(name)

        if len(places) >= MAX_RESULTS:
            break

        lat = el.get("lat") or el.get("center", {}).get("lat")
        lon = el.get("lon") or el.get("center", {}).get("lon")

        category = (
            tags.get("tourism")
            or tags.get("historic")
            or tags.get("leisure")
            or tags.get("amenity")
            or tags.get("sport")
            or tags.get("shop")
            or category_label
        )

        place = {
            "name":          name,
            "category":      category,
            "lat":           lat,
            "lon":           lon,
            "opening_hours": tags.get("opening_hours", "N/A"),
            "website":       tags.get("website") or tags.get("contact:website", "N/A"),
            "description":   tags.get("description") or tags.get("description:ro", "N/A"),
            "phone":         tags.get("phone") or tags.get("contact:phone", "N/A"),
            "wheelchair":    tags.get("wheelchair", "N/A"),
            "fee":           tags.get("fee", "N/A"),
            "addr_street":   tags.get("addr:street", "N/A"),
            "addr_housenr":  tags.get("addr:housenumber", "N/A"),
            "imagine":       "N/A",
        }
        places.append(place)

    return places


# ---------------------------------------------------------------------------
# WIKIPEDIA IMAGE LOOKUP
# ---------------------------------------------------------------------------

def _fetch_image_by_title(api_url, title, thumb_size):
    """Fetches a Wikipedia thumbnail for an exact article title."""
    response = requests.get(
        api_url,
        params={
            "action":      "query",
            "titles":      title,
            "prop":        "pageimages",
            "format":      "json",
            "pithumbsize": thumb_size,
            "redirects":   1,
        },
        headers={"User-Agent": "TripPlannerPIP/1.0 (university project)"},
        timeout=5
    )
    response.raise_for_status()
    pages = response.json().get("query", {}).get("pages", {})
    for page in pages.values():
        thumbnail = page.get("thumbnail", {}).get("source")
        if thumbnail:
            return thumbnail
    return None


def _search_wikipedia_title(api_url, name):
    """Uses Wikipedia full-text search to find the closest article title."""
    response = requests.get(
        api_url,
        params={
            "action":   "query",
            "list":     "search",
            "srsearch": name,
            "format":   "json",
            "srlimit":  1,
        },
        headers={"User-Agent": "TripPlannerPIP/1.0 (university project)"},
        timeout=5
    )
    response.raise_for_status()
    results = response.json().get("query", {}).get("search", [])
    if results:
        return results[0]["title"]
    return None


def get_wikipedia_image(name, thumb_size=400):
    """
    Searches for a thumbnail image on Romanian Wikipedia first, then English.
    Strategy: try exact title match first; if no image found, fall back to
    full-text search and use the first result's article image.

    Parameters:
        name       (str): Name of the attraction to search.
        thumb_size (int): Desired thumbnail width in pixels.

    Returns:
        str: Thumbnail URL or "N/A".
    """
    for api_url in [WIKIPEDIA_RO, WIKIPEDIA_EN]:
        try:
            # Pass 1: exact title
            img = _fetch_image_by_title(api_url, name, thumb_size)
            if img:
                return img

            # Pass 2: full-text search fallback
            best_title = _search_wikipedia_title(api_url, name)
            if best_title:
                img = _fetch_image_by_title(api_url, best_title, thumb_size)
                if img:
                    return img

        except requests.exceptions.RequestException:
            continue

    return "N/A"


def enrich_with_images(places):
    """
    Adds a 'imagine' field to each place by querying Wikipedia.
    Only queries attractions — restaurants are skipped (rarely have Wikipedia pages).
    Prints progress for each lookup.

    Parameters:
        places (list[dict]): List of place dicts from extract_places().

    Returns:
        list[dict]: Same list with 'imagine' field added to each entry.
    """
    total = len(places)
    for i, place in enumerate(places, start=1):
        name = place["name"]
        print(f"[INFO] Wikipedia image lookup {i}/{total}: {name}")
        place["imagine"] = get_wikipedia_image(name)
        time.sleep(0.1)
    return places


# ---------------------------------------------------------------------------
# SAVE JSON
# ---------------------------------------------------------------------------

def save_json(data, filepath):
    """
    Saves data to a JSON file with UTF-8 encoding and indentation.

    Parameters:
        data     (dict | list): Data to serialize.
        filepath (str): Output file path.

    Raises:
        SystemExit: If the file cannot be written.
    """
    try:
        with open(filepath, "w", encoding="utf-8") as f:
            json.dump(data, f, indent=2, ensure_ascii=False)
    except OSError as e:
        print(f"[ERROR] Could not write JSON file '{filepath}': {e}")
        sys.exit(1)


# ---------------------------------------------------------------------------
# SAVE TO SQLITE
# ---------------------------------------------------------------------------

def save_to_db(attractions, restaurants, city, db_file=DB_FILE):
    """
    Inserts attractions and restaurants into the SQLite database.
    Clears existing records for the given city before inserting to avoid
    duplicates on repeated calls.

    Parameters:
        attractions  (list[dict]): Extracted attraction records.
        restaurants  (list[dict]): Extracted restaurant records.
        city         (str): City name used as partition key.
        db_file      (str): Path to the SQLite database file.
    """
    conn = sqlite3.connect(db_file)
    cursor = conn.cursor()

    cursor.execute("""
        CREATE TABLE IF NOT EXISTS atractii (
            id           INTEGER PRIMARY KEY AUTOINCREMENT,
            oras         TEXT,
            nume         TEXT,
            categorie    TEXT,
            lat          REAL,
            lon          REAL,
            program      TEXT,
            website      TEXT,
            descriere    TEXT,
            telefon      TEXT,
            wheelchair   TEXT,
            taxa_intrare TEXT,
            strada       TEXT,
            numar        TEXT,
            imagine      TEXT
        )
    """)

    cursor.execute("""
        CREATE TABLE IF NOT EXISTS restaurante (
            id           INTEGER PRIMARY KEY AUTOINCREMENT,
            oras         TEXT,
            nume         TEXT,
            categorie    TEXT,
            lat          REAL,
            lon          REAL,
            program      TEXT,
            website      TEXT,
            descriere    TEXT,
            telefon      TEXT,
            wheelchair   TEXT,
            taxa_intrare TEXT,
            strada       TEXT,
            numar        TEXT,
            imagine      TEXT
        )
    """)

    cursor.execute("DELETE FROM atractii")
    cursor.execute("DELETE FROM restaurante")

    for a in attractions:
        cursor.execute("""
            INSERT INTO atractii
            (oras, nume, categorie, lat, lon, program, website, descriere,
             telefon, wheelchair, taxa_intrare, strada, numar, imagine)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            city, a["name"], a["category"], a["lat"], a["lon"],
            a["opening_hours"], a["website"], a["description"],
            a["phone"], a["wheelchair"], a["fee"],
            a["addr_street"], a["addr_housenr"], a["imagine"]
        ))

    for r in restaurants:
        cursor.execute("""
            INSERT INTO restaurante
            (oras, nume, categorie, lat, lon, program, website, descriere,
             telefon, wheelchair, taxa_intrare, strada, numar, imagine)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """, (
            city, r["name"], r["category"], r["lat"], r["lon"],
            r["opening_hours"], r["website"], r["description"],
            r["phone"], r["wheelchair"], r["fee"],
            r["addr_street"], r["addr_housenr"], r["imagine"]
        ))

    conn.commit()
    conn.close()
    print(f"[INFO] Saved {len(attractions)} attractions and {len(restaurants)} restaurants for '{city}' to database.")


# ---------------------------------------------------------------------------
# MAIN
# ---------------------------------------------------------------------------

params = read_input()

city          = params["city"]
selected_tags = params["tags"]

print(f"[INFO] City       : {city}")
print(f"[INFO] Tags       : {selected_tags}")
print(f"[INFO] Valid tags : {list(TAG_DICTIONARY.keys())}")

# Step 1: Geocode city
bbox = get_bounding_box(city)
if bbox is None:
    print(f"[ERROR] Cannot continue without a valid bounding box for '{city}'.")
    sys.exit(1)

# Step 2: Build attraction filters from user tags
attraction_filters = build_filters_for_tags(selected_tags)
if not attraction_filters:
    print("[ERROR] No valid tags produced any filters. Exiting.")
    sys.exit(1)

# Step 3: Query attractions
print("[INFO] Querying Overpass API for attractions...")
attraction_query  = build_overpass_query(bbox, attraction_filters, max_results=MAX_RESULTS * 3)
attraction_raw    = query_overpass(attraction_query)
attraction_elems  = attraction_raw.get("elements", [])
attractions       = extract_places(attraction_elems, category_label="attraction")

# Step 4: Query restaurants (always, independent of user tags)
print("[INFO] Querying Overpass API for restaurants...")
restaurant_query  = build_overpass_query(bbox, RESTAURANT_FILTERS, max_results=MAX_RESULTS * 3)
restaurant_raw    = query_overpass(restaurant_query)
restaurant_elems  = restaurant_raw.get("elements", [])
restaurants       = extract_places(restaurant_elems, category_label="restaurant")

print(f"[INFO] Found {len(attractions)} attractions and {len(restaurants)} restaurants.")

if not attractions and not restaurants:
    print("[WARNING] No results found. Check city name or try different tags.")
    sys.exit(0)

# Step 5: Enrich attractions with Wikipedia images
print("[INFO] Fetching Wikipedia images for attractions...")
attractions = enrich_with_images(attractions)

# Step 6: Save JSON outputs
save_json(
    {"city": city, "tags": selected_tags, "attractions": attractions, "restaurants": restaurants},
    os.path.join(OUTPUT_FOLDER, "atractii_filtered.json")
)
save_json(
    {"raw_attractions": attraction_raw, "raw_restaurants": restaurant_raw},
    os.path.join(OUTPUT_FOLDER, "atractii_raw.json")
)

# Step 7: Save to SQLite
save_to_db(attractions, restaurants, city)

print("Done! Files created:")
print("  - atractii_filtered.json  (date extrase)")
print("  - atractii_raw.json       (raspuns brut Overpass)")
print(f"  - {DB_FILE}  (baza de date SQLite)")
