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
import modello.GestioneListe;
import modello.ListaDiArticoli;

/**
 * La classe {@code PannelloListe} rappresenta l'interfaccia utente per la gestione e visualizzazione
 * delle diverse liste di articoli presenti nel sistema
 * <p>
 * Questo pannello permette di visualizzare un riepilogo delle liste create in formato tabellare,
 * indicando per ciascuna il nome, il numero di articoli attivi e quelli presenti nel cestino.
 * Offre inoltre i comandi necessari per la creazione di nuove liste, l'apertura di una lista
 * esistente o la sua eliminazione definitiva
 * 
 * @author Angie Albitres
 */
@SuppressWarnings("serial")
public class PannelloListe extends JPanel {
	private JTable tabellaListe;
	private DefaultTableModel tableModel;

	/**
	 * Crea un nuovo {@code PannelloListe} inizializzando la tabella e i relativi comandi.
	 * Configura il layout e collega i bottoni al controller
	 * 
	 * @param controllo Il controller che gestisce la logica delle operazioni sulle liste
	 */
	public PannelloListe(ControlloGestore controllo) {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

		String[] colonne = {"Nome Lista", "Articoli", "Cestino"};
		tableModel = new DefaultTableModel(colonne, 0) {
			
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tabellaListe = new JTable(tableModel);
		tabellaListe.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(new JScrollPane(tabellaListe), BorderLayout.CENTER);

		// pannello Bottoni
		JPanel bottoni = new JPanel();
		JButton btnCrea = new JButton("Nuova Lista");
		JButton btnApri = new JButton("Apri Selezionata");
		JButton btnElimina = new JButton("Elimina Lista");

		// assegnazione del controller ai bottoni
		btnCrea.addActionListener(controllo);
		btnApri.addActionListener(controllo);
		btnElimina.addActionListener(controllo);

		bottoni.add(btnCrea);
		bottoni.add(btnApri);
		bottoni.add(btnElimina);
		add(bottoni, BorderLayout.NORTH);

		aggiornaDati();
	}

	/**
	 * Sincronizza la visualizzazione della tabella con i dati correnti presenti nel sistema.
	 * Svuota il modello attuale e lo ripopola con l'elenco aggiornato delle liste, 
	 * includendo i conteggi degli articoli attivi e di quelli nel cestino per ciascuna lista.
	 */
	public void aggiornaDati() {
		tableModel.setRowCount(0);
		for (ListaDiArticoli l : GestioneListe.getListeArticoli()) {
			Object[] riga = {
				l.getNome(),
				l.numEl(),      
				l.numElCanc()   
			};
			tableModel.addRow(riga);
		}
	}

	/**
	 * Recupera il nome della lista attualmente selezionata dall'utente nella tabella
	 * 
	 * @return Il nome della lista selezionata come {@code String}, 
	 * oppure {@code null} se non è presente alcuna selezione.
	 */
	public String getListaSelezionata() {
		int riga = tabellaListe.getSelectedRow();
		if (riga == -1) 
			return null;
		
		return (String) tableModel.getValueAt(riga, 0);
	}
}