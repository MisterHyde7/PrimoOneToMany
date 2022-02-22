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

public class ArticoloDAO {

	public List<Articolo> list() {

		List<Articolo> result = new ArrayList<Articolo>();

		try (Connection c = MyConnection.getConnection();
				Statement s = c.createStatement();
				// STRATEGIA EAGER FETCHING
				ResultSet rs = s.executeQuery("select * from articolo a inner join negozio n on n.id=a.negozio_id")) {

			while (rs.next()) {
				Articolo articoloTemp = new Articolo();
				articoloTemp.setNome(rs.getString("NOME"));
				articoloTemp.setMatricola(rs.getString("matricola"));
				articoloTemp.setId(rs.getLong("a.id"));

				Negozio negozioTemp = new Negozio();
				negozioTemp.setId(rs.getLong("n.id"));
				negozioTemp.setNome(rs.getString("nome"));
				negozioTemp.setIndirizzo(rs.getString("indirizzo"));

				articoloTemp.setNegozio(negozioTemp);
				result.add(articoloTemp);
			}

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public Articolo selectById(Long idArticoloInput) {

		if (idArticoloInput == null || idArticoloInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo a where a.id=?")) {

			ps.setLong(1, idArticoloInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("NOME"));
					result.setMatricola(rs.getString("matricola"));
					result.setId(rs.getLong("id"));
				} else {
					result = null;
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public int insert(Articolo articoloInput) {

		if (articoloInput.getNegozio() == null || articoloInput.getNegozio().getId() < 1)
			return -1;

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c
						.prepareStatement("INSERT INTO articolo (nome, matricola,negozio_id) VALUES (?, ?, ?)")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getNegozio().getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	// TODO

	public Articolo selectByIdWithJoin(Long idInput) {
		if (idInput == null || idInput < 1)
			return null;

		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement(
						"SELECT * FROM negozio INNER JOIN articolo ON negozio_id = negozio.id where negozio.id = ?;")) {

			ps.setLong(1, idInput);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("nome"));
					result.setMatricola(rs.getString("matricola"));
					result.setId(rs.getLong("id"));
				}
			} // niente catch qui

		} catch (Exception e) {
			e.printStackTrace();
			// rilancio in modo tale da avvertire il chiamante
			throw new RuntimeException(e);
		}
		return result;
	}

	public int update(Articolo articoloInput) {
		if (articoloInput == null || articoloInput.getId() < 1) {
			return 0;
		}

		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("UPDATE articolo SET nome=?, matricola=? WHERE id = ?;")) {

			ps.setString(1, articoloInput.getNome());
			ps.setString(2, articoloInput.getMatricola());
			ps.setLong(3, articoloInput.getId());
			result = ps.executeUpdate();

		} catch (Exception e) {

			e.printStackTrace();

		}
		return result;
	}

	public int delete(Articolo articoloInput) {
		int result = 0;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("DELETE FROM articolo WHERE id = ?;")) {

			ps.setLong(1, articoloInput.getId());
			result = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return result;
	}

	// implementare inoltre
	public List<Articolo> findAllByNegozio(Negozio negozioInput) {
		if (negozioInput == null)
			return null;

		List<Articolo> lista = new ArrayList<Articolo>();
		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo where negozio_id = ?;")) {

			ps.setLong(1, negozioInput.getId());

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("NOME"));
					result.setMatricola(rs.getString("matricola"));
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

	public List<Articolo> findAllByMatricola(String matricolaInput) {
		if (matricolaInput == null || matricolaInput == "")
			return null;

		List<Articolo> lista = new ArrayList<Articolo>();
		Articolo result = null;
		try (Connection c = MyConnection.getConnection();
				PreparedStatement ps = c.prepareStatement("select * from articolo where matricola = ?;")) {

			ps.setString(1, matricolaInput);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					result = new Articolo();
					result.setNome(rs.getString("NOME"));
					result.setMatricola(rs.getString("matricola"));
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

}
