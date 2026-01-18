package gui.grafica.controllo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import gui.ListaGui;
import gui.grafica.vista.DialogoArticolo;
import gui.grafica.vista.PannelloArticoliGlobali;
import gui.grafica.vista.PannelloCategorie;
import gui.grafica.vista.PannelloListe;
import modello.Articolo;
import modello.GestioneListe;
import modello.ListaDiArticoli;
import modello.exception.GestioneListeException;

/**
 * La classe {@code ControlloGestore} funge da controller principale per la gestione 
 * delle operazioni globali del sistema
 * <p>
 * Implementa l'interfaccia {@link ActionListener} per gestire gli eventi generati 
 * dall'interfaccia grafica relativi a:
 * <ul>
 *   <li>Gestione delle liste (creazione, eliminazione, apertura)</li>
 *   <li>Anagrafica delle categorie (aggiunta, rimozione)</li>
 *   <li>Registro globale degli articoli (inserimento, cancellazione)</li>
 * </ul>
 * * @author Angie Albitres
 */
public class ControlloGestore implements ActionListener {

	private PannelloCategorie vistaCategorie;
	private PannelloArticoliGlobali vistaArticoli;
	private PannelloListe vistaListe;

	/**
	 * Collega la vista delle liste al controller
	 * 
	 * @param vista Il pannello {@link PannelloListe} da gestire
	 */
	public void setVistaListe(PannelloListe vista) {
        this.vistaListe = vista;
    }
	
	/**
	 * Collega la vista delle categorie al controller
	 * 
	 * @param vista Il pannello {@link PannelloCategorie} da gestire
	 */
	public void setVistaCategorie(PannelloCategorie vista) {
		this.vistaCategorie = vista;
	}
	
	/**
	 * Collega la vista degli articoli globali al controller
	 * 
	 * @param vista Il pannello {@link PannelloArticoliGlobali} da gestire
	 */
	public void setVistaArticoli(PannelloArticoliGlobali vista) {
        this.vistaArticoli = vista;
    }

	/**
	 * Gestisce i click sui bottoni dei pannelli globali
	 * Smista le richieste ai metodi specifici in base all'etichetta del bottone premuto
	 * 
	 * @param e L'evento di azione generato.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = ((JButton) e.getSource()).getText();

        switch (comando) {
            case "Aggiungi Categoria" -> gestisciAggiungiCategoria();
            case "Elimina Categoria" -> gestisciEliminaCategoria();
            
            case "Aggiungi Articolo" -> gestisciAggiungiArticoloGlobale();
            case "Elimina Articolo" -> gestisciEliminaArticoloGlobale();
        
            case "Nuova Lista" -> gestisciNuovaLista();
			case "Elimina Lista" -> gestisciEliminaLista();
			case "Apri Selezionata" -> gestisciApriLista();
        }
	}
	
	// LISTE
	/**
	 * Gestisce la creazione di una nuova lista di articoli.
	 * Richiede il nome tramite un dialogo di input e aggiorna la vista delle liste.
	 */
	private void gestisciNuovaLista() {
		String nome = JOptionPane.showInputDialog(null, "Inserisci il nome della nuova lista:");
		if (nome != null && !nome.isBlank()) {
			try {
				GestioneListe.inserisciLista(new ListaDiArticoli(nome));
				vistaListe.aggiornaDati();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Gestisce l'eliminazione della lista attualmente selezionata nel pannello liste.
	 * Prevede una conferma da parte dell'utente prima dell'eliminazione definitiva.
	 */
	private void gestisciEliminaLista() {
		String selezionata = vistaListe.getListaSelezionata();
		if (selezionata == null) {
			JOptionPane.showMessageDialog(null, "Seleziona una lista da eliminare");
			return;
		}

		int confirm = JOptionPane.showConfirmDialog(null, "Eliminare definitivamente la lista '" + selezionata + "'?", "Conferma", JOptionPane.YES_NO_OPTION);
		if (confirm == JOptionPane.YES_OPTION) {
			try {
				GestioneListe.cancellaLista(selezionata);
				vistaListe.aggiornaDati();
			} catch (GestioneListeException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	/**
	 * Apre l'interfaccia specifica per la lista selezionata.
	 * Crea una nuova istanza di {@link ListaGui} per la gestione degli articoli interni.
	 */
	private void gestisciApriLista() {
		String selezionata = vistaListe.getListaSelezionata();
		if (selezionata == null) {
			JOptionPane.showMessageDialog(null, "Seleziona una lista da aprire");
			return;
		}
		try {
			ListaDiArticoli lista = GestioneListe.matchLista(selezionata);
			new ListaGui(lista); 
		} catch (GestioneListeException ex) {
			JOptionPane.showMessageDialog(null, ex.getMessage());
		}
	}

	// CATEGORIA
	/**
	 * Gestisce l'aggiunta di una nuova categoria nel sistema.
	 * Aggiorna la vista delle categorie in caso di successo.
	 */
	private void gestisciAggiungiCategoria() {
        String nome = JOptionPane.showInputDialog(null, "Inserisci il nome della nuova categoria:");
        if (nome != null && !nome.isBlank()) {
            try {
                GestioneListe.inserisciCategoria(nome);
                vistaCategorie.aggiornaDati();
            } catch (GestioneListeException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

	/**
	 * Gestisce l'eliminazione della categoria selezionata.
	 * Impedisce l'eliminazione se non è selezionata alcuna categoria o in caso di vincoli del modello.
	 */
    private void gestisciEliminaCategoria() {
        String selezionata = vistaCategorie.getCategoriaSelezionata();
        if (selezionata == null) {
            JOptionPane.showMessageDialog(null, "Seleziona una categoria da eliminare");
            return;
        }

        try {
            GestioneListe.cancellaCategoria(selezionata);
            vistaCategorie.aggiornaDati();
        } catch (GestioneListeException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // ARTICOLI
    /**
     * Gestisce l'inserimento di un nuovo articolo nel registro globale del sistema.
     * Utilizza {@link DialogoArticolo} per raccogliere i dati dell'utente.
     */
    private void gestisciAggiungiArticoloGlobale() {
        String[] inputs = new DialogoArticolo().getInputs("Nuovo Articolo nel Registro");

        if (inputs != null) {
            try {
                String nome = inputs[0];
                String categoria = inputs[1];
                double prezzo = Double.parseDouble(inputs[2]);
                String nota = inputs[3];

                Articolo nuovo = new Articolo(nome, categoria, prezzo, nota);
                GestioneListe.inserisciArticolo(nuovo);
                
                vistaArticoli.aggiornaDati();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Il prezzo deve essere un numero valido", "Errore Input", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Gestisce la rimozione di un articolo dal registro globale.
     * L'articolo viene eliminato dal sistema centrale ma rimane nelle liste dove è già presente.
     */
    private void gestisciEliminaArticoloGlobale() {
        Articolo sel = vistaArticoli.getArticoloSelezionato();
        if (sel == null) {
            JOptionPane.showMessageDialog(null, "Seleziona un articolo da rimuovere dal sistema");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, 
            "Eliminare '" + sel.getNome() + "' dal registro globale?\nAttenzione: non verrà rimosso dalle liste esistenti", 
            "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                GestioneListe.cancellaArticolo(sel);
                vistaArticoli.aggiornaDati();
            } catch (GestioneListeException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}