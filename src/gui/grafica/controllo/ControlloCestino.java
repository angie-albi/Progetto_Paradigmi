package gui.grafica.controllo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import gui.grafica.vista.CestinoDialog;
import gui.grafica.vista.ContentListaPanel;
import gui.grafica.vista.PannelloListe;
import modello.Articolo;
import modello.ListaDiArticoli;
import modello.exception.ListaDiArticoliException;

/**
 * La classe {@code ControlloCestino} gestisce le operazioni eseguibili sugli articoli
 * presenti nel cestino di una lista specifica.
 * <p>
 * Implementa l'interfaccia {@link ActionListener} per intercettare e gestire gli eventi
 * generati dai pulsanti del dialogo del cestino, coordinando le seguenti operazioni:
 * <ul>
 *   <li>Recupero di singoli articoli dalla lista dei cancellati alla lista attiva</li>
 *   <li>Svuotamento completo del cestino con eliminazione definitiva di tutti gli articoli</li>
 * </ul>
 * 
 * <p>Dopo ogni operazione, il controller si occupa di aggiornare sia la vista del cestino
 * che le viste principali della lista e del pannello globale delle liste.
 * 
 * @author Angie Albitres
 */
public class ControlloCestino implements ActionListener{
	
	private CestinoDialog vistaCestino;
    private ListaDiArticoli model;
    private ContentListaPanel vistaPrincipaleContenuto;
    private PannelloListe vistaPrincipale;

    /**
     * Costruisce un nuovo controller per il cestino collegandolo al modello e alle viste
     * che devono essere aggiornate dopo le operazioni.
     * 
     * @param model Il modello {@link ListaDiArticoli} che contiene gli articoli cancellati
     * @param vistaPrincipaleContenuto Il pannello del contenuto della lista da aggiornare
     * @param vistaPrincipale Il pannello globale delle liste da sincronizzare
     */
    public ControlloCestino(ListaDiArticoli model, ContentListaPanel vistaPrincipaleContenuto, PannelloListe vistaPrincipale) {
        this.model = model;
        this.vistaPrincipaleContenuto = vistaPrincipaleContenuto;
        this.vistaPrincipale = vistaPrincipale;
    }
    
    /**
     * Collega la vista del dialogo del cestino al controller.
     * Questo metodo viene chiamato dal {@link CestinoDialog} dopo la sua creazione.
     * 
     * @param vistaCestino Il dialogo del cestino da gestire
     */
    public void setVista(CestinoDialog vistaCestino) {
        this.vistaCestino = vistaCestino;
    }
    
    /**
     * Gestisce gli eventi generati dai pulsanti del dialogo del cestino.
     * Identifica il comando in base al testo del pulsante premuto e invoca
     * il metodo di gestione appropriato.
     * <p>
     * Dopo l'esecuzione dell'operazione, aggiorna tutte le viste coinvolte:
     * il dialogo del cestino, il contenuto della lista e il pannello globale delle liste.
     * 
     * @param e L'evento di azione generato dal pulsante premuto
     */
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = ((JButton) e.getSource()).getText();

        switch (comando) {
            case "Recupera" -> gestisciRecupera();
            case "Svuota Cestino" -> gestisciSvuotaCestino();
            default -> System.out.println("Comando non riconosciuto nel cestino: " + comando);
        }
        
        vistaCestino.aggiornaVista();   
        vistaPrincipaleContenuto.updateView();
        vistaPrincipale.aggiornaDati();
	}
	
	 /**
     * Gestisce il recupero di un articolo selezionato dal cestino
     * Verifica che sia stato selezionato un articolo nella vista del cestino,
     * quindi tenta di ripristinarlo nella lista attiva tramite il modello.
     * Mostra un messaggio di conferma in caso di successo o un messaggio
     * di errore se l'operazione fallisce.
     * Se nessun articolo è selezionato, viene mostrato un messaggio di avviso.
     */
	private void gestisciRecupera() {
		Articolo daRecuperare = vistaCestino.getArticoloSelezionato();
        if (daRecuperare == null) {
            JOptionPane.showMessageDialog(vistaCestino, "Seleziona un articolo da recuperare", "Nessuna selezione", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            model.recuperaArticolo(daRecuperare);
            JOptionPane.showMessageDialog(null, "Recupero Effettuato");
        } catch (ListaDiArticoliException ex) {
            JOptionPane.showMessageDialog(vistaCestino, ex.getMessage());
        }
	}

	/**
     * Gestisce lo svuotamento completo del cestino.
     * Richiede conferma all'utente prima di procedere con l'eliminazione definitiva
     * di tutti gli articoli presenti nel cestino.
     * Se l'utente conferma l'operazione, tutti gli articoli cancellati vengono
     * rimossi permanentemente tramite il metodo {@link ListaDiArticoli#svuotaCancellati()}.
     */
	private void gestisciSvuotaCestino() {
		int conferma = JOptionPane.showConfirmDialog(vistaCestino, "Eliminare definitivamente tutto il cestino?");
		
        if (conferma == JOptionPane.YES_OPTION) 
            model.svuotaCancellati();
	}
}
