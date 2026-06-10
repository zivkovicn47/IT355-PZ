# UNIVERZITET METROPOLITAN
## FAKULTET INFORMACIONIH TEHNOLOGIJA

**Predmet:** IT355 - Veb sistemi 2  
**Projekat:** Projektni zadatak 2 (PZ02)  

<br><br><br>

# PROJEKTNA DOKUMENTACIJA
## AUTOMATIKOM MANAGEMENT SYSTEM

<br><br><br><br><br>

**Student:** Nikola Živković  
**Indeks:** 6090  
**Profesor:**  Jovana Jović
**Asistent:**  Vladan Ristić

**Datum:** Jun, 2026.  

---

## Sadržaj
1. Uvod
2. Planiranje i dizajn sistema
3. Razvoj korisničkog interfejsa (UI)
4. Backend implementacija i slojevita arhitektura
5. Implementacija bezbednosti (Spring Security i JWT)
6. Testiranje aplikacije (Unit i Integration testovi)
7. Zaključak i prilozi

---

## 1. Uvod

### 1.1. Opis Teme
U savremenoj industriji, sistemi automatizacije i kontrole (PLC, SCADA, HMI, industrijske mreže) predstavljaju osnovu proizvodnih procesa. Upravljanje projektima u ovoj oblasti zahteva precizno praćenje inženjerskog kadra, instalirane opreme (komponenti), licenci softverskih paketa za programiranje, kao i samih zadataka na projektima. 

**Automatikom Management System** je softversko rešenje kreirano kao Full-Stack veb aplikacija, dizajnirana da omogući centralizovano upravljanje inženjerskim projektima industrijske automatizacije. Sistem služi preduzećima koja se bave integracijom sistema da prate svoje resurse na efikasan način, obezbeđujući sigurnost podataka i jasnu raspodelu posla.

### 1.2. Tehnološki Stak
Aplikacija je izgrađena korišćenjem modernih tehnologija i najboljih praksi u razvoju višeslojnih Full-Stack veb sistema:
- **Java 21 (LTS)**: Korišćene su najnovije jezičke performanse i unapređenja u produktivnosti.
- **Spring Boot 4.0.6**: Okvir koji obezbeđuje brz razvoj i konfiguraciju serverske strane.
- **Spring Data JPA**: Za napredno objektno-relaciono mapiranje i rad sa bazom podataka.
- **H2 Baza Podataka**: Brza, *in-memory* relaciono baza podataka korišćena za lokalni razvoj i testiranje.
- **React 18 & Vite**: Brzo i optimizovano frontend okruženje zasnovano na komponentama.
- **Spring Security & JSON Web Token (JWT)**: Za kontrolu pristupa, stateless sesije i bezbednu autorizaciju korisnika na osnovu uloga.
- **Apache POI (v5.2.5)**: Biblioteka za dinamičko generisanje i stilizovanje Excel (.xlsx) izveštaja na backendu.
- **JUnit 5 i Mockito**: Za jedinično testiranje servisnog sloja.
- **MockMvc**: Za integraciono testiranje REST API kontrolera.

---

## 2. Planiranje i Dizajn Sistema

### 2.1. Funkcionalni Zahtevi i Use-Case Akteri
Sistem identifikuje dva osnovna aktera koji pristupaju platformi:
1. **Korisnik (USER)**:
   - Može da se prijavi na sistem sa svojim kredencijalima.
   - Ima pravo pregleda (Read-Only) svih 5 sistemskih entiteta (Projekti, Komponente, Inženjeri, Licence i Zadaci).
   - Nema dozvolu za dodavanje, izmenu ili brisanje bilo kog podatka.
2. **Administrator (ADMIN)**:
   - Ima pune privilegije nad sistemom (Create, Read, Update, Delete - CRUD).
   - Može da dodaje nove inženjere, dodeljuje zadatke projektima, ugrađuje komponente, produžava licence i briše zastarele zapise.
   - Vidi akciona dugmad i modalne forme koje su sakrivene od običnog korisnika.

### 2.2. Dizajn Baze Podataka (JPA Entiteti i Relacije)
Sistem se sastoji od 5 povezanih relacionih entiteta, čime je ispunjen zahtev projekta za rad sa najmanje pet tabela u bazi podataka:

1. **Projekat (Projekat)**:
   - Predstavlja industrijski projekat automatizacije.
   - *Polja*: `id` (Long, PK), `naziv` (String), `klijent` (String), `status` (String - "U radu", "Zavrseno").
2. **Komponenta (Komponenta)**:
   - Predstavlja hardversku ili softversku opremu (npr. PLC, HMI, mrežni switch) koja se ugrađuje na projektima.
   - *Polja*: `id` (Long, PK), `naziv` (String), `serijskiBroj` (String), `proizvodjac` (String), `status` (String - "Na stanju", "Ugrađeno").
3. **Inženjer (Inzenjer)**:
   - Predstavlja člana tima zaposlenog na poslovima automatizacije.
   - *Polja*: `id` (Long, PK), `ime` (String), `prezime` (String), `email` (String), `uloga` (String - npr. "PLC Programer", "SCADA Inženjer").
4. **Licenca (Licenca)**:
   - Predstavlja licencu softvera (npr. Siemens TIA Portal, Rockwell Studio 5000) neophodnu za programiranje opreme.
   - *Polja*: `id` (Long, PK), `nazivSoftvera` (String), `kljucLicence` (String), `tipLicence` (String), `aktivna` (boolean).
5. **Zadatak (Zadatak)**:
   - Predstavlja dodeljenu inženjersku aktivnost na određenom projektu.
   - *Polja*: `id` (Long, PK), `opis` (String), `projekatId` (Long, FK ka Projekat), `inzenjerId` (Long, FK ka Inzenjer), `zavrsen` (boolean).

#### Relacije:
U modelu baze podataka, entitet **Zadatak** služi kao vezni entitet koji povezuje **Projekat** i **Inženjera** (mnogo-prema-jedan relacije mapirane preko spoljnih ključeva `projekatId` i `inzenjerId`), omogućavajući kreiranje zadataka za specifične inženjere na konkretnim projektima.

---

## 3. Razvoj Korisničkog Interfejsa (UI)

Korisnički interfejs je implementiran u **React** tehnologiji i pruža responzivan i moderan dizajn koji se prilagođava rezolucijama desktop računara i mobilnih uređaja.

### 3.1. Login Stranica
Pristup sistemu je kontrolisan preko `Login` komponente. Korisnik unosi korisničko ime i lozinku. Forma šalje podatke backend REST API-ju. Nakon uspešne prijave, JWT token se trajno čuva u `localStorage`-u pod ključem `token`, a korisnik se preusmerava na radni panel.

### 3.2. Centralni Layout i Sidebar Navigacija
Aplikacija koristi deljeni `ProtectedLayout` koji sadrži:
- **Sidebar (Bočna navigacija)**: Prikazuje se sa leve strane na desktopu. Sadrži brend, profil ulogovanog korisnika (sa imenom i ulogom) i linkove (`NavLink`) za prelazak između entiteta. Na dnu se nalazi dugme "Odjavi se" za brisanje sesije.
- **Mobile UI & Hamburger meni**: Na ekranima širine manje od `768px`, Sidebar se automatski sakriva van ekrana (`translateX(-100%)`). Na vrhu ekrana se pojavljuje stiki traka sa "hamburger" dugmetom. Klikom na njega, meni glatko uklizava sa leve strane preko sadržaja, sa zatamnjenom i zamućenom pozadinom (`backdrop-filter`).
- **Glavni sadržaj**: Prikazuje se sa desne strane. Sadrži naslov stranice, dugme za dodavanje (samo za ADMIN-e) i tabelu sa podacima.

### 3.3. CRUD Operacije i Role-Based UI (Uloge na UI-ju)
Sve stranice entiteta koriste `jwt-decode` biblioteku za analizu sadržaja JWT tokena. 
- Ako token sadrži rolu `ROLE_ADMIN`, state varijabla `isAdmin` se postavlja na `true`.
- **Conditional Rendering (Uslovno prikazivanje)**: Na osnovu stanja `isAdmin`, React na svim tabelama dinamički prikazuje ili sakriva dugme "Dodaj", kolonu "Akcije" i dugmiće za izmenu ("Izmeni") i brisanje ("Obriši").
- **Modalni prozori za CRUD**: Klikom na dugme za dodavanje ili izmenu otvara se interaktivni modalni prozor sa animacijom (`slideUp`). Po čuvanju forme, šalje se POST zahtev na backend, zatvara se modal i automatski se osvežavaju podaci u tabeli bez osvežavanja cele stranice.
- **Horizontalno skrolovanje tabela**: Sve tabele su umotane u `.table-responsive` klase sa `overflow-x: auto` i `white-space: nowrap`, čime se sprečava lomljenje rasporeda i omogućava prevlačenje tabele levo-desno na mobilnim telefonima.

---

## 4. Backend Implementacija i Slojevita Arhitektura

Backend deo aplikacije prati klasičnu troslojnu arhitekturu preporučenu za Enterprise Spring Boot aplikacije:

```
[Klijent / React App] 
        │
        ▼
[Controller Sloj (REST API)] ───► Prima HTTP zahteve, rukuje DTO objektima
        │
        ▼
[Service Sloj (Poslovna logika)] ───► Transakcije, poslovna pravila
        │
        ▼
[Repository Sloj (Prolećni podaci)] ───► JPA/Hibernate rad sa bazom
        │
        ▼
[Baza Podataka (H2)]
```

### 4.1. Slojevi Aplikacije
1. **Controller Sloj (REST Controllers)**:
   - Svi kontroleri su anotirani sa `@RestController` i mapirani na `/api/...`.
   - Prihvataju zahteve u JSON formatu preko `@RequestBody` i vraćaju podatke upakovane u `ResponseEntity`.
   - Primer: `ProjekatController` upravlja rutama `/api/projekti`, `/api/projekti/sacuvaj` i `/api/projekti/obrisi/{id}`.
2. **Service Sloj (`AutomatizacijaService`)**:
   - Predstavlja centralno mesto poslovne logike aplikacije.
   - Metode za izmenu, čuvanje i brisanje podataka su označene sa `@Transactional` kako bi se osigurao integritet baze.
   - Ubrizgava sve JPA repozitorijume i obavlja preuzimanje i validaciju podataka.
3. **Repository Sloj**:
   - Interfejsi koji nasleđuju `JpaRepository` (npr. `ProjekatRepository`, `ZadatakRepository`).
   - Obezbeđuju standardne CRUD metode bez potrebe za pisanjem SQL upita.

### 4.2. Napredne Funkcionalnosti (Excel Export i Weather API)
Kako bi aplikacija ponudila više od standardnog CRUD interfejsa, implementirane su dve napredne serverske usluge:
1. **Izvoz podataka u Excel (`ExcelExportService`)**:
   - Koristi se Apache POI biblioteka za dinamičko prevođenje liste objekata `Projekat` u binarni `.xlsx` dokument.
   - Ćelije zaglavlja su stilizovane sa bold belim tekstom na tamnoplavoj pozadini, dodate su ivice oko svih ćelija za bolju čitljivost, i primenjeno je automatsko skaliranje širine kolona prema dužini sadržaja (`sheet.autoSizeColumn`).
   - Kontroler `ProjekatController` izlaže rutu `GET /api/projekti/export/excel` i vraća niz bajtova sa zaglavljima `Content-Type: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet` i `Content-Disposition: attachment; filename=projekti.xlsx`, što obezbeđuje preuzimanje fajla na klijentskoj strani.
2. **Integracija spoljnog Vremenskog API-ja (`WeatherService`)**:
   - Servis se povezuje sa eksternim servisom **OpenWeatherMap** (API rute su parametrizovane u `application.properties`).
   - Slanje HTTP GET zahteva obavlja se preko Spring klase `RestTemplate`. Parametar `units=metric` se koristi kako bi se temperatura dobila u Celzijusima.
   - Implementirano je sigurno rukovanje izuzecima (try-catch): ukoliko je API ključ nepostojeći (placeholder `TVOJ_API_KLJUC_OVDE`), grad nije pronađen ili nema mreže, servis to detektuje i vraća fallback JSON sa opisom greške, sprečavajući pad aplikacije.
   - Kontroler `WeatherController` izlaže endpoint `GET /api/weather?city={grad}` koji React frontend periodično poziva.

---

## 5. Implementacija Bezbednosti (Spring Security i JWT)

Bezbednosni sloj je implementiran korišćenjem **Spring Security** okvira i **JSON Web Token (JWT)** mehanizma.

### 5.1. Konfiguracija i Stateless Rad
U klasi `SecurityConfig` bezbednost je podešena na potpuno *stateless* rad (bez čuvanja sesije na serveru), što je standard za moderne REST API sisteme:
```java
http
    .cors(Customizer.withDefaults())
    .csrf(csrf -> csrf.disable())
    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
```
CORS je konfigurisan preko `CorsConfigurationSource` beana koji dozvoljava klijentima sa porta `5173` (React aplikacija) slanje svih HTTP metoda (GET, POST, PUT, DELETE, OPTIONS) i slanje Authorization hedera sa kredencijalima.

### 5.2. Autentifikacija pomoću JWT-a
1. **`JwtUtil.java`**:
   - Odgovorna za rad sa tokenom. Koristi HMAC HS256 algoritam sa 256-bitnim ključem.
   - Metode: `generateToken` (kreiranje tokena sa ubačenim listom uloga pod ključem `"roles"`), `extractUsername` (čitanje korisničkog imena iz `subject`-a) i `validateToken` (provera validnosti i isteka roka).
2. **`JwtAuthenticationFilter.java`**:
   - Filter koji nasleđuje `OncePerRequestFilter`. Presreće svaki HTTP zahtev.
   - Izdvaja token iz `Authorization` hedera (Bearer šema). Ako je token prisutan i validan, učitava korisnika iz baze (`UserDetailsService`) i postavlja ga u `SecurityContextHolder`.

### 5.3. Autorizacija na Osnovu Uloga (Role-Based Access Control - RBAC)
Rute na backendu su strogo zaštićene na osnovu uloga korisnika definisanih u Spring Security kontekstu:
- Rute za čitanje podataka (GET `/api/projekti/**`, `/api/komponente/**`, itd.) su otvorene za uloge `USER` i `ADMIN`.
- Rute za izmenu i brisanje podataka (POST `/api/.../sacuvaj`, DELETE `/api/.../obrisi/**`) su eksplicitno ograničene samo na ulogu `ADMIN`:
```java
.requestMatchers(
    "/api/projekti/sacuvaj", "/api/projekti/obrisi/**",
    "/api/komponente/sacuvaj", "/api/komponente/obrisi/**"
    // ...
).hasRole("ADMIN")
```

---

## 6. Testiranje Aplikacije (Unit i Integration Testovi)

Aplikacija sadrži razvijene jedinične (Unit) testove za servisni sloj i integracione (Integration) testove za kontrolerski sloj, čime se potvrđuje stabilnost koda.

### 6.1. Jedinični Testovi (Service Sloj)
Jedinični testovi su kreirani u klasi `AutomatizacijaServiceTest` u test direktorijumu:
- **Tehnologija**: JUnit 5 i Mockito.
- Klasa je anotirana sa `@ExtendWith(MockitoExtension.class)`.
- Repozitorijumi su mock-ovani (`@Mock`), a servis je testiran nezavisno od baze podataka (`@InjectMocks`).
- Testirane su 3 ključne metode servisa:
  - `testGetSveProjekte()`: Verifikuje vraćanje liste svih projekata koristeći Mockito `given()`.
  - `testSacuvajProjekat()`: Verifikuje ispravno pozivanje metode `save` repozitorijuma preko `verify()`.
  - `testObrisiProjekat()`: Verifikuje poziv metode `deleteById`.

### 6.2. Integracioni Testovi (Controller Sloj)
Integracioni testovi su kreirani u klasi `ProjekatControllerTest` za verifikaciju REST krajnjih tačaka:
- **Tehnologija**: Spring Boot Test i `MockMvc`.
- Klasa koristi anotacije `@SpringBootTest` i `@AutoConfigureMockMvc` za podizanje testnog Spring konteksta i konfigurisanje MockMvc klijenta.
- **Zaobilaženje JWT filtera**: Da bi se uspešno zaobišla zaštita nad REST API rutama bez stvarnog slanja tokena, korišćen je MockMvc `RequestPostProcessor` `.with(user("admin").roles("ADMIN"))` iz `spring-security-test` biblioteke.
- Test proverava da poziv `GET /api/projekti` vraća status **200 OK**.

---

## 7. Zaključak i Prilozi

### 7.1. Rezime Projekta
Tokom izrade projekta IT355 PZ02, uspešno je razvijen kompletan Full-Stack sistem za upravljanje inženjerskim resursima pod nazivom **Automatikom Management System**.
Aplikacija demonstrira slojevitu arhitekturu backenda sa Spring Boot-om, sigurnost na bazi JWT tokena bez čuvanja sesija na serveru, i moderan React frontend sa ulogama zasnovanim korisničkim interfejsom i responzivnim mobilnim drawer-om.

Pored standardnih CRUD operacija, u sistem su uspešno implementirane napredne integracije: izvoz projekata u Excel datoteke pomoću Apache POI biblioteke i asinhroni prikaz vremenske prognoze za Niš (terensku lokaciju) u realnom vremenu pomoću OpenWeather API-ja.

Tokom integracije, rešeni su izazovi poput bezbednog konfigurisanja CORS politika u Spring Security kako bi se pretraživačima eksplicitno izložilo zaglavlje `Content-Disposition` (za nesmetano preuzimanje datoteka preko klijentskog Axiosa) i implementacije robusnog try-catch mehanizma u servisnom sloju koji sprečava prekide u radu i vraća fallback podatke ukoliko API ključ za vreme nije konfigurisan.

Rad sistema je potvrđen kroz prolazak svih napisanih jediničnih i integracionih testova.

### 7.2. GitHub Repozitorijum
Izvorni kod projekta, istorija izmena (commit log) i prateće konfiguracione datoteke objavljeni su na zvaničnom GitHub nalogu:
- **Link do repozitorijuma**: [https://github.com/zivkovicn47/IT355-PZ.git](https://github.com/zivkovicn47/IT355-PZ.git)
