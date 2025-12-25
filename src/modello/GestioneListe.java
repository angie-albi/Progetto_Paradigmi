package modello;

import java.util.ArrayList;
import java.util.List;

import modello.exception.GestioneListeException;

/**
 * La classe {@code GestioneListe} funge da centro di controllo statico per l'intero sistema
 * <p> Gestisce centralmente:
 * <ul>
 *   <li>L'elenco globale di tutte le liste create ({@code listeArticoli})</li>
 *   <li>L'anagrafica di tutte le categorie merceologiche disponibili</li>
 *   <li>Il registro di tutti gli articoli inseriti nel sistema</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
public class GestioneListe {
	/**
	 * Elenco di tutte le liste di articoli gestite dal sistema
	 */
	private static List<ListaDiArticoli> listeArticoli; 
	/**
	 * Elenco delle categorie merceologiche definite dall'utente
	 */
	private static List<String> categorie; 
	/**
	 * Registro globale di tutti gli articoli esistenti nel sistema
	 */
	private static List<Articolo> articoli; 
	
	/**
	 * Nome della categoria predefinita assegnata agli articoli non categorizzati
	 */
	public static final String CATEGORIA_DEFAULT = "Non categorizzato";
	
	static {
		reset();	
	}
	/**
	 * Ripristina lo stato iniziale del sistema creando nuove liste vuote
	 * La categoria di default viene aggiunta automaticamente
	 */
	public static void reset() {
		listeArticoli = new ArrayList<ListaDiArticoli>();
		categorie = new ArrayList<String>();
		articoli = new ArrayList<Articolo>();
		
		categorie.add(CATEGORIA_DEFAULT);
	}

	/**
	 * Aggiunge una nuova lista al sistema verificando che non ne esista già una con lo stesso nome
	 * 
	 * @param list L'oggetto {@code ListaDiArticoli} da inserire
	 * 
	 * @throws GestioneListeException Viene lanciata se la lista è nulla o se il nome è già presente
	 */
	public static void inserisciLista(ListaDiArticoli list) throws GestioneListeException{
		if (list == null)
			throw new GestioneListeException("La lista non può essere nulla");
			
		if (trovaListaPerNome(list.getNome()) != null) 
            throw new GestioneListeException("Lista già presente");
		
		listeArticoli.add(list);
	}	
		
	/**
	 * Rimuove definitivamente una lista dal sistema cercandola per nome
	 * 
	 * @param nome Il nome della lista da eliminare
	 * 
	 * @throws GestioneListeException Viene lanciata se il nome è vuoto o se la lista non viene trovata
	 */
	public static void cancellaLista(String nome) throws GestioneListeException {
		if(nome== null ||nome.isBlank()) 
			throw new GestioneListeException("Il nome della lista non può essere vuoto");
		
		ListaDiArticoli listCanc = trovaListaPerNome(nome);
		
		if (listCanc == null)
			throw new GestioneListeException("Lista non trovata");
		
		listeArticoli.remove(listCanc);
	}
	
	/**
	 * Ricerca e restituisce una lista specifica tramite il suo nome
	 * 
	 * @param nome Il nome della lista da cercare
	 * 
	 * @return L'oggetto {@code ListaDiArticoli} corrispondente
	 * 
	 * @throws GestioneListeException Viene lanciata se il nome è vuoto o se la lista non esiste
	 */
	public static ListaDiArticoli matchLista(String nome) throws GestioneListeException {
		if(nome== null ||nome.isBlank()) 
			throw new GestioneListeException("Il nome della lista non può essere vuoto");
		
		ListaDiArticoli listaTrovata = trovaListaPerNome(nome);
        if (listaTrovata == null)
             throw new GestioneListeException("Lista non trovata");
        
        return listaTrovata;
	}
	/**
	 * Metodo interno per la ricerca di una lista nell'elenco statico
	 * 
	 * @param nome Nome della lista da trovare
	 * 
	 * @return Il riferimento alla lista se trovata, {@code null} altrimenti
	 */
	private static ListaDiArticoli trovaListaPerNome(String nome) {
        if (nome == null) 
        		return null;
        
        for (ListaDiArticoli l : listeArticoli) {
            if (l.getNome().equalsIgnoreCase(nome)) {
                return l;
            }
        }
        return null; 
    }

	/**
	 * Registra una nuova categoria nel sistema
	 * 
	 * @param nome Il nome della categoria da aggiungere
	 * 
	 * @throws GestioneListeException Viene lanciata se il nome è vuoto o se la categoria esiste già
	 */
	public static void inserisciCategoria(String nome) throws GestioneListeException {
		if(nome== null ||nome.isBlank()) 
			throw new GestioneListeException("Il nome della categoria non può essere vuoto");
		
		if(categorie.contains(nome)) 
			throw new GestioneListeException("Categoria già presente");
		
		categorie.add(nome);
	}
	
	/**
	 * Rimuove una categoria dall'anagrafica
	 * Non è permesso rimuovere la categoria di default
	 * 
	 * @param nome Il nome della categoria da eliminare
	 * 
	 * @throws GestioneListeException Viene lanciata se la categoria è quella di default o se non esiste
	 */
	public static void cancellaCategoria(String nome) throws GestioneListeException {
		if(nome== null ||nome.isBlank()) 
			throw new GestioneListeException("Il nome della categoria non può essere vuoto");
		
		if(!categorie.contains(nome)) 
			throw new GestioneListeException("Categoria non trovata");
		
		if(nome.equals(CATEGORIA_DEFAULT))
			throw new GestioneListeException("Non è possibile cancellare la categoria di default");
		
		categorie.remove(nome);
	}
	
	/**
	 * Verifica la presenza di una categoria nell'anagrafica di sistema
	 * 
	 * @param nome Il nome della categoria da controllare
	 * 
	 * @return true se la categoria esiste, false altrimenti
	 */
	public static boolean esisteCategoria(String nome) {
	    return categorie.contains(nome);
	}
	
	/**
	 * Inserisce un nuovo articolo nel registro globale
	 * Se la categoria dell'articolo non esiste, viene creata automaticamente
	 * 
	 * @param a L'oggetto {@code Articolo} da registrare
	 * 
	 * @throws GestioneListeException Viene lanciata se l'articolo è già presente nel registro
	 */
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
	
	/**
	 * Rimuove un articolo dal registro globale del sistema
	 * 
	 * @param a L'articolo da eliminare
	 * 
	 * @throws GestioneListeException Viene lanciata se l'articolo non è presente nel registro
	 */
	public static void cancellaArticolo(Articolo a) throws GestioneListeException {
		if(!articoli.contains(a))
			throw new GestioneListeException("Articolo non trovato");
		
		articoli.remove(a);
	}
}