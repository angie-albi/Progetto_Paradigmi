package gui.grafica.vista;

import javax.swing.JButton;
import javax.swing.JPanel;

import gui.grafica.controllo.ControlloLista;

@SuppressWarnings("serial")
public class OpsListaPanel extends JPanel{

	public OpsListaPanel(ControlloLista controllo) {
		JButton addArticolo = new JButton("Aggiungi"); 
		addArticolo.addActionListener(controllo); 
		add(addArticolo); 
		
		JButton deleteArticolo = new JButton("Rimuovi");
		deleteArticolo.addActionListener(controllo);
		add(deleteArticolo);
		
		JButton addArticoloEsistente = new JButton("Aggiungi dal catalogo");
		addArticoloEsistente.addActionListener(controllo);
		add(addArticoloEsistente);
		
		JButton visualizzaCestino = new JButton("Visualizza Cestino");
		visualizzaCestino.addActionListener(controllo);
		add(visualizzaCestino);
	}

}
