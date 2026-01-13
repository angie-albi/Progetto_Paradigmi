package gui.grafica.vista;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.grafica.controllo.ControlloLista;

public class OpsPanel extends JPanel{

	public OpsPanel(ControlloLista controllo) {
		JButton addArticolo = new JButton("Aggiungi"); 
		addArticolo.addActionListener(controllo); 
		add(addArticolo); 
		
		JButton deleteArticolo = new JButton("Rimuovi");
		deleteArticolo.addActionListener(controllo);
		add(deleteArticolo);
		
		JButton visualizzaCestino = new JButton("Visualizza Cestino");
		visualizzaCestino.addActionListener(controllo);
		add(visualizzaCestino);
	}

}
