package modello.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modello.Articolo;
import modello.GestioneListe;
import modello.ListaDiArticoli;
import modello.exception.GestioneListeException;

class GestioneListeTest {

	private ListaDiArticoli l1;
	private Articolo a1;

	@BeforeEach
	void setUp() throws Exception {
		GestioneListe.reset();
		l1 = new ListaDiArticoli("Spesa");
		a1 = new Articolo("Latte", "Alimentari", 1.50);
	}

	@Test
	void testInserisciLista() {
		assertDoesNotThrow(() -> {
			GestioneListe.inserisciLista(l1);
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.inserisciLista(new ListaDiArticoli("Spesa"));
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.inserisciLista(null);
		});
	}
	
	@Test
	void testMatchLista() throws GestioneListeException {
		GestioneListe.inserisciLista(l1);
		
		assertEquals(l1, GestioneListe.matchLista("Spesa"));
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.matchLista("Lavoro");
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.matchLista("");
		});
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.matchLista(null);
		});
	}

	@Test
	void testCancellaLista() throws GestioneListeException {
		GestioneListe.inserisciLista(l1);
		
		assertDoesNotThrow(() -> {
			GestioneListe.cancellaLista("Spesa");
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.matchLista("Spesa");
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaLista("NonEsiste");
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaLista("");
		});
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaLista(null);
		});
	}

	@Test
	void testInserisciCategoria() {
		assertDoesNotThrow(() -> {
			GestioneListe.inserisciCategoria("Elettronica");
		});
		
		assertTrue(GestioneListe.esisteCategoria("Elettronica"));
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.inserisciCategoria("Elettronica");
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.inserisciCategoria("");
		});
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.inserisciCategoria(null);
		});
	}
	
	@Test
	void testCancellaCategoria() throws GestioneListeException {
		GestioneListe.inserisciCategoria("Bricolage");
		
		assertDoesNotThrow(() -> {
			GestioneListe.cancellaCategoria("Bricolage");
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaCategoria("Bricolage");
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaCategoria(GestioneListe.CATEGORIA_DEFAULT);
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaCategoria("");
		});
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaCategoria(null);
		});
	}

	@Test
	void testInserisciArticolo() {
		assertDoesNotThrow(() -> {
			GestioneListe.inserisciArticolo(a1);
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.inserisciArticolo(a1);
		});
	}

	@Test
	void testCancellaArticolo() throws GestioneListeException {
		GestioneListe.inserisciArticolo(a1);
		
		assertDoesNotThrow(() -> {
			GestioneListe.cancellaArticolo(a1);
		});
		
		assertThrows(GestioneListeException.class, () -> {
			GestioneListe.cancellaArticolo(a1);
		});
	}
}