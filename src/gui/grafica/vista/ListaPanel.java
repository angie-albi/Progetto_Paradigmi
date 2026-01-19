package gui.grafica.vista;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import gui.grafica.controllo.ControlloGestore;
import gui.grafica.controllo.ControlloLista;
import modello.ListaDiArticoli;

/**
 * La classe {@code ListaPanel} funge da contenitore aggregatore per la gestione di una singola lista.
 * <p>
 * Il suo scopo principale è quello di assemblare le diverse componenti grafiche e logiche 
 * relative a una specifica {@link ListaDiArticoli}. In particolare, coordina:
 * <ul>
 *    <li>{@link ContentListaPanel}: La parte centrale che visualizza la tabella degli articoli.</li>
 *    <li>{@link OpsListaPanel}: La parte superiore contenente i pulsanti di operazione e ricerca.</li>
 *    <li>{@link ControlloLista}: Il controller dedicato che gestisce la logica di questa specifica lista.</li>
 * </ul>
 * 
 * Utilizza un {@link BorderLayout} per organizzare gli elementi in modo ordinato e spazioso.
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class ListaPanel extends JPanel{

	/**
	 * Costruisce e configura il pannello assemblando vista e controller.
	 * 
	 * @param model Il modello {@link ListaDiArticoli} da gestire.
	 * @param vistaPrincipale Riferimento alla vista principale per la gestione degli eventi.
	 * @param controllerGlobale Riferimento al controller centrale del sistema.
	 */
	public ListaPanel(ListaDiArticoli model, PannelloListe vistaPrincipale, ControlloGestore controllerGlobale) {		setLayout(new BorderLayout());
	    setBorder(new EmptyBorder(10, 10, 10, 10));
	    
	    ContentListaPanel contenutoLista = new ContentListaPanel(model);
	    ControlloLista controllo = new ControlloLista(contenutoLista, model, vistaPrincipale, controllerGlobale);	    
	    OpsListaPanel operazioniLista = new OpsListaPanel(controllo);
	    controllo.setVistaOperazioni(operazioniLista);
	    
	    add(contenutoLista, BorderLayout.CENTER);
	    add(operazioniLista, BorderLayout.NORTH);
	}

}
