import sys, sqlite3
sys.stdout.reconfigure(encoding='utf-8')
conn = sqlite3.connect('output/atractii.db')
cur = conn.cursor()

cur.execute("SELECT COUNT(*) FROM atractii WHERE imagine != 'N/A'")
cu_img = cur.fetchone()[0]
cur.execute("SELECT COUNT(*) FROM atractii")
total = cur.fetchone()[0]
print(f"Atractii cu imagine: {cu_img}/{total}")

print("\n=== Exemple cu imagine ===")
for row in cur.execute("SELECT nume, categorie, imagine FROM atractii WHERE imagine != 'N/A' LIMIT 6"):
    print(f"  {row[0]} ({row[1]})")
    print(f"  -> {row[2]}")
    print()
conn.close()
