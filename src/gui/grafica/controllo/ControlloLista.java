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

public class ControlloLista implements ActionListener {
	private ContentListaPanel contenutoLista;
	private ListaDiArticoli model;
	private PannelloListe vistaPrincipale;
	private ControlloGestore controllerGlobale;
	private OpsListaPanel vistaOperazioni;

	public ControlloLista(ContentListaPanel contenutoLista, ListaDiArticoli model, 
            PannelloListe vistaPrincipale, ControlloGestore controllerGlobale) {
		this.contenutoLista = contenutoLista;
		this.model = model;
		this.vistaPrincipale = vistaPrincipale;
		this.controllerGlobale = controllerGlobale; 
	}
	
	public void setVistaOperazioni(OpsListaPanel vistaOperazioni) {
        this.vistaOperazioni = vistaOperazioni;
    }

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

	    if (!comando.equals("Cerca") && !comando.equals("Reset")) {
            contenutoLista.updateView();
        }
        
        if (controllerGlobale != null) {
            controllerGlobale.aggiornaTutto();
        }
		vistaPrincipale.aggiornaDati();
	}

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

	private void gestisciVisualizzaCestino() {
		ControlloCestino controllerCestino = new ControlloCestino(model, contenutoLista, vistaPrincipale);
		new CestinoDialog(null, model, controllerCestino);

		contenutoLista.updateView();
	}
	
	private void gestisciRicerca() {
        if (vistaOperazioni == null) return;
        
        String prefisso = vistaOperazioni.getTestoRicerca();
        
        if (prefisso == null || prefisso.isBlank()) {
            gestisciReset(); // Se premo invio su campo vuoto, resetta
        } else {
            java.util.List<Articolo> risultati = model.ricercaArticolo(prefisso);
            contenutoLista.mostraRisultatiRicerca(risultati);
            vistaOperazioni.mostraReset(true); // Mostra il tasto Reset
        }
    }
	
	private void gestisciReset() {
        if (vistaOperazioni == null) return;
        
        vistaOperazioni.pulisciCampo();   // Svuota il JTextField
        vistaOperazioni.mostraReset(false); // Nasconde il tasto Reset
        contenutoLista.updateView();      // Torna alla lista normale (solo attivi)
    }
}
