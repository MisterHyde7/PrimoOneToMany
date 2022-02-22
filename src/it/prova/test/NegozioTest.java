package it.prova.test;

import java.util.List;

import it.prova.dao.ArticoloDAO;
import it.prova.dao.NegozioDAO;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioTest {

	public static void main(String[] args) {
		NegozioDAO negozioDAOInstance = new NegozioDAO();
		ArticoloDAO articoloDAOInstance = new ArticoloDAO();

		// ora con i dao posso fare tutte le invocazioni che mi servono
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testInserimentoNegozio(negozioDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");

		testFindByIdNegozio(negozioDAOInstance);

		testInsertArticolo(negozioDAOInstance, articoloDAOInstance);
		System.out.println("In tabella negozio ci sono " + negozioDAOInstance.list().size() + " elementi.");
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		testFindByIdArticolo(articoloDAOInstance);
		System.out.println("In tabella articolo ci sono " + articoloDAOInstance.list().size() + " elementi.");

		// ESERCIZIO: COMPLETARE DAO E TEST RELATIVI

		testUpdate(negozioDAOInstance);

		testDelete(negozioDAOInstance);

		testFindAllByIniziale(negozioDAOInstance, "N");

		testSelectByIdWithJoin(negozioDAOInstance, articoloDAOInstance);

		testUpdateArticolo(negozioDAOInstance, articoloDAOInstance, new Articolo(1L, "", "pippo1", new Negozio()));

		testDeleteArticolo(articoloDAOInstance);

		testFindAllByNegozio(negozioDAOInstance, articoloDAOInstance, new Negozio(700L, "provola", "nulla"));

		testFindByMatricola(articoloDAOInstance, "pippo1");

		testFindByIndirizzo(negozioDAOInstance, "via prova");

		Negozio negozioDaPopolare = new Negozio(1000L, "pippo1", "prova");
		testPopulateArticoli(negozioDAOInstance, negozioDaPopolare);
		System.out.println(negozioDaPopolare.getArticoli().size());
//		for (Articolo articoloItem : negozioDaPopolare.getArticoli()) {
//			System.out.println(articoloItem);
//		}

		// ESERCIZIO SUCCESSIVO
		/*
		 * se io voglio caricare un negozio e contestualmente anche i suoi articoli
		 * dovrò sfruttare il populateArticoli presente dentro negoziodao. Per esempio
		 * Negozio negozioCaricatoDalDb = negozioDAOInstance.selectById...
		 * 
		 * negozioDAOInstance.populateArticoli(negozioCaricatoDalDb);
		 * 
		 * e da qui in poi il negozioCaricatoDalDb.getArticoli() non deve essere più a
		 * size=0 (se ha articoli ovviamente) LAZY FETCHING (poi ve lo spiego)
		 */

	}

	private static void testInserimentoNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testInserimentoNegozio inizio.............");
		int quantiNegoziInseriti = negozioDAOInstance.insert(new Negozio("Negozio1", "via dei mille 14"));
		if (quantiNegoziInseriti < 1)
			throw new RuntimeException("testInserimentoNegozio : FAILED");

		System.out.println(".......testInserimentoNegozio fine: PASSED.............");
	}

	private static void testFindByIdNegozio(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testFindByIdNegozio inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		Negozio negozioCheRicercoColDAO = negozioDAOInstance.selectById(primoNegozioDellaLista.getId());
		if (negozioCheRicercoColDAO == null
				|| !negozioCheRicercoColDAO.getNome().equals(primoNegozioDellaLista.getNome()))
			throw new RuntimeException("testFindByIdNegozio : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdNegozio fine: PASSED.............");
	}

	private static void testInsertArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testInsertArticolo inizio.............");
		// mi serve un negozio esistente
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", primoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		System.out.println(".......testInsertArticolo fine: PASSED.............");
	}

	private static void testFindByIdArticolo(ArticoloDAO articoloDAOInstance) {
		System.out.println(".......testFindByIdArticolo inizio.............");
		List<Articolo> elencoArticoliPresenti = articoloDAOInstance.list();
		if (elencoArticoliPresenti.size() < 1)
			throw new RuntimeException("testFindByIdArticolo : FAILED, non ci sono articoli sul DB");

		Articolo primoArticoloDellaLista = elencoArticoliPresenti.get(0);

		Articolo articoloCheRicercoColDAO = articoloDAOInstance.selectById(primoArticoloDellaLista.getId());
		if (articoloCheRicercoColDAO == null
				|| !articoloCheRicercoColDAO.getNome().equals(primoArticoloDellaLista.getNome()))
			throw new RuntimeException("testFindByIdArticolo : FAILED, i nomi non corrispondono");

		System.out.println(".......testFindByIdArticolo fine: PASSED.............");
	}

	private static void testUpdate(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testUpdate inizio.............");
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();

		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		negozioDAOInstance.insert(new Negozio("nulla", "via nulla"));
		String nome = "prova";
		negozioDAOInstance.update(negozioDAOInstance.list().get(negozioDAOInstance.list().size() - 1), nome);

		if (!(negozioDAOInstance.list().get(negozioDAOInstance.list().size() - 1).getNome()).equals(nome)) {
			throw new RuntimeException("test fallito");
		}

		System.out.println("test update riuscito con successo");
	}

	private static void testDelete(NegozioDAO negozioDAOInstance) {
		System.out.println(".......testDelete inizio.............");
		negozioDAOInstance.insert(new Negozio("nulla", "via nulla"));
		if (negozioDAOInstance.delete(negozioDAOInstance.list().get(negozioDAOInstance.list().size() - 1)) < 0) {
			throw new RuntimeException("delete non riuscita");
		}
		System.out.println("delete riuscita con successo");
	}

	private static void testFindAllByIniziale(NegozioDAO negozioDAOInstance, String inizialeInput) {
		System.out.println(".......testFindAllByIniziale inizio.............");
		List<Negozio> listaDiNegoziEsistenti = negozioDAOInstance.list();
		if (listaDiNegoziEsistenti.size() < 0) {
			throw new RuntimeException("non ci sono negozi esistenti");
		}
		if (negozioDAOInstance.findAllByIniziali(inizialeInput) == null) {
			throw new RuntimeException("errore in db");
		}
		System.out.println("test find by iniziale riusciuto con successo");
	}

	private static void testSelectByIdWithJoin(NegozioDAO negozioDAO, ArticoloDAO articoloDAO) {
		System.out.println(".......testSelectByIdWithJoin inizio.............");
		List<Articolo> listaArticoliEsistenti = articoloDAO.list();
		if (listaArticoliEsistenti.size() < 0) {
			throw new RuntimeException("errore nella lista di articoli");
		}

		negozioDAO.insert(new Negozio("nulla", "via nulla"));
		articoloDAO.insert(new Articolo("prova", "prova1", negozioDAO.list().get(negozioDAO.list().size() - 1)));

		if (articoloDAO.selectByIdWithJoin(negozioDAO.list().get(negozioDAO.list().size() - 1).getId()) == null) {
			throw new RuntimeException("errore nel db");
		}
		System.out.println("test eseguito con successo articolo trovato");
	}

	private static void testUpdateArticolo(NegozioDAO negozioDAOInstance, ArticoloDAO articoloDAOInstance,
			Articolo articoloInput) {
		System.out.println("======== test update di articolo =======");
		// mi serve un negozio esistente
		List<Negozio> elencoNegoziPresenti = negozioDAOInstance.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testInsertArticolo : FAILED, non ci sono negozi sul DB");

		Negozio primoNegozioDellaLista = elencoNegoziPresenti.get(0);

		int quantiArticoliInseriti = articoloDAOInstance
				.insert(new Articolo("articolo1", "matricola1", primoNegozioDellaLista));
		if (quantiArticoliInseriti < 1)
			throw new RuntimeException("testInsertArticolo : FAILED");

		if (articoloDAOInstance.update(articoloInput) < 0) {
			throw new RuntimeException("errore di update");
		}

		System.out.println("Test update articolo eseguito con successo");
	}

	private static void testDeleteArticolo(ArticoloDAO articoloDAO) {
		System.out.println("======== test delete di articolo =======");
		articoloDAO.insert(new Articolo());

		if (articoloDAO.delete(articoloDAO.list().get(articoloDAO.list().size() - 1)) < 1) {
			throw new RuntimeException("errore di delete articolo");
		}
		System.out.println("delete riuscita");
	}

	private static void testFindAllByNegozio(NegozioDAO negozioDAO, ArticoloDAO articoloDAO, Negozio negozioInput) {
		System.out.println("======== test find di articolo by negozio =======");
		List<Negozio> elencoNegoziPresenti = negozioDAO.list();
		if (elencoNegoziPresenti.size() < 1)
			throw new RuntimeException("testFindByIdNegozio : FAILED, non ci sono negozi sul DB");

		if (articoloDAO.findAllByNegozio(negozioInput) == null) {
			throw new RuntimeException("errore di find");
		}
		System.out.println("test riuscito senza errori");
	}

	private static void testFindByMatricola(ArticoloDAO articoloDAO, String matricolaInput) {
		System.out.println("======== test find di articolo by matricola =======");

		if (matricolaInput == null || matricolaInput == "") {
			throw new RuntimeException("errore di find");
		}
		if (articoloDAO.findAllByMatricola(matricolaInput) == null) {
			throw new RuntimeException("errore di find");
		}
		System.out.println("test find by matricola riuscito");
	}

	private static void testFindByIndirizzo(NegozioDAO negozioDAO, String indirizzoInput) {
		System.out.println("======== test find di articolo by indirizzo =======");

		if (indirizzoInput == null || indirizzoInput == "") {
			throw new RuntimeException("errore di find");
		}

		negozioDAO.insert(new Negozio("cosa", indirizzoInput));

		if (negozioDAO.findAllByIndirizzoNegozio(indirizzoInput) == null) {
			throw new RuntimeException("errore di find");
		}
		System.out.println("test find by indirizzo riuscito");
	}

	private static void testPopulateArticoli(NegozioDAO negozioDAO, Negozio negozioInput) {
		System.out.println("======== test populate =======");
		if (negozioInput == null) {
			throw new RuntimeException("errore nel metodo populate");
		}
		negozioDAO.insert(negozioInput);
		negozioDAO.populateArticoli(negozioInput);
		System.out.println("test riuscito con populate");
	}

}