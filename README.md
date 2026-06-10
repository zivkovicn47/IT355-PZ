# Automatikom Management System (Full-Stack)

Ovaj projekat predstavlja kompletnu realizaciju projektnog zadatka iz predmeta **IT355 - Veb sistemi 2** na Metropolitan univerzitetu. Aplikacija služi za evidenciju i upravljanje resursima u inženjerskim projektima industrijske automatizacije.

Sistem je realizovan kao **Full-Stack** aplikacija koja se sastoji od nezavisnog **Spring Boot REST API backenda** i **React klijentskog frontenda**.

---

## Opis Sistema

**Automatikom Management System** olakšava organizaciju industrijskih inženjerskih projekata kroz:
- Evidenciju hardverskih i softverskih komponenti (PLC-ovi, HMI paneli, mrežna oprema).
- Praćenje softverskih licenci (TIA Portal, WinCC OA, Studio 5000) i njihove valjanosti.
- Upravljanje inženjerskim timom (PLC programeri, SCADA/HMI inženjeri).
- Raspodelu i praćenje konkretnih zadataka po projektima i inženjerima.
- Eksport podataka u Excel format putem Apache POI biblioteke.
- Prikaz trenutnih vremenskih uslova za odabrani grad putem OpenWeatherMap API-ja.

---

## Tehnički Stack

| Sloj | Tehnologije |
|------|-------------|
| Backend | Java 21, Spring Boot 4.0.6, Spring Security, Spring Data JPA |
| Baza podataka | H2 (in-memory) |
| Autentifikacija | JWT (jjwt 0.11.5), BCrypt |
| Excel export | Apache POI |
| Vremenska prognoza | OpenWeatherMap REST API |
| Frontend | React 19, Vite 8, React Router 7, Axios |

---

## Arhitektura Aplikacije

### 1. Backend (Spring Boot REST API)
Backend je organizovan kroz višeslojnu arhitekturu:
- **Model sloj (JPA Entiteti)**: Podaci mapirani kao relacioni entiteti u H2 in-memory bazi podataka.
- **Repository sloj (Spring Data JPA)**: Perzistencija preko `JpaRepository` interfejsa.
- **Service sloj (`AutomatizacijaService`)**: Poslovna logika zaštićena `@Transactional` anotacijama.
- **Controller sloj (REST API)**: `@RestController` kontroleri koji primaju i vraćaju JSON podatke.

### 2. Sigurnost i Kontrola Pristupa (Spring Security & JWT)
Aplikacija koristi **stateless** sigurnosni režim na bazi JSON Web Tokena (JWT):
- **Autentifikacija**: `POST /api/auth/login` proverava kredencijale i vraća JWT token.
- **Autorizacija na osnovu uloga (RBAC)**:
  - **Admin** (`admin` / `admin`) — puna CRUD prava nad svim entitetima.
  - **User** (`user` / `user`) — isključivo čitalački (Read-Only) pristup.

### 3. Excel Export (Apache POI)
Ruta `GET /api/projekti/export-excel` generiše `.xlsx` fajl sa listom svih projekata i vraća ga kao preuzimanje direktno iz browsera.

### 4. Vremenska Prognoza (OpenWeatherMap)
Ruta `GET /api/weather?city={grad}` vraća trenutnu temperaturu i opis vremenskih uslova za zadati grad, prikazano kao widget u sidebaru aplikacije.

### 5. Testiranje
- **Jedinični testovi**: Napisani za servisni sloj pomoću JUnit 5 i Mockito (`AutomatizacijaServiceTest.java`).
- **Integracioni testovi**: Napisani za REST kontrolere pomoću Spring Boot Test i MockMvc (`ProjekatControllerTest.java`).

### 6. Frontend (React & Vite)
Klijentski deo aplikacije se nalazi u [frontend/](frontend/) folderu:
- **Rutiranje**: Implementirano preko `react-router-dom` uz `ProtectedLayout` za zaštitu privatnih stranica.
- **Axios klijent**: Konfigurisan u `api.js` sa automatskim presretanjem zahteva koje dodaje `Authorization: Bearer <token>` header.
- **Mobilna responzivnost**: Responzivni CSS sa klizećom fiokom (Drawer), hamburger menijem i horizontalno skrolujućim tabelama.

---

## Model Klasa (5 Entiteta)

| Entitet | Polja |
|---------|-------|
| **Projekat** | `id`, `naziv`, `klijent`, `status` |
| **Komponenta** | `id`, `naziv`, `serijskiBroj`, `proizvodjac`, `status` |
| **Inzenjer** | `id`, `ime`, `prezime`, `email`, `uloga` |
| **Licenca** | `id`, `nazivSoftvera`, `kljucLicence`, `tipLicence`, `aktivna` |
| **Zadatak** | `id`, `opis`, `projekatId`, `inzenjerId`, `zavrsen` |

---

## Uputstvo za Pokretanje

### Korak 1: Konfiguracija API ključa za vremensku prognozu

U fajlu `src/main/resources/application.properties` unesite vaš OpenWeatherMap API ključ:
```properties
weather.api.key=VAŠ_API_KLJUČ
```
Besplatan API ključ možete dobiti na [openweathermap.org](https://openweathermap.org/api).

### Korak 2: Pokretanje Backenda (Port: 8080)

```bash
.\mvnw spring-boot:run
```

H2 konzoli možete pristupiti na [http://localhost:8080/h2-console](http://localhost:8080/h2-console):
- JDBC URL: `jdbc:h2:mem:automatikomdb`
- Username: `sa`
- Password: `password`

### Korak 3: Pokretanje React Frontenda (Port: 5173)

```bash
cd frontend
npm install
npm run dev
```

Aplikacija će biti dostupna na [http://localhost:5173](http://localhost:5173).

---

## Kredencijali za Prijavu

| Korisnik | Lozinka | Uloga |
|----------|---------|-------|
| `admin` | `admin` | Admin — pun pristup (CRUD) |
| `user` | `user` | User — samo pregled |
