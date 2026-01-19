package gui.rigaComando;

import java.util.List;
import java.io.IOException;
import jbook.util.Input;
import modello.GestioneListe;
import modello.ListaDiArticoli;
import modello.Articolo;
import modello.exception.GestioneListeException;
import modello.exception.ArticoloException;
import modello.exception.ListaDiArticoliException;

/**
 * La classe {@code InterfacciaRigaDiComando} gestisce l'interazione testuale con l'utente.
 * <p>
 * Fornisce un sistema di menu per gestire le liste, l'anagrafica delle categorie
 * e il registro globale degli articoli, garantendo la parità di funzioni con la GUI.
 *
 * @author Angie Albitres
 */
public class InterfacciaRigaDiComando {

    /**
     * Inizializza l'interfaccia testuale e avvia il menu principale.
     */
    public InterfacciaRigaDiComando() {
        menuPrincipale();
    }
    
    /**
     * Gestisce il ciclo principale dell'applicazione da riga di comando.
     * Smista le operazioni verso i sottopannelli o i metodi di gestione.
     */
    private void menuPrincipale() {
        boolean on = true;
        while(on) {
            try {
                visualizzaMenu();
                int scelta = Input.readInt("Scegli l'operazione: ");
                switch (scelta){
                    case 0 -> {
                        if (confermaUscita()) on = false;
                    }
                    case 1 -> creaLista();
                    case 2 -> visualizzaListe();
                    case 3 -> selezionaLista();
                    case 4 -> eliminaLista();
                    case 5 -> menuCategorie();
                    case 6 -> menuRegistroArticoli();
                    case 7 -> gestisciFile();
                    default -> System.out.println("\nScelta non valida...");
                }
            } catch (GestioneListeException | ListaDiArticoliException | ArticoloException e) {
                System.out.println("\nErrore nel modello: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("\nErrore di input: Inserisci un numero valido.");
            } catch (Exception e) {
                System.out.println("\nSi è verificato un errore imprevisto: " + e.getMessage());
            }
        }
        System.out.println("Chiusura interfaccia da riga di comando...");
    }
    
    /**
     * Mostra le opzioni disponibili nel menu principale.
     */
    private void visualizzaMenu() {
        System.out.println("\n===== GESTORE LISTE (MENU PRINCIPALE) =====");    
        System.out.println("0 - Esci / Torna al menu interfacce");
        System.out.println("1 - Crea una nuova lista");    
        System.out.println("2 - Visualizza tutte le liste");
        System.out.println("3 - Seleziona una lista per gestirla");    
        System.out.println("4 - Elimina una lista");
        System.out.println("5 - Gestisci Categorie Globali");
        System.out.println("6 - Gestisci Registro Articoli Globale");
        System.out.println("7 - Salvataggio / Caricamento dati");
        System.out.println("===========================================");
    }

    /**
     * Crea una nuova lista di articoli chiedendo il nome all'utente.
     * 
     * @throws ListaDiArticoliException Se il nome della lista non è valido[cite: 13].
     * @throws GestioneListeException Se la lista esiste già nel sistema[cite: 21].
     */
    private void creaLista() throws ListaDiArticoliException, GestioneListeException {
        String nome = Input.readString("Inserisci il nome della nuova lista: ");
        GestioneListe.inserisciLista(new ListaDiArticoli(nome));
        GestioneListe.setModificato(true);
        System.out.println("Lista '" + nome + "' creata con successo.");
    }

    /**
     * Stampa a video l'elenco di tutte le liste registrate nel sistema.
     */
    private void visualizzaListe() {
        List<ListaDiArticoli> liste = GestioneListe.getListeArticoli();
        if (liste.isEmpty()) {
            System.out.println("Nessuna lista presente nel sistema.");
        } else {
            System.out.println("Liste disponibili:");
            liste.forEach(l -> System.out.println("- " + l.getNome()));
        }
    }

    /**
     * Permette di selezionare una lista per accedere al menu di gestione interno.
     * 
     * @throws GestioneListeException Se la lista non viene trovata[cite: 21].
     */
    private void selezionaLista() throws GestioneListeException {
        visualizzaListe();
        String nome = Input.readString("Nome della lista da aprire: ");
        ListaDiArticoli lista = GestioneListe.matchLista(nome);
        menuLista(lista);
    }

    /**
     * Rimuove una lista dal sistema previo inserimento del nome.
     * 
     * @throws GestioneListeException Se la lista non esiste o il nome è vuoto[cite: 21].
     */
    private void eliminaLista() throws GestioneListeException {
        visualizzaListe();
        String nome = Input.readString("Nome della lista da eliminare: ");
        GestioneListe.cancellaLista(nome);
        GestioneListe.setModificato(true);
        System.out.println("Lista '" + nome + "' eliminata.");
    }

    /**
     * Sottemenu per la gestione delle categorie merceologiche globali.
     * 
     * @throws GestioneListeException In caso di errori di validazione, duplicati o cancellazione della categoria default[cite: 21].
     */
    private void menuCategorie() throws GestioneListeException {
        System.out.println("\n--- GESTIONE CATEGORIE ---");
        System.out.println("1 - Aggiungi Categoria\n2 - Elimina Categoria\n3 - Visualizza Categorie\n0 - Annulla");
        try {
            int scelta = Input.readInt("Scelta: ");
            switch (scelta) {
                case 1 -> {
                    String cat = Input.readString("Nome nuova categoria: ");
                    GestioneListe.inserisciCategoria(cat);
                    GestioneListe.setModificato(true);
                    System.out.println("Categoria aggiunta.");
                }
                case 2 -> {
                    String cat = Input.readString("Nome categoria da eliminare: ");
                    GestioneListe.cancellaCategoria(cat);
                    GestioneListe.setModificato(true);
                    System.out.println("Categoria rimossa.");
                }
                case 3 -> GestioneListe.getCategorie().forEach(System.out::println);
            }
        } catch (NumberFormatException e) {
            System.out.println("Errore: Inserisci un numero valido.");
        }
    }

    /**
     * Sottemenu per la gestione del registro globale degli articoli condivisi.
     * 
     * @throws ArticoloException Se i dati dell'articolo non sono validi[cite: 18].
     * @throws GestioneListeException Se l'articolo esiste già o non viene trovato[cite: 21].
     */
    private void menuRegistroArticoli() throws ArticoloException, GestioneListeException {
        System.out.println("\n--- REGISTRO GLOBALE ARTICOLI ---");
        System.out.println("1 - Aggiungi Articolo\n2 - Elimina Articolo\n3 - Modifica Articolo\n4 - Visualizza Catalogo\n0 - Annulla");
        try {
            int scelta = Input.readInt("Scelta: ");
            switch (scelta) {
                case 1 -> {
                    String nome = Input.readString("Nome: ");
                    String cat = Input.readString("Categoria: ");
                    double prezzo = Input.readDouble("Prezzo: ");
                    String nota = Input.readString("Nota: ");
                    GestioneListe.inserisciArticolo(new Articolo(nome, cat, prezzo, nota));
                    GestioneListe.setModificato(true);
                }
                case 2 -> {
                    visualizzaCatalogo();
                    String nome = Input.readString("Nome articolo da rimuovere globalmente: ");
                    Articolo trovato = trovaArticoloInCatalogo(nome);
                    if(trovato != null) {
                        GestioneListe.cancellaArticolo(trovato);
                        GestioneListe.setModificato(true);
                    } else System.out.println("Articolo non trovato.");
                }
                case 3 -> gestisciModificaGlobale();
                case 4 -> visualizzaCatalogo();
            }
        } catch (NumberFormatException e) {
            System.out.println("Errore: Inserisci un numero o prezzo valido.");
        }
    }

    /**
     * Mostra l'elenco di tutti gli articoli presenti nel catalogo globale.
     */
    private void visualizzaCatalogo() {
        System.out.println("\nCatalogo Globale Articoli:");
        GestioneListe.getArticoli().forEach(a -> 
            System.out.println("- " + a.getNome() + " [" + a.getCategoria() + "] € " + String.format("%.2f", a.getPrezzo())));
    }

    /**
     * Menu operativo interno per la gestione degli articoli di una lista specifica.
     * 
     * @param lista La lista su cui operare.
     */
    private void menuLista(ListaDiArticoli lista) {
        boolean on = true;
        while(on) {
            try {
                visualizzaMenuLista(lista);
                int scelta = Input.readInt("Operazione su '" + lista.getNome() + "': ");
                switch (scelta){
                    case 0 -> on = false;
                    case 1 -> aggiungiArticoloALista(lista);
                    case 2 -> visualizzaContenutoLista(lista);
                    case 3 -> cercaArticoloInLista(lista);
                    case 4 -> spostaNelCestino(lista);
                    case 5 -> gestioneCestino(lista);
                    case 6 -> System.out.println("Totale attuale lista: € " + String.format("%.2f", lista.calcoloPrezzoTotale()));
                    default -> System.out.println("Scelta non valida...");
                }
            } catch (ListaDiArticoliException | ArticoloException | GestioneListeException e) {
                System.out.println("Errore nella lista: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Errore di input numerico.");
            }
        }
    }

    /**
     * Mostra le opzioni disponibili per la gestione della lista singola.
     *
     * @param lista La lista corrente.
     */
    private void visualizzaMenuLista(ListaDiArticoli lista) {
        System.out.println("\n--- GESTIONE LISTA: " + lista.getNome() + " ---");
        System.out.println("0 - Torna al menu principale");
        System.out.println("1 - Aggiungi Articolo (Nuovo o dal Catalogo)");
        System.out.println("2 - Visualizza Articoli Attivi");
        System.out.println("3 - Cerca Articolo (prefisso)");
        System.out.println("4 - Rimuovi Articolo (cestino)");
        System.out.println("5 - Gestione Cestino");
        System.out.println("6 - Calcola Prezzo Totale");
    }

    /**
     * Permette l'inserimento di un articolo nella lista.
     * 
     * @throws ListaDiArticoliException Se l'articolo è già presente[cite: 13].
     * @throws ArticoloException Se i dati del nuovo articolo non sono validi[cite: 18].
     * @throws GestioneListeException Se si verificano errori nel registro globale[cite: 21].
     */
    private void aggiungiArticoloALista(ListaDiArticoli lista) throws ListaDiArticoliException, ArticoloException, GestioneListeException {
        System.out.println("1 - Crea nuovo articolo\n2 - Scegli dal catalogo esistente");
        int opz = Input.readInt("Scelta: ");
        if (opz == 1) {
            String nome = Input.readString("Nome: ");
            String cat = Input.readString("Categoria: ");
            double pr = Input.readDouble("Prezzo: ");
            String nota = Input.readString("Nota: ");
            Articolo a = new Articolo(nome, cat, pr, nota);
            lista.inserisciArticolo(a);
            try { GestioneListe.inserisciArticolo(a); } catch (GestioneListeException e) { /* Ignora se già in catalogo */ }
        } else {
            visualizzaCatalogo();
            String nome = Input.readString("Nome dell'articolo dal catalogo: ");
            Articolo a = trovaArticoloInCatalogo(nome);
            if (a != null) lista.inserisciArticolo(a);
            else System.out.println("Articolo non trovato nel catalogo.");
        }
        GestioneListe.setModificato(true);
    }

    /**
     * Visualizza gli articoli attivi presenti nella lista utilizzando l'iteratore[cite: 13].
     */
    private void visualizzaContenutoLista(ListaDiArticoli lista) {
        System.out.println("\nArticoli attivi in '" + lista.getNome() + "':");
        for(Articolo a : lista) {
            if(!lista.getArticoliCancellati().contains(a)) {
                System.out.println("- " + a.getNome() + " (" + a.getCategoria() + ") € " + String.format("%.2f", a.getPrezzo()));
            }
        }
    }

    /**
     * Ricerca articoli nella lista (attivi e cancellati) tramite prefisso[cite: 13].
     */
    private void cercaArticoloInLista(ListaDiArticoli lista) {
        String pref = Input.readString("Inserisci prefisso di ricerca: ");
        List<Articolo> ris = lista.ricercaArticolo(pref);
        if(ris.isEmpty()) System.out.println("Nessun articolo corrispondente.");
        else ris.forEach(a -> System.out.println("Trovato: " + a.getNome() + " [" + a.getCategoria() + "]"));
    }

    /**
     * Sposta un articolo selezionato nella lista dei cancellati[cite: 13].
     * 
     * @throws ListaDiArticoliException Se l'articolo non è presente negli attivi[cite: 13].
     */
    private void spostaNelCestino(ListaDiArticoli lista) throws ListaDiArticoliException {
        visualizzaContenutoLista(lista);
        String nome = Input.readString("Nome articolo da spostare nel cestino: ");
        for(Articolo a : lista) {
            if(a.getNome().equalsIgnoreCase(nome) && !lista.getArticoliCancellati().contains(a)) {
                lista.cancellaArticolo(a);
                GestioneListe.setModificato(true);
                System.out.println("Articolo rimosso.");
                return;
            }
        }
        throw new ListaDiArticoliException("Articolo non presente tra gli attivi.");
    }

    /**
     * Gestisce le operazioni di recupero o svuotamento del cestino della lista[cite: 13].
     * 
     * @throws ListaDiArticoliException Se l'articolo non è nei cancellati[cite: 13].
     */
    private void gestioneCestino(ListaDiArticoli lista) throws ListaDiArticoliException {
        System.out.println("\n--- CESTINO '" + lista.getNome() + "' ---");
        lista.getArticoliCancellati().forEach(a -> System.out.println("- " + a.getNome()));
        System.out.println("1 - Recupera Articolo\n2 - Svuota Cestino\n0 - Annulla");
        int scelta = Input.readInt("Scelta: ");
        if (scelta == 1) {
            String nome = Input.readString("Nome articolo da ripristinare: ");
            for(Articolo a : lista.getArticoliCancellati()) {
                if(a.getNome().equalsIgnoreCase(nome)) {
                    lista.recuperaArticolo(a);
                    GestioneListe.setModificato(true);
                    System.out.println("Articolo ripristinato.");
                    return;
                }
            }
            throw new ListaDiArticoliException("Articolo non trovato nel cestino.");
        } else if (scelta == 2) {
            lista.svuotaCancellati();
            GestioneListe.setModificato(true);
            System.out.println("Cestino svuotato.");
        }
    }

    /**
     * Gestisce il salvataggio o caricamento manuale tramite file.
     */
    private void gestisciFile() {
        System.out.println("1 - Salva Stato Sistema\n2 - Carica Stato Sistema");
        try {
            int scelta = Input.readInt("Scelta: ");
            if (scelta == 1) {
                GestioneListe.salvaSistema("dati_sistema.txt");
                System.out.println("Salvataggio completato.");
            } else if (scelta == 2) {
                GestioneListe.caricaSistema("dati_sistema.txt");
                System.out.println("Sistema ripristinato dal file.");
            }
        } catch (IOException e) {
            System.out.println("Errore di I/O: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Errore durante il caricamento: " + e.getMessage());
        }
    }

    /**
     * Procedura di conferma uscita: verifica modifiche pendenti e chiede conferma.
     * 
     * @return {@code true} se si conferma l'uscita, {@code false} altrimenti.
     */
    private boolean confermaUscita() {
        if (!GestioneListe.getModificato()) {
            return true;
        }

        System.out.println("\n[AVVISO] Sono presenti modifiche non salvate.");
        System.out.print("Vuoi salvare prima di uscire? (S=Sì, N=No, A=Annulla): ");
        String ris = Input.readString().trim().toUpperCase();

        switch (ris) {
            case "S" -> {
                try {
                    GestioneListe.salvaSistema("dati_sistema.txt");
                    System.out.println("Dati salvati. Uscita in corso...");
                    return true;
                } catch (IOException e) {
                    System.out.println("Errore salvataggio: " + e.getMessage());
                    return false;
                }
            }
            case "N" -> {
                return true;
            }
            default -> {
                System.out.println("Uscita annullata.");
                return false;
            }
        }
    }

    /**
     * Metodo ausiliario per la ricerca di un articolo nel registro globale per nome.
     */
    private Articolo trovaArticoloInCatalogo(String nome) {
        return GestioneListe.getArticoli().stream()
                .filter(a -> a.getNome().equalsIgnoreCase(nome))
                .findFirst().orElse(null);
    }

    /**
     * Permette la modifica dei campi di un articolo nel registro globale.
     *
     * @throws ArticoloException Se il nuovo prezzo è negativo[cite: 18].
     */
    private void gestisciModificaGlobale() throws ArticoloException {
        visualizzaCatalogo();
        String nome = Input.readString("Nome articolo da modificare: ");
        Articolo a = trovaArticoloInCatalogo(nome);
        if(a != null) {
            a.setCategoria(Input.readString("Nuova Categoria (attuale: " + a.getCategoria() + "): "));
            a.setPrezzo(Input.readDouble("Nuovo Prezzo (attuale: " + a.getPrezzo() + "): "));
            a.setNota(Input.readString("Nuova Nota: "));
            GestioneListe.setModificato(true);
            System.out.println("Modifica effettuata.");
        } else System.out.println("Articolo non trovato.");
    }
}