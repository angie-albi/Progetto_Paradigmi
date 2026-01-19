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
/**
 * La classe {@code CestinoDialog} rappresenta una finestra di dialogo modale per la visualizzazione
 * e la gestione degli articoli cancellati di una specifica lista
 * <p>
 * Questa interfaccia permette all'utente di:
 * <ul>
 *   <li>Visualizzare tutti gli articoli presenti nel cestino della lista</li>
 *   <li>Recuperare singoli articoli riportandoli nella lista attiva</li>
 *   <li>Svuotare completamente il cestino eliminando definitivamente tutti gli articoli</li>
 * </ul>
 * <p>
 * Il dialogo è modale, quindi blocca l'interazione con la finestra padre fino alla sua chiusura
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class CestinoDialog extends JDialog{
	/** Modello dei dati che definisce la struttura e il contenuto informativo della tabella. */
    private DefaultTableModel tableModel;

    /** Componente Swing dedicata alla rappresentazione grafica dei dati in formato tabellare. */
    private JTable tabella;

    /** Riferimento al modello di dominio contenente la logica e i dati reali degli articoli. */
    private ListaDiArticoli model;

    /**
     * Costruisce e visualizza il dialogo del cestino per una specifica lista.
     * Inizializza la tabella degli articoli cancellati, configura i pulsanti di controllo
     * e collega il controller per la gestione degli eventi
     * 
     * @param parent La finestra padre rispetto alla quale il dialogo viene centrato
     * @param model Il modello della lista di cui visualizzare gli articoli cancellati
     * @param controllo Il controller che gestisce le operazioni sul cestino (recupero e svuotamento)
     */
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

    /**
     * Aggiorna il contenuto della tabella sincronizzandola con lo stato corrente
     * degli articoli cancellati nel modello.
     * Svuota completamente la tabella e la ripopola con i dati aggiornati
     */
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
    
    /**
     * Identifica e restituisce l'articolo corrispondente alla riga attualmente selezionata
     * nella tabella del cestino
     * <p>
     * La ricerca viene effettuata confrontando nome e categoria (case-insensitive)
     * esclusivamente tra gli articoli presenti nella lista dei cancellati
     * 
     * @return L'oggetto {@code Articolo} selezionato, oppure {@code null} se non c'è alcuna selezione
     * o se l'articolo non viene trovato
     */
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
