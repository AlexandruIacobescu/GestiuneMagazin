# Gestiune Magazin

## Descriere
Aplicația "Gestiune Magazin" este o soluție software creată în JavaFX, pentru gestionarea operațiunilor unui magazin. Aceasta permite administrarea produselor, angajaților, conturilor și comenzilor printr-o interfață prietenoasă și intuitivă.

## Funcționalități
- **Conexiune la baza de date SQLite**: Aplicația utilizează o bază de date SQLite, integrată în structura proiectului, pentru stocarea și gestionarea datelor.
- **Tabele în baza de date**: Baza de date conține tabelele `Products`, `Employees`, `Accounts`, `Orders`, `OrderItems` pentru a organiza informațiile necesare.
- **Login Administrator**: Administratorii pot autentifica în aplicație pentru a adăuga sau șterge angajați din baza de date.
- **Înregistrare Angajați**: Odată adăugați, angajații se pot înregistra în aplicație folosind ID-ul unic de angajat pentru a accesa baza de date a produselor și pentru a efectua operațiuni asupra acesteia.
- **Generarea Facturilor**: Angajații pot genera facturi pentru comenzi, printre alte operațiuni disponibile în aplicație.

## Dependințe
- JDK 21

## Instalare
1. Clonează repository-ul pe sistemul tău local folosind `git clone [URL_REPOSITORY]`.
2. Deschide proiectul într-un IDE compatibil cu JavaFX (de exemplu, IntelliJ IDEA).
3. Asigură-te că JDK 21 este instalat și configurat corespunzător pe sistemul tău.
4. Rulează aplicația din IDE (`Application.java`).



## Configurare Bază de Date
Baza de date SQLite este preconfigurată și gata de utilizare. Fișierul bazei de date este: `inventory.db`.

## Utilizare
Pentru a utiliza aplicația, execută următorii pași:
1. Autentifică-te ca administrator pentru a gestiona angajații 
2. Username și parola pentru administrator sunt `admin`.
3. Înregistrează angajații folosind funcționalitatea de înregistrare.
4. Odată înregistrați, angajații pot accesa funcționalitățile aplicației.

## Contribuții
- Iacobescu Alexandru



## Contact
- [GitHub](https://github.com/AlexandruIacobescu)

## Licență
- MIT License