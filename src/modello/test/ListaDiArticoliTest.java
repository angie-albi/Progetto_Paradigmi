package modello.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modello.Articolo;
import modello.ListaDiArticoli;
import modello.exception.ArticoloException;
import modello.exception.GestioneListeException;
import modello.exception.ListaDiArticoliException;

class ListaDiArticoliTest {

	ListaDiArticoli l1, l2, l3;
	
	@BeforeEach
	void setUp() throws ListaDiArticoliException {
		l1 = new ListaDiArticoli("Spesa");
		l2 = new ListaDiArticoli("Regali");
		l3 = new ListaDiArticoli("Lavoro");
	}
	
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

	@Test
	void testCancellaArticolo() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
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
	
	@Test
	void testRecuperaArticolo() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
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
	
	@Test
	void testRicercaArticolo() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
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
	
	@Test
	void testRicercaInclusiCancellati() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
	    riempiLista(l1);
	    Articolo a = l1.ricercaArticolo("Latte").get(0);
	    l1.cancellaArticolo(a); 
	    
	    List<Articolo> ris = l1.ricercaArticolo("Latte");
	    assertEquals(1, ris.size());
	    assertEquals("Latte", ris.get(0).getNome());
	}
	
	@Test
	void testCalcoloPrezzoTotale() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
		assertEquals(0.0, l1.calcoloPrezzoTotale(), 0.001);
		
		riempiLista(l1);
		assertEquals(13.50, l1.calcoloPrezzoTotale(), 0.001);
		
		l1.cancellaArticolo(l1.ricercaArticolo("Vino").get(0));
		assertEquals(3.50, l1.calcoloPrezzoTotale(), 0.001);
	}
	
	@Test
	void testSvuotaCancellati() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
		riempiLista(l1);
		Articolo a = l1.ricercaArticolo("Latte").get(0);
		l1.cancellaArticolo(a);
		
		l1.svuotaCancellati();
		assertEquals(0, l1.numElCanc());
		
		assertThrows(ListaDiArticoliException.class, () -> {
			l1.recuperaArticolo(a);
		});
	}
	
	@Test
	void testInserimentoArticoloGiaCancellato() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
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
	
	@Test
	void testIteratoreContenuto() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
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
	
	@Test
	void testToString() throws ArticoloException, ListaDiArticoliException, GestioneListeException {
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

	void riempiLista(ListaDiArticoli l) throws ArticoloException, ListaDiArticoliException, GestioneListeException{
		l.inserisciArticolo("Latte", "Alimentari", 1.50);
		l.inserisciArticolo("Pane", "Alimentari", 2.00);
		l.inserisciArticolo("Vino", "Bevande", 10.00);
	}
}