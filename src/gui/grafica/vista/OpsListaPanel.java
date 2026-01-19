package gui.grafica.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.grafica.controllo.ControlloLista;

/**
 * La classe {@code OpsListaPanel} rappresenta il pannello operativo dedicato 
 * alle azioni sulla lista corrente.
 * <p>
 * Il pannello è diviso in due aree principali:
 * <ul>
 *    <li><b>Area Operazioni:</b> Contiene i pulsanti per aggiungere, rimuovere 
 *    o gestire articoli (inclusa la visualizzazione del cestino).</li>
 *    <li><b>Area Ricerca:</b> Include un campo di testo per filtrare gli articoli 
 *    per nome e un tasto di reset dinamico.</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class OpsListaPanel extends JPanel{
	/** Campo di input per la ricerca testuale degli articoli */
	private JTextField campoRicerca;
	
	/** Pulsante per annullare il filtro di ricerca e ripristinare la vista completa */
	private JButton btnReset;
	
	/**
	 * Crea un nuovo pannello operativo e inizializza i suoi componenti.
	 * 
	 * @param controllo Il controller {@link ControlloLista} che riceverà 
	 * le notifiche degli eventi generati dai pulsanti e dal campo ricerca.
	 */
	public OpsListaPanel(ControlloLista controllo) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 5));
		
		// Bottoni
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
		
			//Ricerca
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

	/**
	 * Svuota il contenuto del campo di ricerca.
	 */
	public void pulisciCampo() {
        campoRicerca.setText("");
    }

	/**
     * Gestisce la visibilità del pulsante di Reset.
     * <p>
     * Questo metodo chiama {@code revalidate()} e {@code repaint()} per assicurarsi 
     * che l'interfaccia grafica si aggiorni correttamente non appena il bottone 
     * viene mostrato o nascosto.
     * 
     * @param visibile {@code true} per mostrare il tasto Reset, {@code false} per nasconderlo.
     */
    public void mostraReset(boolean visibile) {
        btnReset.setVisible(visibile);
        revalidate(); // Forza il ridisegno del layout
        repaint();
    }

    /**
     * Restituisce il testo attualmente inserito dall'utente nella barra di ricerca.
     * 
     * @return Una {@link String} contenente il prefisso da ricercare.
     */
    public String getTestoRicerca() {
        return campoRicerca.getText();
    }
}
