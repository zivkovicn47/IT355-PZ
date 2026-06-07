# Sistem za upravljanje inženjerskim projektima i automatizacijom (Automatikom Management System)

Ovaj projekat predstavlja prvu fazu (PZ01) projektnog zadatka iz predmeta **IT355 - Veb sistemi 2** na Metropolitan univerzitetu. Aplikacija služi za evidenciju i upravljanje resursima u inženjerskim projektima industrijske automatizacije.

## Opis Sistema
**Automatikom Management System** je veb aplikacija koja olakšava organizaciju inženjerskih projekata. Omogućava:
- Evidenciju hardverskih komponenti (PLC-ovi, HMI paneli, frekventni regulatori).
- Praćenje softverskih licenci (TIA Portal, WinCC Runtime, WinCC OA).
- Upravljanje inženjerskim timom (PLC programeri, SCADA inženjeri, projektanti).
- Dodelu i praćenje konkretnih zadataka po projektima i inženjerima.

## Arhitektura (PZ01)
Aplikacija je implementirana u **Spring Boot 4.0.6 (Java 21)** frejmworku prateći čist **MVC (Model-View-Controller)** arhitekturni šablon:
- **Model sloj (POJO)**: Predstavlja podatke u sistemu.
- **Repository sloj (InMemoryRepository)**: Simulira bazu podataka. Pošto u prvoj fazi projekta ne koristimo eksternu bazu, podaci se čuvaju in-memory u radnoj memoriji aplikacije kao Spring singleton bean (Application Scope), sa logikom za automatsko dodeljivanje jedinstvenih ID-jeva.
- **Service sloj (AutomatizacijaService)**: Sloj poslovne logike koji povezuje repozitorijume sa kontrolerima.
- **Controller sloj (Spring MVC Kontroleri)**: Prihvata zahteve i prosleđuje ih odgovarajućim Thymeleaf šablonima.
- **View sloj (Thymeleaf & CSS)**: Dinamički HTML šabloni stilizovani modernom tamnom "clean-tech" dashboard temom (čist CSS).

## Struktura klasa
Sistem je modelovan kroz sledećih 5 model klasa (POJO):
1. **Projekat**: `id`, `naziv`, `klijent`, `status`
2. **Komponenta**: `id`, `naziv`, `serijskiBroj`, `proizvodjac`, `status`
3. **Inzenjer**: `id`, `ime`, `prezime`, `email`, `uloga`
4. **Licenca**: `id`, `nazivSoftvera`, `kljucLicence`, `tipLicence`, `aktivna`
5. **Zadatak**: `id`, `opis`, `projekatId`, `inzenjerId`, `zavrsen`

## Uputstvo za pokretanje
Da biste pokrenuli aplikaciju lokalno, pratite sledeće korake:

1. Klonirajte repozitorijum.
2. Otvorite korenski direktorijum projekta.
3. Pokrenite server komandom:
   ```bash
   ./mvnw spring-boot:run
   ```
4. Aplikaciju možete otvoriti u pretraživaču na adresi:
   [http://localhost:8080](http://localhost:8080)
