package gui.grafica.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.grafica.controllo.ControlloLista;

@SuppressWarnings("serial")
public class OpsListaPanel extends JPanel{
	private JTextField campoRicerca;
	private JButton btnReset;
	
	public OpsListaPanel(ControlloLista controllo) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		JPanel pnlBottoni = new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		JButton addArticolo = new JButton("Aggiungi"); 
		addArticolo.addActionListener(controllo); 
		pnlBottoni.add(addArticolo); 
		
		JButton deleteArticolo = new JButton("Rimuovi");
		deleteArticolo.addActionListener(controllo);
		pnlBottoni.add(deleteArticolo);
		
		JButton addArticoloEsistente = new JButton("Aggiungi dal catalogo");
		addArticoloEsistente.addActionListener(controllo);
		pnlBottoni.add(addArticoloEsistente);
		
		JButton visualizzaCestino = new JButton("Visualizza Cestino");
		visualizzaCestino.addActionListener(controllo);
		pnlBottoni.add(visualizzaCestino);
		
			JPanel pnlRicerca = new JPanel(new FlowLayout(FlowLayout.LEFT));
			JLabel ricercaArticolo = new JLabel("Cerca: ");
			pnlRicerca.add(ricercaArticolo);
	    
		    campoRicerca = new JTextField(15);
		    campoRicerca.addActionListener(controllo); 
		    pnlRicerca.add(campoRicerca);
		    
		    btnReset = new JButton("Reset");
	        btnReset.setVisible(false); // nascosto all'avvio
	        btnReset.addActionListener(controllo);
	        pnlRicerca.add(btnReset);

        // Aggiungiamo i due sottopannelli al pannello principale
        add(pnlBottoni, BorderLayout.NORTH);
        add(pnlRicerca, BorderLayout.SOUTH);
	}

	public void pulisciCampo() {
        campoRicerca.setText("");
    }

    // Metodo per mostrare/nascondere il tasto reset
    public void mostraReset(boolean visibile) {
        btnReset.setVisible(visibile);
        revalidate(); // Forza il ridisegno del layout
        repaint();
    }

    public String getTestoRicerca() {
        return campoRicerca.getText();
    }
}
