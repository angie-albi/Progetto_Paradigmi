package gui;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import gui.grafica.controllo.ControlloGestore;
import gui.grafica.vista.PannelloArticoliGlobali;
import gui.grafica.vista.PannelloCategorie;
import gui.grafica.vista.PannelloListe;
import modello.GestioneListe;

/**
 * La classe {@code GestoreGui} rappresenta la finestra principale dell'interfaccia grafica (GUI).
 * <p>
 * Questa classe estende {@link JFrame} e funge da orchestratore principale dell'applicazione.
 * Si occupa di:
 * <ul>
 *    <li>Inizializzare il frame principale e le sue impostazioni estetiche.</li>
 *    <li>Configurare il sistema di schede ({@link JTabbedPane}) per la navigazione tra le sezioni.</li>
 *    <li>Istanziare il controller globale {@link ControlloGestore} e collegarlo alle relative viste.</li>
 *    <li>Gestire il ciclo di vita dell'applicazione, inclusi il caricamento iniziale e il salvataggio in chiusura.</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class GestoreGui extends JFrame {
	/**
	 * Costruisce un nuovo frame {@code GestoreGui}.
	 * <p>
	 * Il costruttore esegue le seguenti operazioni:
	 * <ol>
	 *   <li>Imposta il titolo e le dimensioni della finestra.</li>
	 *   <li>Configura un {@code WindowListener} per intercettare la chiusura e verificare la presenza di modifiche non salvate.</li>
	 *   <li>Inizializza il {@link ControlloGestore} (Controller) e i pannelli (Vista), stabilendo il legame tra di essi.</li>
	 *   <li>Organizza i pannelli all'interno di un componente a schede.</li>
	 * </ol>
	 */
	public GestoreGui() {
		setTitle("Gestore Liste");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		
		// Listener per la gestione della chiusura
		addWindowListener(new java.awt.event.WindowAdapter() {
	        @Override
	        public void windowClosing(java.awt.event.WindowEvent e) {
	            if (GestioneListe.getModificato()) {
	                int risposta = JOptionPane.showConfirmDialog(null, 
	                    "Ci sono modifiche non salvate. Vuoi salvare prima di uscire?", 
	                    "Salvataggio richiesto", JOptionPane.YES_NO_CANCEL_OPTION);
	                
	                if (risposta == JOptionPane.YES_OPTION) {
	                    try {
	                        GestioneListe.salvaSistema("dati_sistema.txt");
	                        System.exit(0);
	                    } catch (Exception ex) {
	                        JOptionPane.showMessageDialog(null, "Errore nel salvataggio: " + ex.getMessage());
	                    }
	                } else if (risposta == JOptionPane.NO_OPTION) {
	                    System.exit(0);
	                }
	            } else {
	                System.exit(0);
	            }
	        }
		});
		
		setSize(900, 600);
		setLocationRelativeTo(null);

		JTabbedPane tabbedPane = new JTabbedPane();

		// aggiunta controllo
		ControlloGestore controllo = new ControlloGestore();
		
		PannelloListe pannelloListe = new PannelloListe(controllo);
		controllo.setVistaListe(pannelloListe);
		
		PannelloCategorie pannelloCat = new PannelloCategorie(controllo);
		controllo.setVistaCategorie(pannelloCat);
		
		PannelloArticoliGlobali pannelloArt = new PannelloArticoliGlobali(controllo);
	    controllo.setVistaArticoli(pannelloArt);
		
		// aggiunta dei tre pannelli principali
		tabbedPane.addTab("Gestione Liste", pannelloListe);
		tabbedPane.addTab("Categorie", pannelloCat);
		tabbedPane.addTab("Articoli", pannelloArt);

		add(tabbedPane);
		setVisible(true);
	}

	/**
	 * Punto di ingresso principale (Entry Point) dell'intera applicazione.
	 * <p>
	 * Il metodo tenta di ripristinare lo stato del sistema caricando i dati dal file {@code dati_sistema.txt}.
	 * In caso di successo, i dati saranno disponibili nelle classi di modello; in caso di assenza del file,
	 * l'applicazione viene avviata con un database vuoto.
	 * 
	 * @param args Argomenti passati da riga di comando (non utilizzati in questa applicazione).
	 */
	public static void main(String[] args){
		String nomeFile = "dati_sistema.txt";

        try {
            // tenta il ripristino automatico
            GestioneListe.caricaSistema(nomeFile);
            System.out.println("Dati caricati correttamente da " + nomeFile);
            
        } catch (java.io.FileNotFoundException e) {
            // se il file non esiste
            System.out.println("Nessun salvataggio trovato. Avvio con sistema vuoto.");
            
        } catch (Exception e) {
            // altri errori
            JOptionPane.showMessageDialog(null, 
                "Si è verificato un errore nel caricamento automatico: " + e.getMessage(), 
                "Errore di Caricamento", 
                JOptionPane.ERROR_MESSAGE);
        }
        
        new GestoreGui();
	}
}