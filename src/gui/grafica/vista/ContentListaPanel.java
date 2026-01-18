package gui.grafica.vista;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import modello.ListaDiArticoli;
import modello.Articolo;

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
		// colonne della tabella 
		String[] colonne = {"Nome", "Categoria", "Prezzo (€)", "Nota"};
		
		// creazione modello della tabella
		tableModel = new DefaultTableModel(colonne, 0) {
			// tabella non modificabile
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        tabella = new JTable(tableModel);
        
        // configurazione 
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

        java.util.List<Articolo> cancellati = model.getArticoliCancellati();
        for (Articolo a : model) {
            if (!cancellati.contains(a)) {
                Object[] riga = {
                    a.getNome(),
                    a.getCategoria(),
                    String.format("%.2f", a.getPrezzo()),
                    a.getNota()
                };
                tableModel.addRow(riga);
            }
        }
        
        double totale = model.calcoloPrezzoTotale();
        labelCostoTotale.setText("Totale: € " + String.format("%.2f", totale) + "  ");
    }
	
    /**
     * Recupera l'articolo corrispondente alla riga selezionata nella tabella.
     * 
     * @return L'oggetto Articolo selezionato, oppure null se nulla è selezionato.
     */
    public Articolo getArticoloSelezionato() {
        int rigaSelezionata = tabella.getSelectedRow();

        if (rigaSelezionata == -1)
            return null; 

        String nome = (String) tableModel.getValueAt(rigaSelezionata, 0);
        String categoria = (String) tableModel.getValueAt(rigaSelezionata, 1);

        try {
            Articolo articoloCerc = new Articolo(nome, categoria);

            for (Articolo a : model) {
                if (a.equals(articoloCerc)) {
                    return a;
                }
            }
        } catch (Exception e) {
            //non genera eccezioni
        }

        return null;
    }
}
