package gui.grafica.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable; 
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import modello.ListaDiArticoli;
import modello.Articolo;

/**
 * La classe {@code ContentListaPanel} è il pannello deputato alla visualizzazione 
 * tabellare degli articoli contenuti in una specifica lista.
 * <p>
 * Oltre alla visualizzazione, il pannello si occupa di:
 * <ul>
 *    <li>Calcolare e mostrare il costo totale degli articoli attivi.</li>
 *    <li>Distinguere graficamente (tramite colori) gli articoli nel cestino.</li>
 *   <li>Fornire i metodi per recuperare l'articolo selezionato dall'utente.</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class ContentListaPanel extends JPanel {
	/** Il modello dei dati contenente la lista degli articoli */
	private ListaDiArticoli model;
	
	/** La tabella Swing per la visualizzazione dei dati */
	private JTable tabella;
	
	/** Il modello della tabella che gestisce righe e colonne */
	private DefaultTableModel tableModel;
	
	/** Etichetta per visualizzare il costo totale aggiornato */
	private JLabel labelCostoTotale;

	/**
	 * Inizializza il pannello e configura la struttura della tabella.
	 * 
	 * @param model Il modello {@link ListaDiArticoli} da cui attingere i dati.
	 */
	public ContentListaPanel(ListaDiArticoli model) {
		this.model = model;
		setLayout(new BorderLayout());
		
		//HEADER
		JPanel headerPanel = new JPanel(new BorderLayout());
		
		JLabel titoloLabel = new JLabel(" Contenuto lista: " + model.getNome());
		
		labelCostoTotale = new JLabel();
		labelCostoTotale.setForeground(new Color(0, 100, 0));
		
		headerPanel.add(labelCostoTotale, BorderLayout.EAST);
		headerPanel.add(titoloLabel, BorderLayout.WEST);
		add(headerPanel, BorderLayout.NORTH);
		
		// TABELLA
		String[] colonne = {"Nome", "Categoria", "Prezzo (€)", "Nota"};
		
		tableModel = new DefaultTableModel(colonne, 0) {
	        @Override
	        public boolean isCellEditable(int row, int column) { 
	        	return false; 
	        }
	    };
	    
	    tabella = new JTable(tableModel) {
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                // se l'articolo è nel cestino, impediamo la selezione
                if (isArticoloNelCestino(rowIndex)) 
                	return;
                super.changeSelection(rowIndex, columnIndex, toggle, extend);
            }
        };
        
        // configurazione 
        tabella.setDefaultRenderer(Object.class, new ArticoloRenderer()); // applica il renderer personalizzato per i colori
        tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
        tabella.getTableHeader().setReorderingAllowed(false); // impedisce di spostare le colonne
        tabella.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        tabella.setFillsViewportHeight(true);

        // PANNELLO SCORRIMENTO
		JScrollPane scrollPane = new JScrollPane(tabella);
		add(scrollPane, BorderLayout.CENTER);
		updateView();
	}

	/**
     * Sincronizza la tabella con lo stato attuale del modello.
     * Svuota la vista e ricarica solo gli articoli che non sono nel cestino.
     */
	public void updateView() {
        tableModel.setRowCount(0);
        List<Articolo> cancellati = model.getArticoliCancellati();
        
        for (Articolo a : model) {
            if (!cancellati.contains(a)) {
                aggiungiRiga(a);
            }
        }
        aggiornaTotale();
    }
	
	/**
     * Identifica l'oggetto {@link Articolo} corrispondente alla riga 
     * attualmente selezionata dall'utente.
     * 
     * @return L'articolo selezionato, oppure {@code null} se non ci sono selezioni attive.
     */
	public Articolo getArticoloSelezionato() {
        int riga = tabella.getSelectedRow();
        if (riga == -1) return null; 

        String nome = (String) tableModel.getValueAt(riga, 0);
        String categoria = (String) tableModel.getValueAt(riga, 1);

        for (Articolo a : model) {
            if (a.getNome().equalsIgnoreCase(nome) && a.getCategoria().equalsIgnoreCase(categoria)) {
                return a;
            }
        }
        return null;
    }
    
	/**
     * Classe interna per la personalizzazione grafica delle celle.
     * Colora di grigio le righe degli articoli presenti nel cestino.
     */
    private class ArticoloRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (isArticoloNelCestino(row)) {
                c.setBackground(new Color(200, 200, 200)); // grigio
            } else {
                c.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
                c.setForeground(isSelected ? table.getSelectionForeground() : table.getForeground());
            }
            return c;
        }
    }
        
    /**
     * Visualizza temporaneamente solo i risultati di una ricerca.
     * 
     * @param risultati La lista filtrata di articoli da mostrare.
     */
    public void mostraRisultatiRicerca(List<Articolo> risultati) {
        tableModel.setRowCount(0);
        for (Articolo a : risultati) {
            aggiungiRiga(a);
        }
    }
    
    /**
     * Verifica se l'articolo mostrato a una determinata riga della tabella 
     * appartiene alla lista dei cancellati del modello.
     * 
     * @param row L'indice della riga da controllare.
     * 
     * @return {@code true} se l'articolo è nel cestino, {@code false} altrimenti.
     */
    private boolean isArticoloNelCestino(int row) {
        if (row < 0) return false;
        String nome = (String) tableModel.getValueAt(row, 0);
        String cat = (String) tableModel.getValueAt(row, 1);
        
        for (Articolo a : model.getArticoliCancellati()) {
            if (a.getNome().equalsIgnoreCase(nome) && a.getCategoria().equalsIgnoreCase(cat)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Converte un oggetto {@link Articolo} in una riga leggibile dalla tabella.
     * 
     * @param a L'articolo da aggiungere.
     */
    private void aggiungiRiga(Articolo a) {
        tableModel.addRow(new Object[]{
            a.getNome(), 
            a.getCategoria(), 
            String.format("%.2f", a.getPrezzo()), 
            a.getNota()
        });
    }
    
    /**
     * Calcola il valore economico totale degli articoli attivi e aggiorna l'interfaccia.
     */
    private void aggiornaTotale() {
        double totale = model.calcoloPrezzoTotale();
        labelCostoTotale.setText("Totale: € " + String.format("%.2f", totale) + "  ");
    }
}
