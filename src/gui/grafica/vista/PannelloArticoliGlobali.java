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
 * Vista che mostra il registro globale di tutti gli articoli presenti nel sistema.
 */
@SuppressWarnings("serial")
public class PannelloArticoliGlobali extends JPanel{
	private JTable tabellaArticoli;
    private DefaultTableModel tableModel;

    public PannelloArticoliGlobali(ControlloGestore controllo) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // Definizione delle colonne (Anagrafica globale)
        String[] colonne = {"Nome", "Categoria", "Prezzo (€)", "Nota"};
        
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Celle non modificabili direttamente in tabella
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
     * Ricarica la tabella leggendo dal registro statico di GestioneListe.
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
     * Identifica l'articolo selezionato nella tabella.
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
