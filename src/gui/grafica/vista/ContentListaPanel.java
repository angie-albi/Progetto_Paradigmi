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

@SuppressWarnings("serial")
public class ContentListaPanel extends JPanel {
	private ListaDiArticoli model;
	private JTable tabella;
	private DefaultTableModel tableModel;
	private JLabel labelCostoTotale;

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
     * Aggiorna la vista svuotando la tabella e riempiendola con i dati aggiornati del modello
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
     * Recupera l'articolo corrispondente alla riga selezionata nella tabella.
     * 
     * @return L'oggetto Articolo selezionato, oppure null se nulla è selezionato.
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
        
    public void mostraRisultatiRicerca(List<Articolo> risultati) {
        tableModel.setRowCount(0);
        for (Articolo a : risultati) {
            aggiungiRiga(a);
        }
    }
    
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
    
    private void aggiungiRiga(Articolo a) {
        tableModel.addRow(new Object[]{
            a.getNome(), 
            a.getCategoria(), 
            String.format("%.2f", a.getPrezzo()), 
            a.getNota()
        });
    }
    
    private void aggiornaTotale() {
        double totale = model.calcoloPrezzoTotale();
        labelCostoTotale.setText("Totale: € " + String.format("%.2f", totale) + "  ");
    }
}
