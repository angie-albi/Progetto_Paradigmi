package modello;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


import modello.exception.ArticoloException;
import modello.exception.ListaDiArticoliException;

/**
 * La classe {@code ListaDiArticoli} gestisce una collezione di prodotti organizzata in due elenchi:
 * <ul>
 *   <li>Articoli attivi: i prodotti attualmente presenti nella lista della spesa</li>
 *   <li>Articoli cancellati: lo storico dei prodotti rimossi che possono essere recuperati</li>
 * </ul>
 * 
 * <p>La classe implementa {@code Iterable} per permettere di scorrere sequenzialmente 
 * tutti i prodotti (sia attivi che cancellati) con un unico ciclo.
 * 
 * @author Angie Albitres
 */
public class ListaDiArticoli implements Iterable<Articolo>{
	
	/** Nome identificativo della lista di articoli. */
    private String nome;

    /** Elenco dei prodotti attualmente attivi e presenti nella lista della spesa. */
    private List<Articolo> articoli;

    /** Storico dei prodotti rimossi (cestino) che possono essere recuperati. */
    private List<Articolo> articoliCancellati;
	
	/**
	 * Restituisce un iteratore che attraversa prima gli articoli attivi e poi quelli cancellati
	 * 
	 * @return Un'istanza di {@code IteratoreArticoli}
	 */
	@Override
	public Iterator<Articolo> iterator() {
		return new IteratoreArticoli();
	}
	
	/**
	 * Classe interna (privata) che implementa la logica di iterazione su due liste distinte
	 */
	private class IteratoreArticoli implements Iterator<Articolo>{
		/**
		 * Iteratore per la lista degli articoli attivi
		 */
		private final Iterator<Articolo> it1 = articoli.iterator();
		/**
		 * Iteratore per la lista degli articoli cancellati
		 */
        private final Iterator<Articolo> it2 = articoliCancellati.iterator();
        
		@Override
		public boolean hasNext() {
			return it1.hasNext() || it2.hasNext();
		}
		
		@Override
		public Articolo next() {
			if(it1.hasNext())
				return it1.next();
			
			if(it2.hasNext())
				return it2.next();
			
			throw new NoSuchElementException();
		}	
	}
	
	/**
	 * Crea una nuova lista articoli inizializzando i contenitori per i prodotti
	 * 
	 * @param nome Il nome della lista
	 * 
	 * @throws ListaDiArticoliException Viene lanciata se il nome della lista è nullo o vuoto
	 */
	public ListaDiArticoli(String nome) throws ListaDiArticoliException {
		if (nome == null || nome.isBlank()) {
            throw new ListaDiArticoliException("Il nome della lista non può essere vuoto");
        }
		
		this.nome = nome.trim();
		this.articoli = new ArrayList<Articolo>();
		this.articoliCancellati = new ArrayList<Articolo>();
	}
	
	/**
	 * Restituisce il nome della lista
	 * 
	 * @return Il nome della lista
	 */
	public String getNome() {
		return nome;
	}
	
	/**
	 * Restituisce la lista dei cancellati
	 * 
	 * @return La lista dei cancellati
	 */
	public List<Articolo> getArticoliCancellati() {
		return articoliCancellati;
	}
	
	/**
	 * Restituisce il numero di prodotti presenti nella lista attiva
	 * 
	 * @return Numero di articoli attivi
	 */
	public int numEl() {
		return articoli.size();
	}
	
	/**
	 * Restituisce il numero di prodotti presenti nella lista dei cancellati
	 * 
	 * @return Numero di articoli cancellati
	 */
	public int numElCanc() {
		return articoliCancellati.size();
	}
	
	/**
	 * Aggiunge un nuovo articolo alla lista attiva. Se il prodotto era tra i cancellati, lo ripristina
	 * 
	 * @param a L'oggetto {@code Articolo} da inserire
	 * 
	 * @throws ListaDiArticoliException Viene lanciata se l'articolo è già presente negli attivi
	 */
	public void inserisciArticolo(Articolo a) throws ListaDiArticoliException {
		if(articoli.contains(a))
			throw new ListaDiArticoliException("Articolo già presente");
		
		if(articoliCancellati.contains(a)) {
			articoliCancellati.remove(a);
		}
		
		articoli.add(a);
	}
	/**
	 * Crea e inserisce un nuovo articolo fornendo solo il nome
	 * 
	 * @param nome Nome del nuovo articolo
	 * 
	 * @throws ListaDiArticoliException In caso di duplicato
	 * @throws ArticoloException In caso di parametri non validi
	 */
	public void inserisciArticolo(String nome) throws ListaDiArticoliException, ArticoloException{
		Articolo nuovoArticolo = new Articolo(nome);
		inserisciArticolo(nuovoArticolo);
	}
	/**
	 * Crea e inserisce un nuovo articolo fornendo solo il nome e la categoria
	 * 
	 * @param nome Nome del nuovo articolo
	 * @param categoria Categoria del nuovo articolo
	 * 
	 * @throws ListaDiArticoliException In caso di duplicato
	 * @throws ArticoloException In caso di parametri non validi
	 */
	public void inserisciArticolo(String nome, String categoria) throws ListaDiArticoliException, ArticoloException {
		inserisciArticolo(new Articolo(nome, categoria));
	}
	/**
	 * Crea e inserisce un nuovo articolo fornendo nome, categoria e prezzo
	 * 
	 * @param nome Nome del nuovo articolo
	 * @param categoria Categoria del nuovo articolo
	 * @param prezzo Prezzo del nuovo articolo
	 * 
	 * @throws ListaDiArticoliException In caso di duplicato
	 * @throws ArticoloException In caso di parametri non validi
	 */
	public void inserisciArticolo(String nome, String categoria, double prezzo) throws ListaDiArticoliException, ArticoloException {
		inserisciArticolo(new Articolo(nome, categoria, prezzo));
	}
	/**
	 * Crea e inserisce un nuovo articolo fornendo nome, categoria e prezzo
	 * 
	 * @param nome Nome del nuovo articolo
	 * @param categoria Categoria del nuovo articolo
	 * @param prezzo Prezzo del nuovo articolo
	 * @param nota Nota aggiuntiva del nuovo articolo
	 * 
	 * @throws ListaDiArticoliException In caso di duplicato
	 * @throws ArticoloException In caso di parametri non validi
	 */
	public void inserisciArticolo(String nome, String categoria, double prezzo, String nota) throws ListaDiArticoliException, ArticoloException{
		inserisciArticolo(new Articolo(nome, categoria, prezzo, nota));
	}
	
	/**
	 * Cerca articoli il cui nome inizia con il prefisso indicato, sia tra gli attivi che tra i cancellati
	 * 
	 * @param prefisso Stringa da cercare all'inizio del nome
	 * 
	 * @return Una lista di articoli che corrispondono al criterio di ricerca
	 */
	public List<Articolo> ricercaArticolo(String prefisso){
		List<Articolo> ris = new ArrayList<Articolo>();
		
		if(prefisso == null)
			return ris;
		
		// normalizzazione prefisso
		prefisso = prefisso.toLowerCase();
		
		for(Articolo a: this) 
			if(a.getNome().toLowerCase().startsWith(prefisso))
				ris.add(a);
		return ris;
	}
	
	/**
	 * Sposta un articolo dalla lista attiva a quella dei cancellati
	 * 
	 * @param a L'articolo da rimuovere
	 * 
	 * @throws ListaDiArticoliException Viene lanciata se l'articolo non è presente negli attivi
	 */
	public void cancellaArticolo(Articolo a) throws ListaDiArticoliException {
		if(articoli.contains(a)) {
			articoli.remove(a);
			articoliCancellati.add(a);
		}
		else {
			throw new ListaDiArticoliException("Articolo non presente nella lista, è impossibile rimuoverlo");
		}
	}
	
	/**
	 * Ripristina un articolo dalla lista dei cancellati riportandolo in quella attiva
	 * 
	 * @param a L'articolo da recuperare
	 * 
	 * @throws ListaDiArticoliException Viene lanciata se l'articolo non è tra i cancellati o se la lista dei cancellati è vuota
	 */
	public void recuperaArticolo(Articolo a) throws ListaDiArticoliException{
		if(articoliCancellati.isEmpty())
			throw new ListaDiArticoliException("La lista dei cancellati è vuota");
		
		if(articoliCancellati.contains(a)) {
			articoliCancellati.remove(a);
			this.inserisciArticolo(a); 
		}
		else {
			throw new ListaDiArticoliException("Articolo non presente nei cancellati, è impossibile recuperarlo");
		}
	}
	
	/**
	 * Svuota definitivamente la lista degli articoli cancellati
	 */
	public void svuotaCancellati() {
		articoliCancellati.clear();
	}
	
	/**
	 * Calcola la somma dei prezzi di tutti i prodotti presenti nella lista attiva
	 * 
	 * @return Il prezzo totale degli articoli attivi
	 */
	public double calcoloPrezzoTotale(){
		double prezzoTotale = 0;
		
		if (articoli.isEmpty()) 
			return 0.0;
		
		for(Articolo a: articoli) {
			prezzoTotale += a.getPrezzo();
		}
		
		return prezzoTotale;
	}
	
	/**
	 * Rimuove definitivamente un articolo da entrambe le liste (attivi e cancellati)
	 * 
	 * @param a L'articolo da rimuovere definitivamente da entrambe le liste
	 */
	public void rimuoviCompletamente(Articolo a) {
	    this.articoli.remove(a);
	    this.articoliCancellati.remove(a);
	}
	
	/**
	 * Fornisce una rappresentazione testuale della lista e dei suoi articoli
	 * 
	 * @return Stringa contenente i dettagli della lista
	 */
	@Override
	public String toString() {
		return "ListaDiArticoli [nome=" + nome + ", articoli=" + articoli + ", articoliCancellati=" + articoliCancellati
				+ "]";
	}
}
