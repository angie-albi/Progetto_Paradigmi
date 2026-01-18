package main;

import gui.rigaComando.InterfacciaRigaDiComando;
import jbook.util.Input;

/**
 * Classe Main principale del programma che avvia le interfaccie disponibili dell'applicazione
 */
public class Main {

	/**
	 * Main principale del programma, permette all'utente di scegliere tra
	 * <ul>
	 *   <li> Interfaccia grafica </li>
	 *   <li> Interfaccia da riga di comando </li>
	 * </ul>
	 * @param args Argomenti da riga di comando
	 */
	public static void main(String[] args) {
		boolean on = true;
		
		while (on) {
			try {
				menuInterfacce();
				
				int scelta = Input.readInt("Scegli l'interfaccia:");
				switch (scelta){
					case 0 -> on = false;
					case 1 -> {
						System.out.println("Avvio interfaccia grafica...");
						interfacciaGrafica();
					}
					case 2 -> {
						System.out.println("Avvio interfaccia da riga di comando...");
						interfacciaRigaComando();
					}
					
			
					default -> System.out.println("\nScelta non valida, riprova");
				}
			} catch (NumberFormatException e) {
	            System.out.println("\nErrore: Inserisci un numero valido (da 0 a 2), riprova");
	        } catch (Exception e) {
	            System.out.println("\nSi è verificato un errore: " + e.getMessage());
	            System.out.println("Riprova");
	        }
		}
		System.out.println("Chiusura del programma...");
	}
	
	/**
	 * Metodo per avviare l'interfaccia da riga di comando
	 */
	private static void interfacciaRigaComando() {
		new InterfacciaRigaDiComando();
	}

	/**
	 * Metodo per avviare l'interfaccia grafica
	 */
	private static void interfacciaGrafica() {
		
		//provvisorio, poi si richiamera a GestoreListe
						
	}
	
	/**
	 * Menu delle interfaccie disponibili per l'utente
	 */
	private static void menuInterfacce() {
		System.out.println("\n----- INTERFACCE DISPONIBILI -------");	
		System.out.println("0 - Esci");
		System.out.println("1 - Interfaccia grafica");	
		System.out.println("2 - Interfaccia da riga di comando");	
		System.out.println("------------------------------------\n");
	}
}
