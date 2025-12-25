package modello;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;


import modello.exception.ArticoloException;
import modello.exception.GestioneListeException;
import modello.exception.ListaDiArticoliException;

/**
 * Gestisce una collezione di articoli organizzata in una lista attiva e una dei cancellati
 * * @author Il Tuo Nome
 */
public class ListaDiArticoli implements Iterable<Articolo>{
	
	// VARIABILI
	private String nome;
	private List<Articolo> articoli;
	private List<Articolo> articoliCancellati;
	
	// ITERATORE
	@Override
	public Iterator<Articolo> iterator() {
		return new IteratoreArticoli();
	}
	
	private class IteratoreArticoli implements Iterator<Articolo>{
		private final Iterator<Articolo> it1 = articoli.iterator();
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
	 * Crea una nuova lista articoli inizializzando le strutture dati interne
	 * * @param nome Il nome identificativo della lista
	 * @throws ListaDiArticoliException Lanciata se il nome della lista è vuoto o nullo
	 */
	// COSTRUTTORE
	public ListaDiArticoli(String nome) throws ListaDiArticoliException {
		if (nome == null || nome.trim().isEmpty()) {
            throw new ListaDiArticoliException("Il nome della lista non può essere vuoto");
        }
		
		this.nome = nome;
		this.articoli = new ArrayList<Articolo>();
		this.articoliCancellati = new ArrayList<Articolo>();
	}
	
	// METODI
	// Getter di Nome
	public String getNome() {
		return nome;
	}
	
	// Numero elementi nella lista
	public int numEl() {
		return articoli.size();
	}
	
	//Numero elementi cancellati
	public int numElCanc() {
		return articoliCancellati.size();
	}
	
	/**
	 * Inserisce un articolo nella lista. Se era tra i cancellati, lo riporta tra gli attivi
	 * * @param a L'oggetto articolo da aggiungere
	 * @throws ListaDiArticoliException Lanciata se l'articolo è già presente nella lista attiva
	 */
	public void inserisciArticolo(Articolo a) throws ListaDiArticoliException {
		if(articoli.contains(a))
			throw new ListaDiArticoliException("Annuncio già presente");
		
		if(articoliCancellati.contains(a)) {
			articoliCancellati.remove(a);
		}
		
		articoli.add(a);
	}
	
	public void inserisciArticolo(String nome) throws ListaDiArticoliException, ArticoloException, GestioneListeException {
		inserisciArticolo(new Articolo(nome));
	}
	
	public void inserisciArticolo(String nome, String categoria) throws ArticoloException, ListaDiArticoliException, GestioneListeException {
		inserisciArticolo(new Articolo(nome, categoria));
	}
	
	public void inserisciArticolo(String nome, String categoria, double prezzo) throws ArticoloException, ListaDiArticoliException, GestioneListeException {
		inserisciArticolo(new Articolo(nome, categoria, prezzo));
	}
	
	public void inserisciArticolo(String nome, String categoria, double prezzo, String nota) throws ArticoloException, ListaDiArticoliException, GestioneListeException {
		inserisciArticolo(new Articolo(nome, categoria, prezzo, nota));
	}
	
	// Ricerca Articolo: sia nella lista che nei cancellati
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
	 * Rimuove un articolo dalla lista attiva e lo sposta in quella dei cancellati
	 * * @param a L'articolo da rimuovere
	 * @throws ListaDiArticoliException Lanciata se l'articolo non è presente nella lista
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
	 * Recupera un articolo precedentemente rimosso riportandolo nella lista attiva
	 * * @param a L'articolo da ripristinare
	 * @throws ListaDiArticoliException Lanciata se l'articolo non si trova tra i cancellati
	 */
	public void recuperaArticolo(Articolo a) throws ListaDiArticoliException{
		if(articoliCancellati.isEmpty())
			throw new ListaDiArticoliException("La lista dei cancellati è vuota");
		
		if(articoliCancellati.contains(a)) {
			articoliCancellati.remove(a);
			this.inserisciArticolo(a); // sono presenti i controlli necessari
		}
		else {
			throw new ListaDiArticoliException("Articolo non presente nei cancellati, è impossibile recuperarlo");
		}
	}
	
	// Svuota Lista Cancellati 
	public void svuotaCancellati() {
		articoliCancellati.clear();
	}
	
	// Calcolo prezzo totale
	public double calcoloPrezzoTotale(){
		double prezzoTotale = 0;
		
		if (articoli.isEmpty()) 
			return 0.0;
		
		for(Articolo a: articoli) {
			prezzoTotale += a.getPrezzo();
		}
		
		return prezzoTotale;
	}
	
	// toString
	@Override
	public String toString() {
		return "ListaDiArticoli [nome=" + nome + ", articoli=" + articoli + ", articoliCancellati=" + articoliCancellati
				+ "]";
	}
	
}
