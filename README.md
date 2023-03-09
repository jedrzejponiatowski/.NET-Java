# .NET-Java
Zajęcia PWr


Aplikacja do zarządzania pożyczonymi pieniądzmi. C# + WPF + Entity Framework Core

1. Opis:
  Aplikacja desktopowa do zarządzania i przechowywania danych o zawartych umowach o podłożu pieniężnym:)
  Wspomaga nadzorowanie bilansu pieniędzy pożyczonych jak i zapożyczonych(?)


2. Funkcje:
  1) Dodawanie kolejnych pozycji - pieniędzy pożyczonych i zapożyczonych
  2) Przechowywanie aktualnego bilansu stanu konta - ile na plusie, ile na minusie
  3) Możliwość symulacji zapłaty (unieważnienie zapożyczenia)
  4) Powiadomienia mailowe o nadchodzących terminach zapłaty
  5) Dodawanie i usuwanie osób do listy "kontaktów"
  6) * Pobieranie aktualnego czasu w momencie utworzenia pozycji
  7) Wpisanie ilości dni na spłacenie i automatyczne wyliczenie daty końca terminu
 
 
 
 3. Wymagania bazy danych

Tabela osoba:
  ID, imię, nazwisko, adres e-mail, nr telefonu
  
Tabela pożyczka:
  ID, ID_kto_pozyczyl, ID_komu_pozyczyl, ile pieniedzy, data pożyczenia, data wygaśniecia, (kategoria)
  
  
  
  
  


