package modello;

import modello.exception.ArticoloException;

/**
 * La classe {@code Articolo} rappresenta un singolo articolo della lista della spesa con i suoi dettagli
 * <p> Per ogni articolo viene memorizzato:
 * <ul>
 *   <li>un nome univoco ({@code nome})</li>
 *   <li>una categoria</li>
 *   <li>un prezzo</li>
 *   <li>una nota</li>
 * </ul>
 * 
 * <p>Due prodotti sono considerati uguali se hanno lo stesso nome e la stessa categoria (senza distinzione tra maiuscole e minuscole)
 * 
 * @author Angie Albitres
 */
public class Articolo{

	/**
     * Nome dell'articolo, è unico e lo identifica dagli altri
     */
	private String nome;
	/**
	 * Categoria dell'articolo
	 */
	private String categoria;
	/**
	 * Prezzo dell'articolo
	 */
	private double prezzo;
	/**
	 * Nota aggiuntiva che descrive l'articolo
	 */
	private String nota;
	
	/**
	 * Etichetta utilizzata quando un articolo non viene associato a nessuna categoria specifica.
	 */
	public static final String CATEGORIA_DEFAULT = "Non categorizzato";
	
	/**
	 * Crea un nuovo {@code Articolo} completo di tutte le sue informazioni
	 * 
	 * @param nome Il nome dell'articolo 
	 * @param categoria La categoria dell'articolo
	 * @param prezzo Il costo dell'articolo
	 * @param nota Annotazioni aggiuntive
	 * 
	 * @throws ArticoloException Viene lanciata se il nome è mancante o se il prezzo è negativo 
	 */
	public Articolo(String nome, String categoria, double prezzo, String nota) throws ArticoloException{
	    if(nome == null || nome.isBlank())
	    		throw new ArticoloException("Il nome non può essere vuoto");
	    
		this.nome = nome.trim(); 
		this.setCategoria(categoria);
		this.setPrezzo(prezzo);
		this.setNota(nota);
	}
	
	/**
	 * Crea un nuovo {@code Articolo} senza la nota, che di default sara' vuota
	 * 
	 * @param nome Il nome dell'articolo 
	 * @param categoria La categoria dell'articolo
	 * @param prezzo Il costo dell'articolo
	 * 
	 * @throws ArticoloException Viene lanciata se il nome è mancante o se il prezzo è negativo 
	 */
	public Articolo(String nome, String categoria, double prezzo) throws ArticoloException {
		this(nome, categoria, prezzo, "");
	}
	
	/**
	 * Crea un nuovo {@code Articolo} solo con il nome e la categoria, di default il prezzo e' 0
	 * 
	 * @param nome Il nome dell'articolo 
	 * @param categoria La categoria dell'articolo
	 * 
	 * @throws ArticoloException Viene lanciata se il nome è mancante o se il prezzo è negativo 
	 */
	public Articolo(String nome, String categoria) throws ArticoloException {
		this(nome, categoria, 0, "");
	}
	
	/**
	 * Crea un nuovo {@code Articolo} solo con il nome, la categoria di default sara' Non categorizzato
	 * 
	 * @param nome Il nome dell'articolo 
	 * 
	 * @throws ArticoloException Viene lanciata se il nome è mancante
	 */
	public Articolo(String nome) throws ArticoloException {
		this(nome, CATEGORIA_DEFAULT, 0, "");
	}
	
	/**
	 * Restituisce il nome univoco dell'articolo
	 * 
	 * @return Il nome dell'articolo
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Restituisce la categoria dell'articolo
	 * 
	 * @return La categoria dell'articolo
	 */
	public String getCategoria() {
		return categoria;
	}
	/**
	 * Modifica la categoria dell'articolo
	 * La categoria non può essere nulla o vuota
	 * 
	 * @param categoria Nuova categoria dell'articolo
	 */
	public void setCategoria(String categoria){
		if(categoria==null || categoria.isBlank()) {
			categoria = CATEGORIA_DEFAULT;
		}
		
		this.categoria = categoria;
	}

	/**
	 * Restituisce il prezzo dell'articolo
	 * 
	 * @return Il prezzo dell'articolo
	 */
	public double getPrezzo() {
		return prezzo;
	}
	/**
	 * Modifica il prezzo dell'articolo
	 * 
	 * @param prezzo Nuovo prezzo dell'articolo
	 * @throws ArticoloException Viene lanciata se il prezzo è negativo 
	 */
	public void setPrezzo(double prezzo) throws ArticoloException {
		this.prezzo = this.validaPrezzo(prezzo);
	}

	/**
	 * Restituisce la nota dell'articolo
	 * 
	 * @return Nota dell'articolo
	 */
	public String getNota() {
		return nota;
	}
	/**
	 * Modifica la nota
	 * La nota non può essere nulla o vuota
	 * 
	 * @param nota Nuova nota dell'articolo
	 */
	public void setNota(String nota) {
		if(nota==null || nota.isBlank()) {
			nota = "";
		}
		
		this.nota = nota;
	}
	
	/**
	 * Controlla che il prezzo sia valido
	 * 
	 * @param prezzo Prezzo da validare
	 * @return Il prezzo se è valido
	 * @throws ArticoloException  Viene lanciata se il prezzo è negativo 
	 */
	private double validaPrezzo(double prezzo) throws ArticoloException {
		if (prezzo < 0) {
			throw new ArticoloException("Il prezzo non può essere negativo");
        }
        return prezzo;
    }

	/**
	 * Confronta l'articolo corrente con un altro oggetto per verificarne l'uguaglianza
	 * Due articoli sono considerati uguali se hanno lo stesso nome e la stessa categoria (senza distinzione tra maiuscole e minuscole)
	 * 
	 * @param obj L'oggetto da confrontare
	 * @return true se gli oggetti sono uguali, false altrimenti
	 */
	@Override
	public boolean equals(Object obj) {
	    if (this == obj) 
	    	return true;
	    
	    if (obj == null || getClass() != obj.getClass()) 
	    	return false;
	    
	    Articolo articolo = (Articolo) obj;
	    
	    return nome.equalsIgnoreCase(articolo.nome)&& categoria.equalsIgnoreCase(articolo.categoria);
	}
	
	/**
	 * Restituisce una descrizione testuale dell'articolo
	 * @return Una stringa con i dati dell'articolo
	 */
	@Override
	public String toString() {
		return "\n Articolo [nome=" + nome +", categoria=" + categoria + ", prezzo=" + prezzo + ", nota=" + nota + "]";
	}
}
