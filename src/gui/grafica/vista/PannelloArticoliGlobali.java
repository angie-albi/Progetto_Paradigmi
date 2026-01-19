package gui.grafica.vista;

import java.awt.BorderLayout;
import java.util.List;

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

/**
 * La classe {@code PannelloArticoliGlobali} rappresenta l'interfaccia utente per la gestione
 * del registro globale degli articoli del sistema
 * <p>
 * Questo pannello visualizza in formato tabellare l'anagrafica completa di tutti gli articoli
 * registrati nel sistema tramite {@link GestioneListe}, mostrando per ciascun articolo:
 * <ul>
 *   <li>Nome identificativo</li>
 *   <li>Categoria merceologica di appartenenza</li>
 *   <li>Prezzo unitario</li>
 *   <li>Nota descrittiva opzionale</li>
 * </ul>
 * 
 * <p>Fornisce i comandi per aggiungere nuovi articoli al registro globale,
 * eliminare articoli esistenti o modificarne i dati.
 * 
 * <p>Gli articoli presenti in questo registro sono condivisi
 * tra tutte le liste del sistema. La modifica di un articolo qui si riflette
 * automaticamente in tutte le liste che lo contengono.
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class PannelloArticoliGlobali extends JPanel{
	/** Componente grafica per la visualizzazione tabellare degli articoli. */
    private JTable tabellaArticoli;

    /** Modello dei dati che gestisce il contenuto e la struttura della tabella. */
    private DefaultTableModel tableModel;

    /**
     * Costruisce il pannello degli articoli globali inizializzando la tabella
     * e i comandi disponibili.
     * <p>
     * Configura il layout, crea i pulsanti per l'aggiunta, l'eliminazione e la modifica
     * degli articoli, e collega il controller per la gestione degli eventi.
     * La tabella viene configurata in modalità di selezione singola e con celle
     * non modificabili direttamente.
     * 
     * @param controllo Il controller {@link ControlloGestore} che gestisce la logica
     * delle operazioni sugli articoli globali
     */
    public PannelloArticoliGlobali(ControlloGestore controllo) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        String[] colonne = {"Nome", "Categoria", "Prezzo (€)", "Nota"};
        
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // celle non modificabili direttamente in tabella
            }
        };

        tabellaArticoli = new JTable(tableModel);
        tabellaArticoli.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabellaArticoli.setFillsViewportHeight(true);
        
        add(new JScrollPane(tabellaArticoli), BorderLayout.CENTER);

        // Pannello operazioni globali
        JPanel bottoni = new JPanel();
        
        JButton btnAggiungi = new JButton("Aggiungi Articolo");
        btnAggiungi.addActionListener(controllo);
        bottoni.add(btnAggiungi);
        
        JButton btnElimina = new JButton("Elimina Articolo");
        btnElimina.addActionListener(controllo);
        bottoni.add(btnElimina);
        
        JButton btnModifica = new JButton("Modifica Articolo");
        btnModifica.addActionListener(controllo);
        bottoni.add(btnModifica);
        
        add(bottoni, BorderLayout.NORTH);

        aggiornaDati();
    }

    /**
     * Sincronizza la visualizzazione della tabella con i dati correnti del registro globale.
     * <p>
     * Svuota completamente la tabella e la ripopola con tutti gli articoli presenti
     * nell'anagrafica centrale gestita da {@link GestioneListe}.
     * Per ogni articolo vengono visualizzati nome, categoria, prezzo formattato
     * con due decimali e nota descrittiva.
     */
    public void aggiornaDati() {
        tableModel.setRowCount(0);
        
        List<Articolo> articoliGlobali = GestioneListe.getArticoli();
        
        for (Articolo a : articoliGlobali) {
            Object[] riga = {
                a.getNome(),
                a.getCategoria(),
                String.format("%.2f", a.getPrezzo()),
                a.getNota()
            };
            tableModel.addRow(riga);
        }
    }

    /**
     * Identifica e restituisce l'articolo corrispondente alla riga attualmente
     * selezionata nella tabella.
     * <p>
     * La ricerca viene effettuata nel registro globale confrontando nome e categoria
     * (case-insensitive) dell'articolo visualizzato con quelli presenti in {@link GestioneListe}.
     * 
     * @return L'oggetto {@code Articolo} selezionato dal registro globale,
     * oppure {@code null} se non c'è alcuna selezione o se l'articolo
     * non viene trovato nel registro
     */
    public Articolo getArticoloSelezionato() {
        int riga = tabellaArticoli.getSelectedRow();
        if (riga == -1) 
        	return null;

        String nome = (String) tableModel.getValueAt(riga, 0);
        String categoria = (String) tableModel.getValueAt(riga, 1);

        for (Articolo a : GestioneListe.getArticoli()) {
            if (a.getNome().equalsIgnoreCase(nome) && a.getCategoria().equalsIgnoreCase(categoria)) {
                return a;
            }
        }
        return null;
    }
}
