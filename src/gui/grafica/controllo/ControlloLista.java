package gui.grafica.controllo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import gui.grafica.vista.CestinoDialog;
import gui.grafica.vista.ContentListaPanel;
import gui.grafica.vista.DialogoArticolo;
import gui.grafica.vista.OpsListaPanel;
import gui.grafica.vista.PannelloListe;
import modello.Articolo;
import modello.GestioneListe;
import modello.ListaDiArticoli;
import modello.exception.ListaDiArticoliException;

/**
 * La classe {@code ControlloLista} funge da controller specifico per la gestione 
 * di una singola istanza di {@link ListaDiArticoli}.
 * <p>
 * Implementa l'interfaccia {@link ActionListener} per reagire alle interazioni dell'utente 
 * (pressione di bottoni o invio di testo). Il suo compito principale è:
 * <ul>
 *    <li>Manipolare il modello locale (la lista selezionata).</li>
 *    <li>Sincronizzare le aggiunte con il catalogo globale {@link GestioneListe}.</li>
 *    <li>Aggiornare dinamicamente le componenti della vista dopo ogni operazione.</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
public class ControlloLista implements ActionListener {
	/** Vista che mostra gli articoli della lista (Tabella o Lista) */
	private ContentListaPanel contenutoLista;
	
	/** Modello dei dati della lista specifica gestita */
	private ListaDiArticoli model;
	
	/** Pannello contenitore principale delle liste per aggiornamenti di alto livello */
	private PannelloListe vistaPrincipale;
	
	/** Riferimento al controller globale per la sincronizzazione tra schede */
	private ControlloGestore controllerGlobale;
	
	/** Pannello dei controlli (Bottoni di ricerca, aggiunta, ecc.) */
	private OpsListaPanel vistaOperazioni;

	/**
	 * Crea un'istanza di {@code ControlloLista}.
	 * 
	 * @param contenutoLista La vista che visualizza i dati del modello.
	 * @param model Il modello {@link ListaDiArticoli} da manipolare.
	 * @param vistaPrincipale Il contenitore UI di riferimento.
	 * @param controllerGlobale Il coordinatore centrale delle funzionalità GUI.
	 */
	public ControlloLista(ContentListaPanel contenutoLista, ListaDiArticoli model, 
            PannelloListe vistaPrincipale, ControlloGestore controllerGlobale) {
		this.contenutoLista = contenutoLista;
		this.model = model;
		this.vistaPrincipale = vistaPrincipale;
		this.controllerGlobale = controllerGlobale; 
	}
	
	/**
	 * Associa la vista dei controlli operativi al controller.
	 * Necessario per gestire i campi di ricerca e i pulsanti di reset.
	 * 
	 * @param vistaOperazioni Il pannello {@link OpsListaPanel} da collegare.
	 */
	public void setVistaOperazioni(OpsListaPanel vistaOperazioni) {
        this.vistaOperazioni = vistaOperazioni;
    }

	/**
	 * Metodo centrale per la gestione degli eventi.
	 * Analizza la sorgente dell'evento (Button o TextField) e invoca 
	 * il metodo di gestione logica corrispondente.
	 * <p>
	 * Al termine di ogni operazione di modifica, notifica il sistema 
	 * che i dati sono cambiati per abilitare il salvataggio in chiusura.
	 * 
	 * @param e L'evento ActionEvent catturato.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = "";

		// se la fonte è un bottone, leggiamo il testo (es. "Aggiungi")
        if (e.getSource() instanceof JButton) {
            comando = ((JButton) e.getSource()).getText();
        } else
        // se la fonte è il JTextField (pressione di INVIO)
        if (e.getSource() instanceof JTextField) {
            comando = "Cerca";
        }
		
	    switch (comando) {
	        case "Aggiungi" -> gestisciAggiungi();
	        case "Rimuovi" -> gestisciRimuovi();
	        case "Aggiungi dal catalogo" -> gestisciAggiungiEsistente();
	        case "Visualizza Cestino" -> gestisciVisualizzaCestino();
	        case "Cerca" -> gestisciRicerca();
	        case "Reset" -> gestisciReset();
	    }
	    
	    // setta a true il valore modifica del GestoreListe
        switch(comando) {
        	case "Aggiungi", "Rimuovi", "Aggiungi dal catalogo" -> GestioneListe.setModificato(true);
        }
        
	    if (!comando.equals("Cerca") && !comando.equals("Reset")) {
            contenutoLista.updateView();
        }
        
        if (controllerGlobale != null) {
            controllerGlobale.aggiornaTutto();
        }
		vistaPrincipale.aggiornaDati();
	}

	/**
	 * Apre un dialogo per la creazione di un nuovo {@link Articolo}.
	 * L'articolo viene inserito sia nella lista corrente che nel registro globale.
	 * <p>
	 * Gestisce internamente l'eventuale duplicazione nel catalogo globale 
	 * senza interrompere il flusso dell'utente.
	 */
	private void gestisciAggiungi() {
	    String[] inputs = new DialogoArticolo().getInputs("Aggiungi Articolo");

	    if (inputs != null) {
	        try {
	            Articolo nuovo = new Articolo(inputs[0], inputs[1], 
	                                          Double.parseDouble(inputs[2]), inputs[3]);

	            // aggiunta dell'articolo nella lista locale
	            model.inserisciArticolo(nuovo);
	            try {
	                GestioneListe.inserisciArticolo(nuovo);
	            } catch (Exception e) {
	                // se l'articolo esiste già globalmente, il sistema ignora l'errore 
	            }

	            // aggiorna la vista corrente
	            contenutoLista.updateView(); 

	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Errore: " + ex.getMessage());
	        }
	    }	
	}


	/**
	 * Rimuove l'articolo correntemente selezionato dalla vista.
	 * L'articolo non viene eliminato definitivamente, ma spostato nel cestino della lista.
	 */
	private void gestisciRimuovi() {
		Articolo articoloSel = contenutoLista.getArticoloSelezionato();

		if (articoloSel == null) {
			JOptionPane.showMessageDialog(null, "Seleziona un articolo dalla lista per rimuoverlo", "Nessuna selezione", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int conferma = JOptionPane.showConfirmDialog(null,
				"Sei sicuro di voler rimuovere l'articolo " + articoloSel.getNome()
						+ " dalla lista? (verra spostato el cestino)",
				"Conferma rimozione", JOptionPane.YES_NO_OPTION);
		if (conferma == JOptionPane.YES_OPTION) {
			try {
				model.cancellaArticolo(articoloSel);
			} catch (ListaDiArticoliException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	/**
	 * Permette di selezionare un articolo già esistente nel catalogo globale
	 * per aggiungerlo alla lista corrente.
	 */
	private void gestisciAggiungiEsistente() {
	    List<Articolo> catalogo = GestioneListe.getArticoli();
	    if (catalogo.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "Il catalogo globale è vuoto!");
	        return;
	    }

	    String[] opzioni = new String[catalogo.size()];
	    for (int i = 0; i < catalogo.size(); i++) {
	        Articolo a = catalogo.get(i);
	        opzioni[i] = a.getNome() + " [" + a.getCategoria() + "]";
	    }

	    String scelta = (String) JOptionPane.showInputDialog(null, 
	            "Seleziona un articolo dal catalogo:", "Aggiungi articolo dal catalogo",
	            JOptionPane.QUESTION_MESSAGE, null, opzioni, opzioni[0]);

	    if (scelta != null) {
	        for (int i = 0; i < opzioni.length; i++) {
	            if (opzioni[i].equals(scelta)) {
	                try {
	                    model.inserisciArticolo(catalogo.get(i));
	                    break;
	                } catch (Exception ex) {
	                    JOptionPane.showMessageDialog(null, ex.getMessage());
	                }
	            }
	        }
	    }
	}

	/**
	 * Inizializza e visualizza il dialogo del cestino per la lista corrente.
	 * Crea un {@link ControlloCestino} dedicato per gestire il ripristino o l'eliminazione definitiva.
	 */
	private void gestisciVisualizzaCestino() {
		ControlloCestino controllerCestino = new ControlloCestino(model, contenutoLista, vistaPrincipale);
		new CestinoDialog(null, model, controllerCestino);

		contenutoLista.updateView();
	}
	
	/**
	 * Esegue un filtraggio degli articoli nella vista in base al prefisso inserito.
	 * Se la ricerca ha successo, abilita l'opzione di Reset nella UI.
	 */
	private void gestisciRicerca() {
        if (vistaOperazioni == null) return;
        
        String prefisso = vistaOperazioni.getTestoRicerca();
        
        if (prefisso == null || prefisso.isBlank()) {
            gestisciReset(); // se premo invio su campo vuoto, resetta
        } else {
            java.util.List<Articolo> risultati = model.ricercaArticolo(prefisso);
            contenutoLista.mostraRisultatiRicerca(risultati);
            vistaOperazioni.mostraReset(true); // mostra il tasto Reset
        }
    }
	
	/**
	 * Annulla ogni filtro di ricerca attivo e ripristina la visualizzazione 
	 * completa degli articoli presenti nella lista.
	 */
	private void gestisciReset() {
        if (vistaOperazioni == null) return;
        
        vistaOperazioni.pulisciCampo();   
        vistaOperazioni.mostraReset(false); // nasconde il tasto Reset
        contenutoLista.updateView();      
    }
}
