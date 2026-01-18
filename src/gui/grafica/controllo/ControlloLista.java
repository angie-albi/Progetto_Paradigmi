package gui.grafica.controllo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import gui.grafica.vista.CestinoDialog;
import gui.grafica.vista.ContentListaPanel;
import gui.grafica.vista.DialogoArticolo;
import gui.grafica.vista.PannelloListe;
import modello.Articolo;
import modello.ListaDiArticoli;
import modello.exception.ArticoloException;
import modello.exception.ListaDiArticoliException;

public class ControlloLista implements ActionListener {
	private ContentListaPanel contenutoLista;
	private ListaDiArticoli model;
	private PannelloListe vistaPrincipale;

	public ControlloLista(ContentListaPanel contenutoLista, ListaDiArticoli model, PannelloListe vistaPrincipale) {
		this.contenutoLista = contenutoLista;
		this.model = model;
		this.vistaPrincipale = vistaPrincipale;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = ((JButton) e.getSource()).getText();

	    switch (comando) {
	        case "Aggiungi" -> gestisciAggiungi();
	        case "Rimuovi" -> gestisciRimuovi();
	        case "Aggiungi dal catalogo" -> gestisciAggiungiEsistente();
	        case "Visualizza Cestino" -> gestisciVisualizzaCestino();
	    }

		contenutoLista.updateView();
		vistaPrincipale.aggiornaDati();
	}

	private void gestisciAggiungi() {
		String[] inputs = new DialogoArticolo().getInputs("Aggiungi Articolo");

		if (inputs != null) {
			try {
				String nome = inputs[0];
				String categoria = inputs[1];
				double prezzo = Double.parseDouble(inputs[2]);
				String nota = inputs[3];

				try {
					model.inserisciArticolo(nome, categoria, prezzo, nota);
				} catch (ArticoloException ex) {
					JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore Modello", JOptionPane.ERROR_MESSAGE);
				}
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(null, "Il prezzo deve essere un numero valido!", "Errore Input",
						JOptionPane.ERROR_MESSAGE);
			} catch (ListaDiArticoliException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore Modello", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void gestisciRimuovi() {
		Articolo articoloSel = contenutoLista.getArticoloSelezionato();

		if (articoloSel == null) {
			JOptionPane.showMessageDialog(null, "Seleziona un articolo dalla lista per rimuoverlo",
					"Nessuna selezione", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int conferma = JOptionPane.showConfirmDialog(null,
				"Sei sicuro di voler rimuovere l'articolo " + articoloSel.getNome()
						+ " dalla lista? (verra spostato el cestino)",
				"Conferma rimozione articolo", JOptionPane.YES_NO_OPTION);
		if (conferma == JOptionPane.YES_OPTION) {
			try {
				model.cancellaArticolo(articoloSel);
				JOptionPane.showMessageDialog(null, "Articolo rimosso");
			} catch (ListaDiArticoliException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Errore", JOptionPane.ERROR_MESSAGE);
			}
		}

	}

	private void gestisciAggiungiEsistente() {

	}

	private void gestisciVisualizzaCestino() {
		ControlloCestino controllerCestino = new ControlloCestino(model, contenutoLista, vistaPrincipale);
		new CestinoDialog(null, model, controllerCestino);

		contenutoLista.updateView();
	}
}
