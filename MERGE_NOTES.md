# Merge Notes - Geo branch + L_Irinel

Acest fisier descrie tot ce s-a schimbat in partea de GUI (acest folder
`JavaGUI/`) si ce trebuie sa contina branch-ul `L_Irinel` ca totul sa compileze
si sa functioneze impreuna dupa merge.


## 1. Layout-ul JavaGUI a fost convertit la Maven

- `pom.xml` la radacina, cu dependintele:
  - `org.xerial:sqlite-jdbc:3.45.3.0`  (DB)
  - `org.json:json:20240303`           (parsing JSON pentru SearchPage)
- Sursele s-au mutat din `JavaGUI/pck1/` in `JavaGUI/src/main/java/pck1/`
- `.classpath` si `.project` (Eclipse-only) au fost sterse.

Comanda de rulare:
    mvn exec:java
sau jar standalone:
    mvn package    -> target/ProiectPIP-GUI-1.0-SNAPSHOT.jar


## 2. Importuri din L_Irinel folosite de GUI

Codul din `JavaGUI/src/main/java/pck1/` importa urmatoarele clase pe care le
asteapta din branch-ul L_Irinel:

    pck1.Proiect_PIP.DataBase_Utilizator
    pck1.Proiect_PIP.Utilizator

Aceste clase trebuie sa se afle in pachetul `pck1.Proiect_PIP` dupa merge,
asa cum sunt acum pe L_Irinel. Daca le mutati in alt pachet, schimbati
importurile in fisierele:
    LoginPage.java, RegisterPage.java, ProfilPage.java, Session.java


## 3. Metoda noua necesara in DataBase_Utilizator

`LoginPage.java` apeleaza:

    Utilizator full = DataBase_Utilizator.getUtilizator(email);

Aceasta metoda NU exista pe L_Irinel inca. Adaugati urmatorul cod in
`DataBase_Utilizator.java`:

```java
/**
 * Incarca un utilizator complet (cu taguri) din baza de date dupa email.
 * @param email - emailul cautat
 * @return Utilizator sau null daca nu exista
 */
public static Utilizator getUtilizator(String email) {
    String sql = "SELECT email, nume, parola, numar_telefon, taguri "
               + "FROM utilizator_db WHERE email = ?";
    try (Connection conn = DriverManager.getConnection(url);
         PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, email);
        try (ResultSet rs = ps.executeQuery()) {
            if (!rs.next()) return null;
            String taguri = rs.getString("taguri");
            java.util.List<String> tags = new java.util.ArrayList<>();
            if (taguri != null && !taguri.isEmpty()) {
                for (String t : taguri.split(",")) {
                    String trimmed = t.trim();
                    if (!trimmed.isEmpty()) tags.add(trimmed);
                }
            }
            return new Utilizator(
                rs.getString("email"),
                rs.getString("nume"),
                Utilizator.decode(rs.getString("parola")),
                rs.getString("numar_telefon"),
                tags
            );
        }
    } catch (Exception e) {
        System.out.println(e);
        return null;
    }
}
```


## 4. Optional: setter pentru tags in Utilizator

`ProfilPage.java` cand salveaza tag-urile noi creeaza un Utilizator nou
prin constructor. Daca preferati sa mutati doar tag-urile pe obiectul
existent, adaugati in `Utilizator.java`:

```java
public void setTags(java.util.List<String> t) {
    this.tags = t;
}
```


## 5. Fisierul bazei de date

`DataBase_Utilizator.java` foloseste calea relativa `database/PIP_P.db`.
Asta inseamna ca executia trebuie facuta din radacina proiectului. Acest
fisier (`PIP_P.db`) trebuie sa fie commited in branch-ul Geo dupa merge,
in folder-ul `database/` de la radacina repo-ului.

La prima rulare apelati o singura data `creareTabela()` ca sa creati tabela
daca DB-ul e gol. (Optional: pot adauga un apel automat in Main.java.)


## 6. Fluxul complet dupa merge

1. User pornind aplicatia -> LoginPage
2. Click "Creeaza cont" -> RegisterPage:
   - Pasul 1: nume, prenume, email, parola, telefon
   - Pasul 2: alege exact 3 din 10 categorii (CategoryPicker)
   - La submit: DataBase_Utilizator.adauga_utilizator(util)
3. LoginPage:
   - DataBase_Utilizator.verifica_parola(util)
   - DataBase_Utilizator.getUtilizator(email) -> incarca tag-urile
   - Session.login(user) populeaza singleton-ul
4. Sidebar -> butonul "Cauta sejur" citeste Session.getUser().getTags()
   si apeleaza nav.runSearch(city, tagsCsv)
5. LoadingScreen indeterminate pana scraper-ul Python termina
6. SearchPage citeste hotelAPI/output/atractii_filtered.json si
   hotels_filtered.json si afiseaza: 5 hoteluri + 5 atractii (2+2+1
   pe cele 3 categorii preferate) + 3 restaurante
7. ProfilPage: butonul "Editeaza" deschide CategoryPicker; "Salveaza"
   apeleaza DataBase_Utilizator.update_tags(user, newTags)
