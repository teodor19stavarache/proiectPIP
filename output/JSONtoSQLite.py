import json
import sqlite3
import sys
import os
import re


DB_FILE   =  "baza.db"
JSON_FILE = "hotels_filtered.json"


def parse_price(price_str):
    """
    Converts a price string with currency into a float number.
    Example: "167 EUR" -> 167.0, "N/A" -> None

    Parameters:
        price_str (str): Price string containing a number and currency code.

    Returns:
        float | None: The extracted price as a float, or None if not parseable.
    """
    if price_str == "N/A" or not price_str:
        return None
    match = re.search(r"[\d,]+", price_str)
    if match:
        return float(match.group().replace(",", ""))
    return None


def parse_hotel_class(hotel_class_str):
    """
    Extracts the star rating number from a hotel class string.
    Example: "3-star tourist hotel" -> 3, "N/A" -> None

    Parameters:
        hotel_class_str (str): Hotel class string from the API.

    Returns:
        int | None: The star number as an integer, or None if not parseable.
    """
    if hotel_class_str == "N/A" or not hotel_class_str:
        return None
    match = re.search(r"\d+", hotel_class_str)
    if match:
        return int(match.group())
    return None


def parse_rating(rating):
    """
    Ensures the rating value is a float.
    Example: 4.1 -> 4.1, "N/A" -> None

    Parameters:
        rating (float | str): Rating value from the hotel dictionary.

    Returns:
        float | None: The rating as a float, or None if not parseable.
    """
    if rating == "N/A" or rating is None:
        return None
    try:
        return float(rating)
    except (ValueError, TypeError):
        return None


def load_json(filepath):
    """
    Reads and parses the filtered hotels JSON file produced by hotelAPI2.py.

    Parameters:
        filepath (str): Path to the hotels_filtered.json file.

    Returns:
        list: A list of hotel dictionaries ready to be inserted into the database.

    Raises:
        SystemExit: If the file is not found or contains invalid JSON.
    """
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"[ERROR] JSON file not found: {filepath}")
        print("[HINT] Make sure hotelAPI2.py ran successfully before this script.")
        sys.exit(1)
    except json.JSONDecodeError as e:
        print(f"[ERROR] Invalid JSON in file '{filepath}': {e}")
        sys.exit(1)


def connect_db(db_path):
    """
    Opens a connection to the SQLite database file. Creates the file
    automatically if it does not already exist.

    Parameters:
        db_path (str): Path to the SQLite database file (e.g. "output/baza.db").

    Returns:
        tuple: A (conn, cursor) tuple where:
               - conn   (sqlite3.Connection): The active database connection.
               - cursor (sqlite3.Cursor):     A cursor for executing SQL statements.

    Raises:
        SystemExit: If the database cannot be opened or created.
    """
    try:
        conn   = sqlite3.connect(db_path)
        cursor = conn.cursor()
        cursor.execute("PRAGMA foreign_keys = ON")
        return conn, cursor
    except sqlite3.Error as e:
        print(f"[ERROR] Could not connect to database '{db_path}': {e}")
        sys.exit(1)


def create_tables(cursor):
    """
    Drops existing tables (if any) and recreates them with the correct schema.
    Prices, rating, and hotel_class are stored as numeric types.

    Tables created:
        - hotels:         Main hotel information.
        - nearby_places:  Places of interest near each hotel.
        - transportation: Transportation options to each nearby place.

    Parameters:
        cursor (sqlite3.Cursor): An active database cursor.

    Returns:
        None

    Raises:
        SystemExit: If any SQL statement fails during table creation.
    """
    try:
        cursor.execute("DROP TABLE IF EXISTS transportation")
        cursor.execute("DROP TABLE IF EXISTS nearby_places")
        cursor.execute("DROP TABLE IF EXISTS hotels")

        cursor.execute("""
            CREATE TABLE IF NOT EXISTS hotels (
                id              INTEGER PRIMARY KEY AUTOINCREMENT,
                name            TEXT NOT NULL,
                rate_per_night  REAL,
                total_rate      REAL,
                link            TEXT,
                rating          REAL,
                hotel_class     INTEGER,
                image           TEXT
            )
        """)

        cursor.execute("""
            CREATE TABLE IF NOT EXISTS nearby_places (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                hotel_id INTEGER,
                name     TEXT,
                FOREIGN KEY (hotel_id) REFERENCES hotels(id)
            )
        """)

        cursor.execute("""
            CREATE TABLE IF NOT EXISTS transportation (
                id       INTEGER PRIMARY KEY AUTOINCREMENT,
                hotel_id INTEGER,
                place_id INTEGER,
                type     TEXT,
                duration TEXT,
                FOREIGN KEY (place_id) REFERENCES nearby_places(id)
            )
        """)
    except sqlite3.Error as e:
        print(f"[ERROR] Failed to create tables: {e}")
        sys.exit(1)


def insert_hotels(cursor, data):
    """
    Iterates over the list of hotel dictionaries and inserts each one into
    the hotels, nearby_places, and transportation tables using parameterized
    queries to prevent SQL injection. Prices, rating and hotel_class are
    converted to numeric types before insertion.

    Parameters:
        cursor (sqlite3.Cursor): An active database cursor.
        data   (list): List of hotel dictionaries from load_json().
                       Each dict must contain: name, rate_per_night, total_rate,
                       link, rating, hotel_class, image, nearby_places.

    Returns:
        int: The total number of hotels successfully inserted.

    Raises:
        SystemExit: If a critical database error occurs during insertion.
    """
    inserted = 0
    for hotel in data:
        try:
            # Convert fields to numeric types
            rate_per_night = parse_price(hotel["rate_per_night"])
            total_rate     = parse_price(hotel["total_rate"])
            rating         = parse_rating(hotel["rating"])
            hotel_class    = parse_hotel_class(hotel.get("hotel_class", "N/A"))

            cursor.execute(
                """INSERT INTO hotels (name, rate_per_night, total_rate, link, rating, hotel_class, image)
                   VALUES (?, ?, ?, ?, ?, ?, ?)""",
                (
                    hotel["name"],
                    rate_per_night,   # REAL  e.g. 167.0
                    total_rate,       # REAL  e.g. 333.0
                    hotel["link"],
                    rating,           # REAL  e.g. 4.1
                    hotel_class,      # INTEGER e.g. 3
                    hotel.get("image", "N/A")
                )
            )
            hotel_id = cursor.lastrowid
            inserted += 1

            for place in hotel.get("nearby_places", []):
                cursor.execute(
                    "INSERT INTO nearby_places (hotel_id, name) VALUES (?, ?)",
                    (hotel_id, place.get("name"))
                )
                place_id = cursor.lastrowid

                for transport in place.get("transportations", []):
                    cursor.execute(
                        "INSERT INTO transportation (hotel_id, place_id, type, duration) VALUES (?, ?, ?, ?)",
                        (hotel_id, place_id, transport.get("type"), transport.get("duration"))
                    )

        except sqlite3.Error as e:
            print(f"[WARNING] Skipping hotel '{hotel.get('name', 'unknown')}' due to DB error: {e}")
            continue

    return inserted


# ---------------------------------------------------------------------------
# MAIN
# ---------------------------------------------------------------------------

# --- Load data from JSON ---
print(f"[INFO] Reading data from '{JSON_FILE}'...")
data = load_json(JSON_FILE)

if not data:
    print("[WARNING] No hotel data found in JSON file. Nothing to insert.")
    sys.exit(0)

# --- Connect to database ---
print(f"[INFO] Connecting to database '{DB_FILE}'...")
conn, cursor = connect_db(DB_FILE)

# --- Create tables ---
print("[INFO] Creating tables...")
create_tables(cursor)

# --- Insert data ---
print(f"[INFO] Inserting {len(data)} hotels into the database...")
inserted = insert_hotels(cursor, data)

# --- Save and close ---
try:
    conn.commit()
    conn.close()
except sqlite3.Error as e:
    print(f"[ERROR] Failed to commit and close database: {e}")
    sys.exit(1)

print(f"Done! {inserted}/{len(data)} hotels inserted into '{DB_FILE}'.")
