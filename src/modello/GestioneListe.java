package modello;

import java.util.ArrayList;
import java.util.List;

import modello.exception.GestioneListeException;

public class GestioneListe {
	// VARIABILI
	private static List<ListaDiArticoli> listeArticoli; 	//tutte le liste
	private static List<String> categorie; 				//tutte le categorie
	private static List<Articolo> articoli; 				//tutti gli articoli
	
	public static final String CATEGORIA_DEFAULT = "Non categorizzato";
	
	static {
		reset();	
	}
	
	// METODI
	
	// - Lista
	// insersci lista
	public static void inserisciLista(ListaDiArticoli list) throws GestioneListeException{
		if (list == null)
			throw new GestioneListeException("La lista non può essere nulla");
			
		if (trovaListaPerNome(list.getNome()) != null) 
            throw new GestioneListeException("Lista già presente");
		
		listeArticoli.add(list);
	}	
		
	// cancella lista
	public static void cancellaLista(String nome) throws GestioneListeException {
		if(nome== null ||nome.trim().isEmpty()) 
			throw new GestioneListeException("Il nome della lista non può essere vuoto");
		
		ListaDiArticoli listCanc = trovaListaPerNome(nome);
		
		if (listCanc == null)
			throw new GestioneListeException("Lista non trovata");
		
		listeArticoli.remove(listCanc);
	}
	
	
	public static ListaDiArticoli matchLista(String nome) throws GestioneListeException {
		if(nome== null ||nome.trim().isEmpty()) 
			throw new GestioneListeException("Il nome dellalista non può essere vuoto");
		
		ListaDiArticoli listaTrovata = trovaListaPerNome(nome);
        if (listaTrovata == null)
             throw new GestioneListeException("Lista non trovata");
        
        return listaTrovata;
	}
	
	// trova lista per nome
	private static ListaDiArticoli trovaListaPerNome(String nome) {
        if (nome == null) 
        		return null;
        
        for (ListaDiArticoli l : listeArticoli) {
            if (l.getNome().equals(nome)) {
                return l;
            }
        }
        return null; // Non trovata
    }

	// - Categoria
	// aggiunta categoria
	public static void inserisciCategoria(String nome) throws GestioneListeException {
		if(nome== null ||nome.trim().isEmpty()) 
			throw new GestioneListeException("Il nome della categoria non può essere vuoto");
		
		if(categorie.contains(nome)) 
			throw new GestioneListeException("Categoria già presente");
		
		categorie.add(nome);
	}
	
	public static void cancellaCategoria(String nome) throws GestioneListeException {
		if(nome== null ||nome.trim().isEmpty()) 
			throw new GestioneListeException("Il nome della categoria non può essere vuoto");
		
		if(!categorie.contains(nome)) 
			throw new GestioneListeException("Categoria non trovata");
		
		if(nome.equals("Non categorizzato"))
			throw new GestioneListeException("Non è possibile cancellare la categoria di default");
		
		categorie.remove(nome);
	}
	
	// esiste categoria
	public static boolean esisteCategoria(String nome) {
	    return categorie.contains(nome);
	}
	
	// - Articoli
	// aggiunta di un articolo
	public static void inserisciArticolo(Articolo a) throws GestioneListeException {
	    if (articoli.contains(a))
	        throw new GestioneListeException("Articolo già esistente");
	    
	    // È qui che il gestore controlla la categoria dell'articolo
	    String cat = a.getCategoria();
	    if (!esisteCategoria(cat)) {
	        inserisciCategoria(cat);
	    }
	    
	    articoli.add(a);
	}
	
	public static void cancellaArticolo(Articolo a) throws GestioneListeException {
		if(!articoli.contains(a))
			throw new GestioneListeException("Articolo non trovato");
		
		articoli.remove(a);
	}
	
	// - Reset
	public static void reset() {
		listeArticoli = new ArrayList<ListaDiArticoli>();
		categorie = new ArrayList<String>();
		articoli = new ArrayList<Articolo>();
		
		categorie.add(CATEGORIA_DEFAULT);
	}
}
