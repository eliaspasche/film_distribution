# Film Distribution

Für die Ausleihe der Filme, Rechnungen der Kunden, Kundendaten, Kontingent der Filme und eine wöchentliche Kalkulation
aller Kosten, haben wir eine Webapplikation als Softwarelösung entwickelt.

Die Software beinhaltet ein Accountsystem, mit dem es möglich ist, neue Benutzer als Admin hinzuzufügen und somit nur
bestimmten Personen Zugriff auf die Anwendung zu erlauben.

Ein Benutzer kann dabei zwei Rollen zugeordnet werden (Admin und User).

#### Admin:

* Zugriff auf alle Seiten
* Account-Verwaltung

#### User:

* Zugriff auf alle Seiten, bis auf die Accountverwaltungsseite
* Keine Account-Verwaltung

Für die Logik der Film Ausleihe, besitzt jeder Film ein bestimmtes Kontingent an Film-Kopien.
Auf dieser Basis können anschließend Ausleihen erstellt werden.

Zu einer Ausleihe gehört jeweils ein Kunde, eine beliebige Anzahl an Film-Kopien und
ein Start- sowie ein End-Datum.

Die Berechnung ergibt sich anhand der angefangenen Wochen über den Ausleih-Zeitraum.

#### Beispiel:

```
Start-Datum: 15.09.2023 (Freitag)
End-Datum: 05.10.2023 (Donnerstag)
Preis/Woche: 5 €

=> 3 angefangene Wochen ausgehend von Freitag

Preis: 15 €
```

Des Weiteren können Rechnungen und Berichte mit bestimmten Filtern als PDF erstellt und ausgedruckt werden.

In folgenden werden die einzelnen Unterseiten genauer beschrieben.


## Running the Application

Short Installation Guide: Maven, NPM, Database Connection, Run Configurations

## Menu (Pages)

### Dashboard

Das Dashboard bietet allgemeine Informationen über die gesamte Webapplikation.
Angefangen mit der Anzahl von Filmen, Kunden, Ausleihen und Film-Kopien gibt es außerdem zwei Diagramme.

#### Balkendiagramm:

Zeigt die Top 5 Filme, auf Basis der aktuell ausgeliehenen Film-Kopien.

#### Donut-Chart:

Zeigt die Umsätze der aktuell ausgeliehenen Filme an.

___
### Distributions

Auf dieser Seite können die Film-Ausleihen verwaltet werden.
Hier können neue Ausleihen erstellt sowie bestehende Ausleihen geändert und entfernt werden.

Bei jedem Speichern wird geprüft, ob die ausgewählten Film-Kopien dem Alter des Kunden entsprechen
und ob die Film-Kopien noch verfügbar sind.

Außerdem kann zu jeder bestehenden Ausleihe eine Rechnung als PDF erstellt werden.
Die ausgeliehenen Filme und die entsprechenden Altersgruppen können ebenfalls für jeden Kunden eingesehen werden.

Für die Auswertung zu einem bestimmten Kunden, Film oder Stichtag kann ein entsprechender Bericht erstellt werden.
Die nötigen Filter können über der Tabellen Ansicht eingestellt und anschließend über "Create Report" erstellt werden.

#### Filter Optionen

* Kunde: Bericht über die Ausleihen eines bestimmten Kunden.
* Film: Bericht über die Ausleihe zu einen bestimmten Film.
* Stichtag: Bericht über die Kunden und Ausleihen zu einem bestimmten Stichtag.

Die Filter können auch in Kombination genutzt werden.
___
### Customers

Auf dieser Seite können Kunden verwaltet werden.
Es können neue Kunden erstellt und bestehende Kunden angepasst und entfernt werden.

___
### Films

Diese Seite dient zur Verwaltung der Filme. Hier können Filme erstellt und bei Bedarf angepasst und entfernt werden.
Bei jedem neuen Film kann eine Anzahl bestehender Kopien angegeben werden, damit diese anschließend automatisch erstellt
werden.

Jeder Film hat dabei einen festen Preis für jede angefangene Woche.
Außerdem hat jeder Film ein bestimmtes Mindestalter, um dieses bei der Ausleihe mit dem Alter des Kunden zu überprüfen.

___
### Film Copies

Zu jedem Film gibt es eine bestimmte Anzahl an Film-Kopien. Diese können auf dieser Seite verwaltet werden.
Es können zu einem bestimmten Film neue Film-Kopien erstellt werden, die von Kunden ausgeliehen werden können.

___
### Age Groups

Auf dieser Seite können die Altersgruppen für die Filme festgelegt werden.

___
### Account (Admin Only)

Diese Seite dürfen nur Benutzer mit der Admin Rolle betreten.
Hier können neue Benutzer für die Verwaltung der Webapplikation angelegt oder bestehende angepasst oder entfernt werden.

___
## SQL Scripts

Installation Scrips: [Installation Scripts](./doc/sql_installation.md)

Required Scripts at runtime: [SQL Scripts](./doc/sql.md)

## ER-Diagram

## Optional: Class Explanation (Database related)

Describe:

* Config
* Repository
* Service
* View



