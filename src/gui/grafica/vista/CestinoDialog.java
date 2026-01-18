package gui.grafica.vista;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import gui.grafica.controllo.ControlloCestino;
import modello.Articolo;
import modello.ListaDiArticoli;

@SuppressWarnings("serial")
public class CestinoDialog extends JDialog{
	private DefaultTableModel tableModel;
    private JTable tabella;
    private ListaDiArticoli model;

    public CestinoDialog(JFrame parent, ListaDiArticoli model, ControlloCestino controllo) {
        super(parent, "Cestino della lista: " + model.getNome(), true);
        this.model = model;
        
        setLayout(new BorderLayout());
        setSize(500, 400);
        setLocationRelativeTo(parent);

        // tabella per gli articoli cancellati
        String[] colonne = {"Nome", "Categoria", "Prezzo (€)", "Nota"};
        
        tableModel = new DefaultTableModel(colonne, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { 
            	return false; 
            }
        };
        
        tabella = new JTable(tableModel);
        tabella.setFillsViewportHeight(true);
        
        add(new JScrollPane(tabella), BorderLayout.CENTER);
        controllo.setVista(this);

        	// operazioni
	        JPanel OpsPanel = new JPanel();
	        
	        JButton btnRecupera = new JButton("Recupera");
	        btnRecupera.addActionListener(controllo);
	        OpsPanel.add(btnRecupera);
	        
	        JButton btnSvuota = new JButton("Svuota Cestino");
	        btnSvuota.addActionListener(controllo);
	        OpsPanel.add(btnSvuota);
	        
        add(OpsPanel, BorderLayout.NORTH);

        aggiornaVista();
        setVisible(true);
    }

    public void aggiornaVista() {
        tableModel.setRowCount(0);
        
        for (Articolo a : model.getArticoliCancellati()) {
            tableModel.addRow(new Object[]{
            		a.getNome(),
                    a.getCategoria(),
                    String.format("%.2f", a.getPrezzo()),
                    a.getNota()
            });
        }
    }
    
    public Articolo getArticoloSelezionato() {
        int riga = tabella.getSelectedRow();
        if (riga == -1) return null; 

        // Recuperiamo il nome dalla prima colonna (colonna 0)
        String nome = (String) tableModel.getValueAt(riga, 0);
        String categoria = (String) tableModel.getValueAt(riga, 1);

        // Cerchiamo l'articolo SOLO tra i cancellati (più veloce e preciso)
        for (Articolo a : model.getArticoliCancellati()) {
            if (a.getNome().equalsIgnoreCase(nome) && a.getCategoria().equalsIgnoreCase(categoria)) {
                return a;
            }
        }
        return null;
    }
}
