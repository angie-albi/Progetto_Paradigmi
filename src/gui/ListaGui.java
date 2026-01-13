package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.grafica.vista.ListaPanel;
import modello.ListaDiArticoli;

public class ListaGui extends JFrame{

	public ListaGui(ListaDiArticoli model){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		setTitle("Lista " + model.getNome());
		JPanel listaPanel = new ListaPanel(model);
		setContentPane(listaPanel);
		setVisible(true);
	}

}

