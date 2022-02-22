package it.prova.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import it.prova.connection.MyConnection;
import it.prova.model.Articolo;
import it.prova.model.Negozio;

public class NegozioDAO {

	public List<Negozio> list() {

		List<Negozio> result = new ArrayList<Negozio>();
		Negozio negozioTemp = null;

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				ResultSet rs = s.executeQuery("select * from negozio a ")) {

			while (rs.next()) {
				negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				result.add(negozioTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public Negozio selectById(Long idNegozioInput) {

		if (idNegozioInput == null || idNegozioInput < 1)
			return null;

		Negozio result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio i where i.id=?;")) {

			ps.setLong(1, idNegozioInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Negozio();
					result.setId(rs.getLong("id"));
					result.setNome(rs.getString("nome"));
					result.setIndirizzo(rs.getString("indirizzo"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Negozio negozioInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("INSERT INTO negozio (nome, indirizzo) VALUES (?, ?)")) {

			ps.setString(1, negozioInput.getNome());
			ps.setString(2, negozioInput.getIndirizzo());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// TODO
	public int update(Negozio negozioInput, String nomeInput) {
		if (negozioInput == null || negozioInput.getId() < 1) {
			return 0;
		}

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("update negozio set nome =?, indirizzo=? where nome =?;")) {

			ps.setString(1, nomeInput);
			ps.setString(2, negozioInput.getIndirizzo());
			ps.setString(3, negozioInput.getNome());
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return result;
	}

	public int delete(Negozio negozioInput) {

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM negozio WHERE id = ?;")) {

			ps.setLong(1, negozioInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// implementare inoltre
	public List<Negozio> findAllByIniziali(String inizialeInput) {
		List<Negozio> result = new ArrayList<Negozio>();
		Negozio negozioTemp = null;

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				PreparedStatement ps = c.prepareStatement("SELECT * FROM negozio where nome like ?;")) {

			ps.setString(1, inizialeInput + "%");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				result.add(negozioTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	public List<Articolo> findAllByIndirizzoNegozio(String indirizzoNegozioInput) {
		if (indirizzoNegozioInput == "" || indirizzoNegozioInput == null) {
			throw new RuntimeException("indirizzo non valido");
		}
		List<Articolo> lista = new ArrayList<Articolo>();
		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from negozio where indirizzo = ?;")) {

			ps.setString(1, indirizzoNegozioInput);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("NOME"));
					result.setMatricola(rs.getString("indirizzo"));
					result.setId(rs.getLong("id"));
					lista.add(result);
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return lista;
	}

	// prende negozioInput e grazie al suo id va sulla tabella articoli e poi
	// ad ogni iterazione sul resultset aggiunge agli articoli di negozioInput
	public void populateArticoli(Negozio negozioInput) {
		List<Articolo> result = new ArrayList<Articolo>();
		Articolo articoloTemp = new Articolo();

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				PreparedStatement ps = c.prepareStatement("select * from articolo where negozio_id = ?;")) {

			ps.setLong(1, negozioInput.getId());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					articoloTemp.setNome(rs.getString("NOME"));
					articoloTemp.setMatricola(rs.getString("matricola"));
					articoloTemp.setId(rs.getLong("id"));

					result.add(articoloTemp);
				}
			}

			negozioInput.setArticoli(result);

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
	}

}
