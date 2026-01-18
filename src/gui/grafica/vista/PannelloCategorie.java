package gui.grafica.vista;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gui.grafica.controllo.ControlloGestore;
import modello.GestioneListe;

/**
 * Vista per la gestione delle categorie globali.
 */
@SuppressWarnings("serial")
public class PannelloCategorie extends JPanel {
	private JList<String> listaCategorie;
    private DefaultListModel<String> listModel;

    public PannelloCategorie(ControlloGestore controllo) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        // Modello della lista per visualizzare le categorie
        listModel = new DefaultListModel<>();
        listaCategorie = new JList<>(listModel);
        add(new JScrollPane(listaCategorie), BorderLayout.CENTER);

        // Pannello per i bottoni di operazione
        JPanel bottoni = new JPanel();
        JButton btnAggiungi = new JButton("Aggiungi Categoria");
        JButton btnElimina = new JButton("Elimina Categoria");

        // Assegniamo il controller ai bottoni
        btnAggiungi.addActionListener(controllo);
        btnElimina.addActionListener(controllo);

        bottoni.add(btnAggiungi);
        bottoni.add(btnElimina);
        add(bottoni, BorderLayout.NORTH);

        // Caricamento iniziale dei dati
        aggiornaDati();
    }

    /**
     * Sincronizza la JList con i dati presenti nel modello GestioneListe
     */
    public void aggiornaDati() {
        listModel.clear();
        for (String cat : GestioneListe.getCategorie()) {
            listModel.addElement(cat);
        }
    }

    /**
     * Restituisce il nome della categoria selezionata nella lista
     */
    public String getCategoriaSelezionata() {
        return listaCategorie.getSelectedValue();
    }
}
