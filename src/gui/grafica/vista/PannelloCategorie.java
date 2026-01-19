package gui.grafica.vista;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import gui.grafica.controllo.ControlloGestore;
import modello.Articolo;
import modello.GestioneListe;
import modello.ListaDiArticoli;

/**
 * La classe {@code PannelloCategorie} rappresenta l'interfaccia utente per la gestione
 * dell'anagrafica delle categorie merceologiche del sistema.
 * <p>
 * Questo pannello visualizza in formato tabellare tutte le categorie registrate nel sistema,
 * mostrando per ciascuna:
 * <ul>
 *   <li>Il nome della categoria</li>
 *   <li>Il numero totale di articoli attivi appartenenti a quella categoria in tutte le liste</li>
 *   <li>Il numero totale di articoli di quella categoria presenti nei cestini di tutte le liste</li>
 * </ul>
 * 
 * <p>Fornisce inoltre i comandi per aggiungere nuove categorie o eliminare quelle esistenti,
 * con il vincolo che la categoria predefinita non può essere rimossa.
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class PannelloCategorie extends JPanel {
	/** 
	 * Componente grafica per la visualizzazione tabellare delle categorie. 
	 */
    private JTable tabellaCategorie;

    /** 
     * Modello dei dati che gestisce il contenuto e la struttura delle categorie. 
     */
    private DefaultTableModel tableModel;

    /**
     * Costruisce il pannello delle categorie inizializzando la tabella e i comandi disponibili.
     * Configura il layout, crea i pulsanti per l'aggiunta e l'eliminazione di categorie,
     * e collega il controller per la gestione degli eventi.
     * 
     * @param controllo Il controller {@link ControlloGestore} che gestisce la logica delle operazioni
     *                  sulle categorie
     */
    public PannelloCategorie(ControlloGestore controllo) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        String[] colonne = {"Nome Categoria", "Articoli", "Cestino"};
        
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tabellaCategorie = new JTable(tableModel);
        tabellaCategorie.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaCategorie.setFillsViewportHeight(true); 
        
        add(new JScrollPane(tabellaCategorie), BorderLayout.CENTER);

        // Pannello per i bottoni di operazione
        JPanel bottoni = new JPanel();
        JButton btnAggiungi = new JButton("Aggiungi Categoria");
        JButton btnElimina = new JButton("Elimina Categoria");

        // Assegnazione del controller ai bottoni
        btnAggiungi.addActionListener(controllo);
        btnElimina.addActionListener(controllo);

        bottoni.add(btnAggiungi);
        bottoni.add(btnElimina);
        add(bottoni, BorderLayout.NORTH);

        aggiornaDati();
    }

    /**
     * Sincronizza la visualizzazione della tabella con i dati correnti presenti nel sistema.
     * <p>
     * Per ogni categoria registrata in {@link GestioneListe}, il metodo:
     * <ul>
     *   <li>Scansiona tutte le liste esistenti nel sistema</li>
     *   <li>Conta gli articoli attivi appartenenti alla categoria</li>
     *   <li>Conta gli articoli di quella categoria presenti nei cestini</li>
     *   <li>Aggiorna la riga corrispondente nella tabella con i conteggi ottenuti</li>
     * </ul>
     * 
     * <p>Questo metodo fornisce una vista aggregata dell'utilizzo di ciascuna categoria
     * attraverso tutte le liste del sistema
     */
    public void aggiornaDati() {
        tableModel.setRowCount(0);
        
        for (String cat : GestioneListe.getCategorie()) {
            int contaGlobali = 0;
            
            
            for (Articolo a : GestioneListe.getArticoli()) {
                if (a.getCategoria().equalsIgnoreCase(cat)) {
                    contaGlobali++;
                }
            }
            
            int contaCestino = 0;
            for (ListaDiArticoli lista : GestioneListe.getListeArticoli()) {
                for (Articolo a : lista.getArticoliCancellati()) {
                    if (a.getCategoria().equalsIgnoreCase(cat)) {
                        contaCestino++;
                    }
                }
            }
            
            Object[] riga = { cat, contaGlobali, contaCestino };
            tableModel.addRow(riga);
        }
    }

    /**
     * Recupera il nome della categoria attualmente selezionata dall'utente nella tabella.
     * 
     * @return Il nome della categoria selezionata come {@code String},
     * oppure {@code null} se non è presente alcuna selezione
     */
    public String getCategoriaSelezionata() {
        int riga = tabellaCategorie.getSelectedRow();
        if (riga == -1) 
        	return null;
        
        return (String) tableModel.getValueAt(riga, 0);
    }
}
