# Sistem za upravljanje inženjerskim projektima i automatizacijom (Automatikom Management System)

Ovaj projekat predstavlja prvu i drugu fazu (PZ01 & PZ02) projektnog zadatka iz predmeta **IT355 - Veb sistemi 2** na Metropolitan univerzitetu. Aplikacija služi za evidenciju i upravljanje resursima u inženjerskim projektima industrijske automatizacije.

## Opis Sistema
**Automatikom Management System** je veb aplikacija koja olakšava organizaciju inženjerskih projekata. Omogućava:
- Evidenciju hardverskih komponenti (PLC-ovi, HMI paneli, frekventni regulatori).
- Praćenje softverskih licenci (TIA Portal, WinCC Runtime, WinCC OA).
- Upravljanje inženjerskim timom (PLC programeri, SCADA inženjeri, projektanti).
- Dodelu i praćenje konkretnih zadataka po projektima i inženjerima.

## Arhitektura (PZ01 & PZ02)
Aplikacija je implementirana u **Spring Boot 4.0.6 (Java 21)** frejmworku prateći čist **MVC (Model-View-Controller)** arhitekturni šablon:
- **Model sloj (JPA Entiteti)**: Predstavlja podatke u sistemu. Klase su mapirane kao relacioni entiteti baze podataka pomoću Jakarta Persistence (JPA) anotacija.
- **Repository sloj (Spring Data JPA)**: Upravljanje podacima se obavlja preko JpaRepository interfejsa koji komuniciraju sa **H2 in-memory relacionom bazom podataka**. Šema baze se automatski kreira i ažurira na osnovu JPA modela.
- **Service sloj (AutomatizacijaService)**: Sloj poslovne logike koji povezuje repozitorijume sa kontrolerima.
- **Controller sloj (Spring MVC Kontroleri)**: Prihvata zahteve i prosleđuje ih odgovarajućim Thymeleaf šablonima.
- **View sloj (Thymeleaf & CSS)**: Dinamički HTML šabloni stilizovani modernom tamnom "clean-tech" dashboard temom (čist CSS). U drugoj fazi, šabloni su dodatno obogaćeni Thymeleaf Security dijalektom radi uloge-baziranog skrivanja opcija.

## Sigurnost i Kontrola Pristupa (Spring Security)
Sistem koristi **Spring Security** za kontrolu pristupa i zaštitu resursa. Autentifikacija je zasnovana na dva in-memory korisnika sa heširanim lozinkama (BCrypt):
- **Admin** (Kredencijali: `admin` / `admin`)
  - Poseduje ulogu `ADMIN`.
  - Ima puna prava nad sistemom (pregled, dodavanje, izmena i brisanje svih entiteta).
- **User** (Kredencijali: `user` / `user`)
  - Poseduje ulogu `USER`.
  - Ima isključivo "read-only" pristup kontrolnoj tabli i listama entiteta. Svi akcioni dugmići za dodavanje, izmenu i brisanje su skriveni na interfejsu (pomoću Thymeleaf Security dijalekta), a direktan pristup URL adresama za modifikaciju je zaštićen i vraća HTTP 403 Forbidden.

## Struktura klasa
Sistem je modelovan kroz sledećih 5 model klasa (JPA Entiteti):
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
5. Nakon pokretanja aplikacije, sistem će zahtevati prijavu. Prijavite se koristeći kredencijale za **Admin** ili **User** naloge navedene iznad.
6. H2 konzoli za pregled baze podataka u realnom vremenu možete pristupiti na adresi:
   [http://localhost:8080/h2-console](http://localhost:8080/h2-console) (Kredencijali za pristup: JDBC URL: `jdbc:h2:mem:automatikomdb`, Username: `sa`, Password: `password`).
