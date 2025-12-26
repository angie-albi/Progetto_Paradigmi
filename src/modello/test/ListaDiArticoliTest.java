package modello.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modello.Articolo;
import modello.ListaDiArticoli;
import modello.exception.ArticoloException;
import modello.exception.ListaDiArticoliException;

/**
 * La classe {@code ListaDiArticoliTest} contiene i test unitari per la classe {@code ListaDiArticoli}
 * <p> Vengono verificati i seguenti aspetti:
 * <ul>
 *   <li>Inizializzazione corretta della lista e validazione del nome</li>
 *   <li>Gestione dell'inserimento (anche tramite overload) e dei duplicati</li>
 *   <li>Spostamento degli articoli tra lista attiva e cancellati</li>
 *   <li>Funzionamento dell'iteratore personalizzato e della ricerca globale</li>
 *   <li>Calcolo del prezzo totale degli articoli attivi</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
class ListaDiArticoliTest {

	ListaDiArticoli l1, l2, l3;
	
	/**
	 * Configura l'ambiente di test creando diverse istanze di liste prima di ogni esecuzione
	 * 
	 * @throws ListaDiArticoliException In caso di nomi lista non validi
	 */
	@BeforeEach
	void setUp() throws ListaDiArticoliException {
		l1 = new ListaDiArticoli("Spesa");
		l2 = new ListaDiArticoli("Regali");
		l3 = new ListaDiArticoli("Lavoro");
	}
	
	/**
	 * Verifica che il costruttore assegni il nome e sollevi eccezioni per input non validi
	 */
	@Test
	void testCostruttore() {
		assertEquals("Spesa", l1.getNome());
		assertEquals("Regali", l2.getNome());
		
		assertThrows(ListaDiArticoliException.class, () -> {
			new ListaDiArticoli("");
		});
		
		assertThrows(ListaDiArticoliException.class, () -> {
			new ListaDiArticoli(null);
		});
	}
	
	/**
	 * Verifica il corretto inserimento di articoli tramite tutti i metodi sovraccaricati
	 */
	@Test
	void testInserisciArticoli() {
		assertDoesNotThrow(() -> {
			l1.inserisciArticolo(new Articolo("Latte"));
			l1.inserisciArticolo("Pane", "Cibo");
			l1.inserisciArticolo("Vino", "Bevande", 10);
			l1.inserisciArticolo("Acqua", "Bevande", 1, "Naturale");
		});
		
		assertEquals(4, l1.numEl());
		
		assertThrows(ListaDiArticoliException.class, () -> {
			l1.inserisciArticolo("Latte");
		});
	}

	/**
	 * Verifica la corretta rimozione di un articolo e il suo spostamento nei cancellati
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException Se l'articolo non è presente o è già rimosso
	 */
	@Test
	void testCancellaArticolo() throws ArticoloException, ListaDiArticoliException {
		riempiLista(l1); 
		Articolo a = l1.ricercaArticolo("Latte").get(0);
		
		assertDoesNotThrow(() -> {
			l1.cancellaArticolo(a);
		});
		
		assertEquals(12.00, l1.calcoloPrezzoTotale(), 0.001);
		assertEquals(1, l1.numElCanc());
		
		assertThrows(ListaDiArticoliException.class, () -> {
			l1.cancellaArticolo(a);
		});
		
		assertThrows(ListaDiArticoliException.class, () -> {
			l1.cancellaArticolo(new Articolo("NonEsiste"));
		});
	}
	
	/**
	 * Verifica il ripristino di un articolo dalla lista dei cancellati
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException Se l'articolo non è presente nei cancellati
	 */
	@Test
	void testRecuperaArticolo() throws ArticoloException, ListaDiArticoliException {
		riempiLista(l1);
		Articolo a = l1.ricercaArticolo("Latte").get(0);
		
		l1.cancellaArticolo(a);
		assertEquals(12.00, l1.calcoloPrezzoTotale(), 0.001);
		
		assertDoesNotThrow(() -> {
			l1.recuperaArticolo(a);
		});
		
		assertEquals(13.50, l1.calcoloPrezzoTotale(), 0.001);
		assertEquals(0, l1.numElCanc());
		
		assertThrows(ListaDiArticoliException.class, () -> {
			l1.recuperaArticolo(a);
		});
		
		assertThrows(ListaDiArticoliException.class, () -> {
			l1.recuperaArticolo(new Articolo("MaiEsistito"));
		});
	}
	
	/**
	 * Verifica la ricerca di articoli per prefisso ignorando la distinzione tra maiuscole e minuscole
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException In caso di errori nella lista
	 */
	@Test
	void testRicercaArticolo() throws ArticoloException, ListaDiArticoliException {
		riempiLista(l1);
		
		List<Articolo> ris = l1.ricercaArticolo("la");
		assertEquals(1, ris.size());
		assertEquals("Latte", ris.get(0).getNome());
		
		ris = l1.ricercaArticolo("PANE");
		assertEquals(1, ris.size());
		
		ris = l1.ricercaArticolo("z");
		assertEquals(0, ris.size());
		
		ris = l1.ricercaArticolo("");
		assertEquals(3, ris.size());
		
		ris = l1.ricercaArticolo(null);
		assertEquals(0, ris.size());
	}
	
	/**
	 * Verifica che la ricerca trovi gli articoli anche quando si trovano nella lista dei cancellati
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException In caso di errori nella lista
	 */
	@Test
	void testRicercaInclusiCancellati() throws ArticoloException, ListaDiArticoliException {
	    riempiLista(l1);
	    Articolo a = l1.ricercaArticolo("Latte").get(0);
	    l1.cancellaArticolo(a); 
	    
	    List<Articolo> ris = l1.ricercaArticolo("Latte");
	    assertEquals(1, ris.size());
	    assertEquals("Latte", ris.get(0).getNome());
	}
	
	/**
	 * Verifica che il calcolo del prezzo totale consideri solo gli articoli attualmente attivi
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException In caso di errori nella lista
	 */
	@Test
	void testCalcoloPrezzoTotale() throws ArticoloException, ListaDiArticoliException {
		assertEquals(0.0, l1.calcoloPrezzoTotale(), 0.001);
		
		riempiLista(l1);
		assertEquals(13.50, l1.calcoloPrezzoTotale(), 0.001);
		
		l1.cancellaArticolo(l1.ricercaArticolo("Vino").get(0));
		assertEquals(3.50, l1.calcoloPrezzoTotale(), 0.001);
	}
	
	/**
	 * Verifica la cancellazione definitiva degli articoli dalla lista dei rimossi
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException In caso di errori nella lista
	 */
	@Test
	void testSvuotaCancellati() throws ArticoloException, ListaDiArticoliException {
		riempiLista(l1);
		Articolo a = l1.ricercaArticolo("Latte").get(0);
		l1.cancellaArticolo(a);
		
		l1.svuotaCancellati();
		assertEquals(0, l1.numElCanc());
		
		assertThrows(ListaDiArticoliException.class, () -> {
			l1.recuperaArticolo(a);
		});
	}
	
	/**
	 * Verifica che l'inserimento di un articolo già cancellato lo ripristini automaticamente tra gli attivi
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException In caso di errori nella lista
	 */
	@Test
	void testInserimentoArticoloGiaCancellato() throws ArticoloException, ListaDiArticoliException {
	    riempiLista(l1);
	    Articolo a = l1.ricercaArticolo("Latte").get(0);
	    l1.cancellaArticolo(a);
	    
	    assertDoesNotThrow(() -> {
	    	l1.inserisciArticolo(a);
	    });
	    
	    assertEquals(3, l1.numEl()); 
	    assertEquals(0, l1.numElCanc());
	    assertEquals(13.50, l1.calcoloPrezzoTotale(), 0.001);
	}
	
	/**
	 * Verifica che l'iteratore attraversi correttamente sia gli articoli attivi che i cancellati
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException In caso di errori nella lista
	 */
	@Test
	void testIteratoreContenuto() throws ArticoloException, ListaDiArticoliException {
	    riempiLista(l1);
	    Articolo a = l1.ricercaArticolo("Latte").get(0);
	    l1.cancellaArticolo(a); 
	    
	    boolean trovatoLatte = false;
	    boolean trovatoPane = false;
	    
	    int conta = 0;
	    for(Articolo art : l1) {
	    	conta++;
	        if(art.getNome().equals("Latte")) trovatoLatte = true;
	        if(art.getNome().equals("Pane")) trovatoPane = true;
	    }
	    
	    assertEquals(3, conta);
	    assertTrue(trovatoLatte);
	    assertTrue(trovatoPane);
	}
	
	/**
	 * Verifica la correttezza della descrizione testuale generata per la lista
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 * @throws ListaDiArticoliException In caso di errori nella lista
	 */
	@Test
	void testToString() throws ArticoloException, ListaDiArticoliException {
	    ListaDiArticoli lista = new ListaDiArticoli("Spesa");
	    Articolo a1 = new Articolo("Latte");
	    Articolo a2 = new Articolo("Pane");
	    
	    lista.inserisciArticolo(a1);
	    lista.inserisciArticolo(a2);
	    lista.cancellaArticolo(a2);
	    
	    String risultato = lista.toString();
	    
	    assertNotNull(risultato);
	    assertTrue(risultato.contains("Spesa"));
	    assertTrue(risultato.contains("Latte"));
	    assertTrue(risultato.contains("Pane"));
	}

	/**
	 * Metodo ausiliario per popolare la lista con articoli predefiniti
	 * 
	 * @param l La lista da riempire
	 * 
	 * @throws ArticoloException In caso di dati articolo non validi
	 * @throws ListaDiArticoliException Se si verificano errori nell'inserimento
	 */
	void riempiLista(ListaDiArticoli l) throws ArticoloException, ListaDiArticoliException {
		l.inserisciArticolo("Latte", "Alimentari", 1.50);
		l.inserisciArticolo("Pane", "Alimentari", 2.00);
		l.inserisciArticolo("Vino", "Bevande", 10.00);
	}
}