package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.grafica.controllo.ControlloGestore;
import gui.grafica.vista.ListaPanel;
import gui.grafica.vista.PannelloListe;
import modello.ListaDiArticoli;

@SuppressWarnings("serial")
public class ListaGui extends JFrame {

	public ListaGui(ListaDiArticoli model, PannelloListe vistaPrincipale, ControlloGestore controllerGlobale) {

		setTitle("Lista: " + model.getNome());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(350, 150, 600, 500);

		JPanel listaPanel = new ListaPanel(model, vistaPrincipale, controllerGlobale);
		setContentPane(listaPanel);

		setVisible(true);
	}

}