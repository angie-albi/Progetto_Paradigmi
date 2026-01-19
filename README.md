<div align="center">
  <h1>🛒 Gestore Liste di Articoli</h1>
  <p>
    Un'applicazione Java completa per la gestione centralizzata di liste della spesa e inventari con doppia interfaccia (GUI e CLI).
    <br />
    <br />
    <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" alt="Java Version">
    <img src="https://img.shields.io/badge/GUI-Swing-red?style=for-the-badge&logo=java" alt="Swing">
    <img src="https://img.shields.io/badge/Architecture-MVC-blueviolet?style=for-the-badge" alt="MVC Pattern">
    <img src="https://img.shields.io/badge/Test-JUnit_5-25A162?style=for-the-badge&logo=junit5" alt="JUnit">
  </p>
</div>

---

## 🧐 Di cosa si tratta?

Questo progetto permette di gestire molteplici liste di articoli (es. "Spesa Casa", "Ufficio") che condividono un **unico registro globale** di prodotti e categorie. Qualsiasi modifica apportata a un articolo nel catalogo centrale (prezzo, categoria o note) viene aggiornata istantaneamente in tutte le liste in cui l'articolo è presente.

Il software è progettato seguendo il pattern architetturale **MVC (Model-View-Controller)**, garantendo una netta separazione tra la logica del dominio e le interfacce utente sviluppate in Swing e per riga di comando.

---

## ✨ Funzionalità Principali

### 📂 Gestione Liste (Modello Globale)
* **Multi-Lista:** Crea, visualizza ed elimina diverse liste indipendenti identificate da nomi univoci.
* **Registro Globale:** Gestione centralizzata di articoli e categorie merceologiche condivise tra tutte le liste.
* **Persistenza:** Salvataggio e caricamento automatico dello stato del sistema su file di testo (`dati_sistema.txt`).

### 👤 Gestione Articoli e Categorie
* **Sistema di Cestino:** Gli articoli rimossi da una lista vengono spostati in un'area "cancellati" specifica per quella lista, permettendone il ripristino o l'eliminazione definitiva.
* **Ricerca Avanzata:** Ricerca per prefisso che opera simultaneamente tra articoli attivi e cestino.
* **Validazione:** Controllo rigoroso sui nomi tramite espressioni regolari e prezzi che devono essere non negativi.
* **Calcolo Totale:** Calcolo in tempo reale del valore economico complessivo degli articoli attivi in ogni lista.

---

## 🏗️ Struttura del Progetto

L'organizzazione dei file segue rigorosamente la suddivisione tra logica (Model), interfaccia (View) e coordinamento (Controller):

```text
src/
├── main/
│   └── Main.java                      # Punto di ingresso dell'applicazione
├── modello/                           # Logica di business
│   ├── Articolo.java
│   ├── ListaDiArticoli.java
│   ├── GestioneListe.java
│   ├── exception/                     # Eccezioni personalizzate
│   │   ├── ArticoloException.java
│   │   ├── ListaDiArticoliException.java
│   │   └── GestioneListeException.java
│   └── test/                          # Test unitari JUnit 5
│       ├── ArticoloTest.java
│       ├── ListaDiArticoliTest.java
│       └── GestioneListeTest.java
├── gui/                               # Interfacce utente e controllori
│   ├── GestoreGui.java
│   ├── ListaGui.java
│   ├── rigaComando/
│   │   └── InterfacciaRigaDiComando.java
│   └── grafica/
│       ├── controllo/                 # Controller per la GUI
│       │   ├── ControlloGestore.java
│       │   ├── ControlloLista.java
│       │   └── ControlloCestino.java
│       └── vista/                     # Componenti grafiche (View)
│           ├── PannelloListe.java
│           ├── PannelloCategorie.java
│           ├── PannelloArticoliGlobali.java
│           ├── OpsListaPanel.java
│           ├── ListaPanel.java
│           ├── DialogoArticolo.java
│           ├── ContentListaPanel.java
│           └── CestinoDialog.java
└── jbook/
    └── util/
        └── Input.java                 # Utility per l'input da tastiera
```

---

## 🧪 Testing e Qualità

La stabilità del progetto è garantita da una suite di test unitari sviluppata con **JUnit 5**.
I test coprono:

* ✅ **Articolo:** Validazione dei nomi tramite Regex, gestione prezzi negativi e logica di uguaglianza basata su nome e categoria.
* ✅ **Lista:** Gestione corretta del cestino (inserimento/recupero), ripristino articoli e calcolo del totale.
* ✅ **Gestore:** Verifica dell'integrità dei registri globali e della corretta associazione tra articoli e categorie condivise.

---

## ⚙️ Installazione e Setup

Segui questi passaggi per configurare il progetto in locale:

1. **Clona la repository:**
   ```bash
   git clone [https://github.com/angie-albi/progetto_paradigmi.git](https://github.com/angie-albi/progetto_paradigmi.git)
   ```
2. **Entra nella cartella:**
   ```bash
   cd progetto_paradigmi
   ```
3. **Compilazione:**
   ```bash
   mkdir -p bin
   javac -d bin -sourcepath src src/main/Main.java
   ```
4. **Esecuzione:**
   ```bash
   java -cp bin main.Main
   ```

---

### 👤 Autore
Sviluppato da **Angie Albitres**
