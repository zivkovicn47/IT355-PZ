# Automatikom Management System (Full-Stack)

Ovaj projekat predstavlja kompletnu realizaciju projektnog zadatka iz predmeta **IT355 - Veb sistemi 2** na Metropolitan univerzitetu (PZ02 faza). Aplikacija služi za evidenciju i upravljanje resursima u inženjerskim projektima industrijske automatizacije.

Sistem je u potpunosti refaktorisan u **Full-Stack** arhitekturu koja se sastoji od nezavisnog **Spring Boot REST API backenda** i **React klijentskog frontenda**.

---

## Opis Sistema
**Automatikom Management System** olakšava organizaciju industrijskih inženjerskih projekata kroz:
- Evidenciju hardverskih i softverskih komponenti (PLC-ovi, HMI paneli, mrežna oprema).
- Praćenje softverskih licenci (TIA Portal, WinCC OA, Studio 5000) i njihove valjanosti.
- Upravljanje inženjerskim timom (PLC programeri, SCADA/HMI inženjeri).
- Raspodelu i praćenje konkretnih zadataka po projektima i inženjerima.

---

## Arhitektura Aplikacije

### 1. Backend (Spring Boot REST API)
Backend je smešten u korenskom direktorijumu i organizovan je kroz višeslojnu arhitekturu:
- **Model sloj (JPA Entiteti)**: Podaci mapirani kao relacioni entiteti u **H2 in-memory bazi podataka** pomoću Jakarta Persistence (JPA) anotacija.
- **Repository sloj (Spring Data JPA)**: Perzistencija preko `JpaRepository` interfejsa.
- **Service sloj (`AutomatizacijaService`)**: Poslovna logika aplikacije zaštićena sa `@Transactional` transakcijama za bezbednost upisa i brisanja.
- **Controller sloj (REST API)**: Kontroleri anotirani sa `@RestController` koji primaju i vraćaju JSON podatke.

### 2. Sigurnost i Kontrola Pristupa (Spring Security & JWT)
Aplikacija koristi **stateless** sigurnosni režim na bazi JSON Web Tokena (JWT):
- **Autentifikacija**: Rute za registraciju i prijavu su pod `/api/auth`. Ruta `POST /api/auth/login` proverava kredencijale (lozinke heširane pomoću `BCryptPasswordEncoder`) i vraća JWT token sa ulogom korisnika u payload claims (`roles`).
- **Autorizacija na osnovu uloga (RBAC)**:
  - **Admin** (`admin` / `admin`) - poseduje ulogu `ROLE_ADMIN` i ima puna CRUD prava nad svim entitetima (GET, POST, DELETE).
  - **User** (`user` / `user`) - poseduje ulogu `ROLE_USER` i ima isključivo čitalački (Read-Only) pristup sistemu.

### 3. Testiranje
- **Jedinični testovi (Unit Tests)**: Napisani za servisni sloj pomoću JUnit 5 i Mockito alata (`AutomatizacijaServiceTest.java`).
- **Integracioni testovi (Integration Tests)**: Napisani za REST kontrolere pomoću Spring Boot Test i `MockMvc` alata (`ProjekatControllerTest.java`), uz simulaciju mock korisnika (`.with(user().roles())`) za bezbednu proveru API-ja.

### 4. Frontend (React & Vite)
Klijentski deo aplikacije se nalazi u [frontend/](file:///c:/Users/nikol/OneDrive/Dokumenti/_Fakultet/6. Semestar/IT355 - Veb sistemi 2/IT355-PZ/IT355-PZ-01/it355pz/frontend) folderu:
- **Rutiranje**: Implementirano preko `react-router-dom` uz upotrebu `ProtectedLayout` i `ProtectedRoute` kontrola za zaštitu privatnih stranica.
- **Axios klijent**: Konfigurisan u `api.js` sa automatskim presretanjem zahteva (Request Interceptor) koji dodaje `Authorization: Bearer <token>` u heder svakog API poziva.
- **Mobilna responzivnost**: Napisan je responzivni CSS sa klizećom fiokom (Drawer), "hamburger" menijem na dodir i horizontalno skrolujućim tabelama (`overflow-x: auto`) za udoban rad na telefonima.

---

## Model Klasa (5 Entiteta)
Sistem sadrži 5 ugrađenih relacionih tabela:
1. **Projekat**: `id`, `naziv`, `klijent`, `status`
2. **Komponenta**: `id`, `naziv`, `serijskiBroj`, `proizvodjac`, `status`
3. **Inzenjer**: `id`, `ime`, `prezime`, `email`, `uloga`
4. **Licenca**: `id`, `nazivSoftvera`, `kljucLicence`, `tipLicence`, `aktivna`
5. **Zadatak**: `id`, `opis`, `projekatId`, `inzenjerId`, `zavrsen`

---

## Uputstvo za Pokretanje

### Korak 1: Pokretanje Backenda (Port: 8080)
1. Otvorite korenski direktorijum projekta u terminalu.
2. Pokrenite aplikaciju komandom:
   ```bash
   .\mvnw spring-boot:run
   ```
3. H2 konzoli možete pristupiti na: [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (JDBC URL: `jdbc:h2:mem:automatikomdb`, Username: `sa`, Password: `password`).

### Korak 2: Pokretanje React Frontenda (Port: 5173)
1. Otvorite novi terminal i uđite u `frontend` folder:
   ```bash
   cd frontend
   ```
2. Instalirajte zavisnosti (ako već niste):
   ```bash
   npm install
   ```
3. Pokrenite razvojni server:
   ```bash
   npm run dev
   ```
4. Aplikacija će biti dostupna na linku: [http://localhost:5173/](http://localhost:5173/)
