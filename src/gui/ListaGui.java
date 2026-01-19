package gui;

import javax.swing.JFrame;
import javax.swing.JPanel;

import gui.grafica.controllo.ControlloGestore;
import gui.grafica.vista.ListaPanel;
import gui.grafica.vista.PannelloListe;
import modello.ListaDiArticoli;

/**La classe {@code ListaGui} rappresenta la finestra dedicata alla visualizzazione
 * e gestione dettagliata di una singola lista di articoli.
 * <p>
 * Questa finestra viene aperta quando l'utente seleziona una lista specifica dal
 * pannello principale e permette di:
 * <ul>
 *   <li>Visualizzare tutti gli articoli presenti nella lista</li>
 *   <li>Aggiungere nuovi articoli alla lista</li>
 *   <li>Rimuovere articoli (spostandoli nel cestino)</li>
 *   <li>Aggiungere articoli dal catalogo globale</li>
 *   <li>Visualizzare e gestire il cestino della lista</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class ListaGui extends JFrame {

	/**
	 * Crea una nuova finestra per la gestione di una lista specifica.
	 * <p>
	 * Configura il comportamento della finestra (chiusura solo del frame corrente) 
	 * e inizializza il pannello interno {@link ListaPanel} che contiene tutta la logica operativa.
	 * 
	 * @param model Il modello {@link ListaDiArticoli} contenente i dati della lista da mostrare.
	 * @param vistaPrincipale Riferimento alla vista principale per permettere gli aggiornamenti grafici.
	 * @param controllerGlobale Il controller globale che coordina le diverse sezioni del sistema.
	 */
	public ListaGui(ListaDiArticoli model, PannelloListe vistaPrincipale, ControlloGestore controllerGlobale) {

		setTitle("Lista: " + model.getNome());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(250, 150, 750, 500);

		JPanel listaPanel = new ListaPanel(model, vistaPrincipale, controllerGlobale);
		setContentPane(listaPanel);

		setVisible(true);
	}
}