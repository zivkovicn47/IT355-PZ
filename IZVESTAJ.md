# Izveštaj o implementaciji - Projekat IT355 PZ01

Ovaj dokument sadrži pregled do sada završenih zadataka u okviru prvog dela projektnog zadatka (PZ01) iz predmeta **IT355 - Veb sistemi 2** na Metropolitan univerzitetu.

## 1. Struktura Paketa
Uspostavljena je standardna struktura Spring Boot Web MVC projekta unutar paketa `com.metropolitan.it355pz`:
- `model` - Sadrži POJO klase koje predstavljaju entitete sistema.
- `repository` - Sadrži in-memory skladište podataka (simulacija baze).
- `service` - Sloj poslovne logike.
- `controller` - MVC kontroleri.

## 2. Model Klase (POJO)
Kreirano je 5 model klasa sa privatnim atributima, praznim (bez parametara) konstruktorom, konstruktorom sa svim poljima, kao i svim pratećim getter i setter metodama:

1. **[Komponenta.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/model/Komponenta.java)**
   - Polja: `Long id`, `String naziv`, `String serijskiBroj`, `String proizvodjac`, `String status` (npr. "Na stanju", "Ugrađeno")
2. **[Licenca.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/model/Licenca.java)**
   - Polja: `Long id`, `String nazivSoftvera`, `String kljucLicence`, `String tipLicence`, `boolean aktivna`
3. **[Inzenjer.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/model/Inzenjer.java)**
   - Polja: `Long id`, `String ime`, `String prezime`, `String email`, `String uloga` (npr. "PLC Programer")
4. **[Projekat.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/model/Projekat.java)**
   - Polja: `Long id`, `String naziv`, `String klijent`, `String status` (npr. "U radu", "Završeno")
5. **[Zadatak.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/model/Zadatak.java)**
   - Polja: `Long id`, `String opis`, `Long projekatId`, `Long inzenjerId`, `boolean zavrsen`

## 3. Repository Sloj (In-Memory Skladište)
Implementirana je klasa **[InMemoryRepository.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/repository/InMemoryRepository.java)** označena Springovom `@Repository` anotacijom:
- Podaci se čuvaju u privatnim listama (`ArrayList`) u memoriji aplikacije tokom njenog rada.
- Automatsko generisanje ID-jeva je rešeno preko brojača (npr. `nextKomponentaId = 1L`) koji se uvećavaju pri svakom dodavanju novog objekta kojem nije eksplicitno postavljen ID.
- **Inicijalizacija podataka**: Prilikom startovanja aplikacije (u konstruktoru repozitorijuma), poziva se metoda `inicijalizujPodatke()` koja popunjava sistem sa po 2-3 povezana test objekta kako bi se olakšao dalji rad.
- **CRUD Metode**: Za svaki entitet obezbeđene su standardne metode za rad sa podacima (`dodaj...`, `get...()`, `get...ById(Long id)`, `azuriraj...`, `obrisi...`).

## 4. Servisni Sloj (Service)
Kreirana je klasa **[AutomatizacijaService.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/service/AutomatizacijaService.java)** označena sa `@Service`:
- Povezana je sa `InMemoryRepository` putem konstruktorske injekcije.
- Sadrži poslovne/CRUD metode za svih 5 entiteta koje delegiraju pozive repozitorijumu:
  - `getSveProjekte()` / `getSveKomponente()` / `getSveInzenjere()` / `getSveLicence()` / `getSveZadatke()`
  - `getProjekatById(id)` / `getKomponentaById(id)` / `getInzenjerById(id)` / `getLicencaById(id)` / `getZadatakById(id)`
  - `sacuvaj...` (automatski detektuje kreiranje ili izmenu)
  - `obrisi...` (brisanje po ID-ju)

## 5. Kontrolerski Sloj (Controller)
Kreirani su Spring Web MVC kontroleri sa konstruktorskom injekcijom servisa:
- **[HomeController.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/controller/HomeController.java)** (Ruta: `/` -> prikazuje `index.html`)
- **[ProjekatController.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/controller/ProjekatController.java)** (Ruta: `/projekti`)
- **[KomponentaController.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/controller/KomponentaController.java)** (Ruta: `/komponente`)
- **[InzenjerController.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/controller/InzenjerController.java)** (Ruta: `/inzenjeri`)
- **[LicencaController.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/controller/LicencaController.java)** (Ruta: `/licence`)
- **[ZadatakController.java](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/java/com/metropolitan/it355pz/controller/ZadatakController.java)** (Ruta: `/zadaci`)

Za sve poslovne kontrolere su implementirane sledeće rute za povezivanje sa Thymeleaf šablonima:
- `GET ""` — prikazuje tabelu svih stavki
- `GET "/novi"` — prikazuje formu za dodavanje (u `ZadatakController` se dodatno prosleđuju liste projekata i inženjera)
- `POST "/sacuvaj"` — prihvata popunjen objekat i vrši redirect na listu
- `GET "/izmeni/{id}"` — pronalazi objekat i otvara formu za izmenu (u `ZadatakController` se dodatno prosleđuju liste projekata i inženjera)
- `GET "/obrisi/{id}"` — briše objekat i vrši redirect na listu

## 6. Korisnički interfejs (View - Thymeleaf & CSS)
Za potrebe korisničkog interfejsa kreirani su sledeći resursi:
- **[style.css](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/resources/static/css/style.css)**: Custom CSS stilovi sa modernom teget/sivom dashboard temom (clean-tech, zaobljene ivice, specifične klase boja za akcije i prelazne animacije pri hover-u).
- **[navbar.html](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/resources/templates/zajedno/navbar.html)**: Zajednička navigacija sa dinamičkim isticanjem aktivne stranice.
- **[index.html](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6.%20Semestar/IT355%20-%20Veb%20sistemi%202/IT355-PZ/IT355-PZ-01/it355pz/src/main/resources/templates/index.html)**: Početna stranica dobrodošlice sa dashboard-om od 5 kartica i brzim prečicama.
- **Šabloni za module (Projekti, Komponente, Inženjeri, Licence, Zadaci)**:
  - `lista.html` - Prikaz tabele podataka sa CRUD linkovima (kod zadataka su prikazana tekstualna imena projekata i inženjera umesto sirovih ID-jeva).
  - `forma.html` - Forme za unos i ažuriranje (kod zadataka su implementirani padajući meniji za izbor projekta i dodeljenog inženjera).

### Responzivnost i Mobilna Verzija
Aplikacija je u potpunosti prilagođena za sve veličine ekrana (mobilni telefoni, tableti, desktop):
1. **Hamburger Meni (Navbar)**:
   - Integrisano je funkcionalno hamburger dugme sa animacijom tri vodoravne linije koje se pri kliku transformišu u znak "X".
   - Korišćen je nativni JavaScript unutar `navbar.html` za preklapanje (toggle) vidljivosti navigacionih linkova na mobilnim uređajima, bez ometanja funkcionalnosti selekcije teksta na stranici.
2. **Prilagođavanje Rasporeda (Media Queries)**:
   - Na širinama ekrana manjim od **768px** (tableti i veći telefoni), navigacioni linkovi se sklapanjem pretvaraju u padajući meni koji se pozicionira preko z-indexa.
   - Prikaz tabela se prilagođava mobilnim ekranima kroz smanjenje margina i unutrašnjih razmaka (`padding`), dok se akciona dugmad u zaglavlju slažu vertikalno.
   - Na širinama ekrana manjim od **576px** (mobilni telefoni), dashboard kartice na početnoj strani se raspoređuju u jednu kolonu, a akciona dugmad unutar formi se slažu vertikalno i zauzimaju punu širinu ekrana za lakše korišćenje na dodir.

## 7. Verifikacija
- Projekat je uspešno pokrenut u terminalu komandom:
   `./mvnw spring-boot:run`
- Izvršeno je testiranje HTTP odziva preko lokalnih mrežnih zahteva:
  - Putanja `http://localhost:8080/` -> **StatusCode: 200 OK** (Potvrđeno učitavanje responzivne navigacije i stilova)
  - Putanja `http://localhost:8080/projekti` -> **StatusCode: 200 OK**
  - Putanja `http://localhost:8080/komponente` -> **StatusCode: 200 OK**
  - Putanja `http://localhost:8080/inzenjeri` -> **StatusCode: 200 OK**
  - Putanja `http://localhost:8080/licence` -> **StatusCode: 200 OK**
  - Putanja `http://localhost:8080/zadaci` -> **StatusCode: 200 OK**
- Sve Thymeleaf stranice se ispravno renderuju, responzivne su na promenu širine prozora i funkcionišu na mobilnim uređajima.

