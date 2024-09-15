# Documentație API - StoreManagementTool

StoreManagementTool este un API RESTful care permite gestionarea produselor și a comenzilor într-un magazin. Acest API oferă funcționalități atât pentru utilizatorii obișnuiți, cât și pentru administratori (ADMIN).

## Autentificare și Autorizare

Pentru a accesa majoritatea endpoint-urilor, este necesară autentificarea și autorizarea utilizatorului prin intermediul unui token de acces. Singurele excepții sunt endpoint-urile de înregistrare și autentificare.

- **POST /store/auth/register**: Înregistrează un nou utilizator.
- **POST /store/auth/login**: Autentifică un utilizator existent și returnează un token de acces.

**Notă:** Token-ul primit trebuie atașat în header-ul cererilor ulterioare pentru autorizare. Cererile fără token vor primi răspunsul **403 FORBIDDEN**.

## Gestionarea Produselor (numai pentru ADMIN)

Următoarele operațiuni pot fi efectuate doar de utilizatorii cu rol de ADMIN:

- **POST /store/products**: Adaugă un produs nou. Datele produsului trebuie incluse în corpul cererii.
- **PUT /store/products**: Editează un produs existent. Datele actualizate ale produsului trebuie incluse în corpul cererii.
- **PATCH /store/products/changePrice/{id}**: Modifică prețul produsului cu ID-ul specificat.
- **PATCH /store/products/changeQuantity/{id}**: Modifică cantitatea produsului cu ID-ul specificat.
- **DELETE /store/products/{id}**: Șterge produsul cu ID-ul specificat.

## Vizualizarea și Căutarea Produselor

- **GET /store/products?name=*&description=***: Returnează o listă cu informații despre toate produsele. Pot fi utilizate filtre opționale precum numele sau descrierea produsului.

## Gestionarea Coșului de Cumpărături

- **PATCH /store/products/addToCart/{id}**: Adaugă produsul cu ID-ul specificat în coșul utilizatorului curent.
- **GET /store/cart**: Returnează o listă cu produsele din coș și totalul acestora.
- **PATCH /store/cart/purchase**: Finalizează comanda utilizatorului curent.
- **DELETE /store/cart/{id}**: Șterge produsul cu ID-ul specificat din coș.

## Arhitectură și Funcționalități

- **Autorizare și Control Acces**: La fiecare cerere, se extrag din token utilizatorul și drepturile acestuia. În Controller, se verifică dacă utilizatorul are dreptul de a efectua acțiunea solicitată înainte de a apela metodele din Service.
- **Servicii (Services)**: Implementarea logicii de business. Serviciile utilizează repository-urile pentru a accesa datele din baza de date.
- **Repository-uri**: Interfața cu baza de date, gestionând operațiile CRUD pentru entități.
- **Entități**: Modelele de date care sunt salvate în baza de date.

## Gestionarea Erorilor

- În cazul în care există erori în datele de intrare, sunt aruncate excepții sugestive.

## Tehnologii Utilizate
- **Autentificare și Autorizare**: Implementate cu **Spring Security**.
- **Logging**: Implementat cu **Slf4j**.
- **Testare Unitară**: Realizată cu **JUnit 5** și **Mockito**.

---

