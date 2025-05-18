📚 Bookemp – Otthoni könyvtárrendező mobilalkalmazás

🔹 Bookemp egy otthoni könyvrendszerező mobilalkalmazás, amely lehetővé teszi a felhasználók számára saját könyvtáraik létrehozását, a könyvek adatainak felvételét és listázását, valamint exportálását CSV/Excel formátumban. A projekt két mérföldkőből áll, a fotós beolvasás funkció később, opcionálisan kerül integrálásra.
________________________________________
1. Mérföldkő: Alapfunkciók, Regisztráció, Főképernyő
Funkciók:
•	Bejelentkezés/Regisztráció: email + jelszó alapon.
•	Könyvtárak kezelése: a felhasználó új könyvtárakat hozhat létre (pl. "Hálószoba", "Nappali").
•	Látványos UI: letisztult megjelenés + 1 animáció (pl. gombreakció vagy átmenet).
Technológia:
•	Android Studio
•	Kotlin
•	Jetpack Compose
•	Room Database (SQLite felett)
________________________________________
2. Mérföldkő: Adatkezelés, Exportálás, Címkézés
Bővített funkciók:
•	Könyvhozzáadás: egy adott könyvtáron belül kézzel vihetők fel könyvadatok.
•	Adatok: szerző, cím, kiadás éve, kiadó, beszerzési ár, azonosító, cimkék.
•	Lista nézet: RecyclerView-al listázva a könyvtáron belüli könyvek.
•	Címkék kezelése: minden könyvhöz szabadon hozzáadható címkék (pl. sci-fi, romantikus).
•	Exportálás: Excel vagy CSV formátumban, képek nélkül.
•	További animációk: pl. könyvtár nyitás, lista megjelenés, sikeres mentés visszajelzés.
•	Jelszó titkosítás: felhasználói adatok biztonságos kezelése.
Adattárolás:
•	Room + SQLite helyi adatbázison belül
•	(Opcionálisan) Firebase Authentication bejelentkezéshez
________________________________________
További fejlesztési lehetőségek (nem kötelező):
•	Fotó alapú szövegfelismerés (OCR)
•	Könyvborító kép mentése
•	Keresés online piactérben (pl. API-n keresztül az árra)
________________________________________
Git struktúra javaslat:
•	main branch: stabil verzió
•	dev branch: aktív fejlesztés
•	feature/xyz: funkciónkkénti fejlesztési ágak
________________________________________
Leadandók az 1. mérföldkőre:
•	Git repo link (pl. GitHub)
•	Projektleírás (ez a dokumentum)
•	APK file vagy futtatható verzió
•	Dokumentáció a működésről + screenshotok
________________________________________
Megjegyzés:
A Bookemp alkalmazás elsődleges célja a felhasználó által birtokolt könyvek rendezett, átlátható, témák szerint csoportosítható nyilvántartása. Az alkalmazás az alapfunkciókon túl jövőbeli OCR-integrációval és online árlekérdezéssel is bővíthető.
