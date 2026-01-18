package gui.grafica.controllo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import gui.grafica.vista.CestinoDialog;
import gui.grafica.vista.ContentListaPanel;
import gui.grafica.vista.PannelloListe;
import modello.Articolo;
import modello.ListaDiArticoli;
import modello.exception.ListaDiArticoliException;

public class ControlloCestino implements ActionListener{
	
	private CestinoDialog vistaCestino;
    private ListaDiArticoli model;
    private ContentListaPanel vistaPrincipaleContenuto;
    private PannelloListe vistaPrincipale;

    public ControlloCestino(ListaDiArticoli model, ContentListaPanel vistaPrincipaleContenuto, PannelloListe vistaPrincipale) {
        this.model = model;
        this.vistaPrincipaleContenuto = vistaPrincipaleContenuto;
        this.vistaPrincipale = vistaPrincipale;
    }
    
    public void setVista(CestinoDialog vistaCestino) {
        this.vistaCestino = vistaCestino;
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = ((JButton) e.getSource()).getText();

        switch (comando) {
            case "Recupera" -> gestisciRecupera();
            case "Svuota Cestino" -> gestisciSvuotaCestino();
            default -> System.out.println("Comando non riconosciuto nel cestino: " + comando);
        }
        
        vistaCestino.aggiornaVista();   
        vistaPrincipaleContenuto.updateView();
        vistaPrincipale.aggiornaDati();
	}
	
	private void gestisciRecupera() {
		Articolo daRecuperare = vistaCestino.getArticoloSelezionato();
        if (daRecuperare == null) {
            JOptionPane.showMessageDialog(vistaCestino, "Seleziona un articolo da recuperare");
            return;
        }

        try {
            model.recuperaArticolo(daRecuperare);
        } catch (ListaDiArticoliException ex) {
            JOptionPane.showMessageDialog(vistaCestino, ex.getMessage());
        }
	}

	private void gestisciSvuotaCestino() {
		int conferma = JOptionPane.showConfirmDialog(vistaCestino, "Eliminare definitivamente tutto il cestino?");
		
        if (conferma == JOptionPane.YES_OPTION) 
            model.svuotaCancellati();
	}
}
