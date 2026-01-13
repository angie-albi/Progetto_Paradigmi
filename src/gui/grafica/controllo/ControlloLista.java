package gui.grafica.controllo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import gui.grafica.vista.ContentPanel;
import gui.grafica.vista.DialogoArticolo;
import modello.ListaDiArticoli;
import modello.exception.ArticoloException;
import modello.exception.ListaDiArticoliException;

public class ControlloLista implements ActionListener{
	private ContentPanel contenutoLista;
	private ListaDiArticoli model;
	
	public ControlloLista(ContentPanel contenutoLista, ListaDiArticoli model) {
		this.contenutoLista = contenutoLista;
		this.model = model;
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		if (source.getText().equals("Aggiungi")) {
			String[] inputs = new DialogoArticolo().getInputs("Aggiungi Articolo");
			if (inputs != null) {
				try {
//					String input3;
//					input3= inputs[3].toString();
					model.inserisciArticolo(inputs[0], inputs[1]/*, input3 , inputs[4]*/);
				} catch (ListaDiArticoliException ex) {
					
				} catch (ArticoloException e1) {
					e1.printStackTrace();
				}
			}
		} else if (source.getText().equals(" Modifica ")) {

		} else {

		}
		contenutoLista.updateView();
	}

}
