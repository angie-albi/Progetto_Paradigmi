package modello.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import modello.Articolo;
import modello.exception.ArticoloException;

/**
 * La classe {@code ArticoloTest} contiene i test unitari per verificare la correttezza della classe {@code Articolo}
 * <p> Vengono testati:
 * <ul>
 *   <li>Il corretto funzionamento dei costruttori</li>
 *   <li>La validazione dei dati (nome e prezzo)</li>
 *   <li>La modifica dei campi tramite i metodi setters</li>
 *   <li>La logica di uguaglianza e la rappresentazione testuale</li>
 * </ul>
 * 
 * @author Angie Albitres
 */
class ArticoloTest {
	
	private Articolo a1, a2, a3, a4;

	/**
	 * Configura l'ambiente di test inizializzando un articolo di riferimento prima di ogni test
	 * 
	 * @throws Exception In caso di errori durante l'inizializzazione
	 */
	@BeforeEach
	void setUp() throws Exception {
		a1 = new Articolo("Latte", "Cibo", 10.00 , "Urgente!");
	}
	
	/**
	 * Verifica che i diversi costruttori di {@code Articolo} assegnino correttamente i valori iniziali
	 * 
	 * @throws ArticoloException Se i dati inseriti non sono validi 
	 */
	@Test
	void testCostruttore() throws ArticoloException{ 
		assertEquals("Latte", a1.getNome());
		assertEquals("Cibo", a1.getCategoria());
		assertEquals(10.00 , a1.getPrezzo(), 0.001); 
		assertEquals("Urgente!", a1.getNota());
		
		a2 = new Articolo("Pane", "Cibo", 10);
		assertEquals("Pane", a2.getNome());
		assertEquals("Cibo", a2.getCategoria());
		assertEquals(10.00 , a2.getPrezzo(), 0.001);
		assertEquals("", a2.getNota());
		
		a3 = new Articolo("Acqua", "Cibo");
		assertEquals("Acqua", a3.getNome());
		assertEquals("Cibo", a3.getCategoria());
		assertEquals(0.00 , a3.getPrezzo(), 0.001);
		assertEquals("", a3.getNota());
		
		a4 = new Articolo("Mela");
		assertEquals("Mela", a4.getNome());
		assertEquals(Articolo.CATEGORIA_DEFAULT, a4.getCategoria());
		assertEquals(0.00 , a4.getPrezzo(), 0.001);
		assertEquals("", a4.getNota());
	}

	/**
	 * Verifica che venga sollevata un'eccezione quando si tenta di creare un articolo con prezzo negativo
	 */
	@Test
	public void testCostruttorePrezzoNegativo() {
		assertThrows(ArticoloException.class, () -> {
			new Articolo("TV", "Categoria", -10, "Errore");
		});
	}
	
	/**
	 * Verifica che il sistema impedisca la creazione di articoli con nomi vuoti o nulli
	 */
    @Test
    public void testCostruttoreNomeNonValido() {
        assertThrows(ArticoloException.class, () -> {
            new Articolo("", "Categoria", 10, "Nota");
        });
        
        assertThrows(ArticoloException.class, () -> {
            new Articolo(null, "Categoria", 10, "Nota");
        });
    }

    /**
     * Verifica il corretto aggiornamento dei campi e la validazione del prezzo tramite i setters
	 * 
	 * @throws ArticoloException Se il prezzo impostato è negativo
     */
	@Test
	public void testSetters() throws ArticoloException {
		a1.setCategoria("Cibo");
		assertEquals("Cibo", a1.getCategoria());
		
		a1.setNota("Urgente");
		assertEquals("Urgente", a1.getNota());
		
		a1.setPrezzo(99.99);
		assertEquals(99.99, a1.getPrezzo(), 0.001);
		
		assertDoesNotThrow(() -> {
			a1.setPrezzo(10.20);
		});
		
		assertThrows(ArticoloException.class, () -> {
			a1.setPrezzo(-1.0);
		});
	}
	
	/**
	 * Verifica che i setters gestiscano correttamente i valori nulli o vuoti assegnando i default
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 */
	@Test
	public void testSettersValoriDiDefault() throws ArticoloException {
	    a1.setCategoria(null);
	    assertEquals(Articolo.CATEGORIA_DEFAULT, a1.getCategoria());

	    a1.setCategoria("");
	    assertEquals(Articolo.CATEGORIA_DEFAULT, a1.getCategoria());

	    a1.setNota(null);
	    assertEquals("", a1.getNota());
	}
	
	/**
	 * Verifica la logica di uguaglianza basata esclusivamente su nome e categoria (case-insensitive)
	 * 
	 * @throws ArticoloException In caso di errore nella creazione degli articoli di test
	 */
	@Test
	public void testEquals() throws ArticoloException {
	    a2 = new Articolo("Latte", "Cibo", 10.00, "Urgente!");
	    assertTrue(a1.equals(a2));
	    Articolo aCase = new Articolo("LATTE", "cibo", 10.00, "Urgente!");
	    assertTrue(a1.equals(aCase));
	    
	    a4 = new Articolo("Latte", "Cibo", 15.00, "Altra nota"); 
	    assertTrue(a1.equals(a4)); 
	    
	    a3 = new Articolo("Latte", "Casa", 10.00, "Urgente!");
	    assertFalse(a1.equals(a3));
	    
	    Articolo aDiverso = new Articolo("Pane", "Cibo");
	    assertFalse(a1.equals(aDiverso));
	    
	    assertFalse(a1.equals(null));
	    assertFalse(a1.equals("Stringa"));
	}
	/**
	 * Verifica che la descrizione testuale dell'articolo contenga tutti i dati correttamente formattati
	 * 
	 * @throws ArticoloException In caso di errore nei dati
	 */
	@Test
	public void testToString() throws ArticoloException {
		assertEquals("\n Articolo [nome=Latte, categoria=Cibo, prezzo=10.0, nota=Urgente!]", a1.toString());
	}
}