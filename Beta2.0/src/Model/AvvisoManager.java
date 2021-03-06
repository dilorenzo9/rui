package Model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import Database.DriverManagerConnectionPool;

public class AvvisoManager {

	/**
	 * Restituisce tutti gli avvsi presenti nel databse
	 * @return una lista contenente tutti gli avvisi
	 * @throws SQLException
	 */
	public static ArrayList<Avviso> doRetrieveAll() throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		ArrayList<Avviso> avvisi = new ArrayList<Avviso>();

		String selectSQL = "SELECT * FROM Avviso";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);

			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Avviso bean = new Avviso();
				bean.setNomeAvviso(rs.getString("nomeAvviso"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setMatricolaDocente(rs.getString("matricolaDocente"));
				bean.setOraAvviso(rs.getDouble("oraAvviso"));
				bean.setData(rs.getString("descrizione"));
				bean.setId(rs.getInt("id"));

				avvisi.add(bean);
			}

		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
			} finally {
				if (connection != null)
					connection.close();
			}
		}
		return avvisi;
	}


	/**
	 * Restituisce un array ontenente una lista di tutti gli avvisi pubblicati dal docente 
	 * @param matricolaDocente dato secondo cui distinguere gli avvisi
	 * @return la lista di avvisi
	 * @throws SQLException
	 */
	public ArrayList<Avviso> getAvvisibyDocente(String matricolaDocente) throws SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement1 = null;

		String SQL1 = "select * from avviso where  matricolaDocente=?";
		ArrayList<Avviso> lista = new ArrayList<Avviso>();
		try {
			conn = DriverManagerConnectionPool.getConnection();
			preparedStatement1 = conn.prepareStatement(SQL1);
			preparedStatement1.setString(1, matricolaDocente);

			ResultSet rs = preparedStatement1.executeQuery();
			while (rs.next()) {
				Avviso pr = new Avviso();
				pr.setData(rs.getString("data"));
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar cal = Calendar.getInstance();
				Date date = cal.getTime();                     
				String inActiveDate = null;
				inActiveDate = sdf.format(date);
				
				if (pr.getData().compareTo(inActiveDate) < 0) continue;
				
				pr.setNomeAvviso(rs.getString("nomeAvviso"));
				pr.setDescrizione(rs.getString("descrizione"));
				pr.setMatricolaDocente(rs.getString("matricolaDocente"));
				pr.setOraAvviso(rs.getDouble("oraAvviso"));
				
				pr.setId(rs.getInt("id"));
				lista.add(pr);
			}

		} catch (SQLException e) {
			conn.close();
		}

		String tempStr;
		double tempOra;
		int length = lista.size();

		for (int t = 0; t < length - 1; t++) {
			for (int i = 0; i < length - t - 1; i++) {
				if ((lista.get(i + 1).getData()).compareTo((lista.get(i)).getData()) > 0) {

					tempStr = lista.get(i).getNomeAvviso();
					lista.get(i).setNomeAvviso(lista.get(i + 1).getNomeAvviso());
					lista.get(i + 1).setNomeAvviso(tempStr);

					tempStr = lista.get(i).getDescrizione();
					lista.get(i).setDescrizione(lista.get(i + 1).getDescrizione());
					lista.get(i + 1).setDescrizione(tempStr);

					tempStr = lista.get(i).getMatricolaDocente();
					lista.get(i).setMatricolaDocente(lista.get(i + 1).getMatricolaDocente());
					lista.get(i + 1).setMatricolaDocente(tempStr);

					tempOra = lista.get(i).getOraAvviso();
					lista.get(i).setOraAvviso(lista.get(i + 1).getOraAvviso());
					lista.get(i + 1).setOraAvviso(tempOra);

					tempStr = lista.get(i).getData();
					lista.get(i).setData(lista.get(i + 1).getData());
					lista.get(i + 1).setData(tempStr);
				} else if ((lista.get(i + 1).getData()).compareTo((lista.get(i)).getData()) == 0) {

					if ((lista.get(i + 1).getOraAvviso()) > ((lista.get(i)).getOraAvviso())) {

						tempStr = lista.get(i).getNomeAvviso();
						lista.get(i).setNomeAvviso(lista.get(i + 1).getNomeAvviso());
						lista.get(i + 1).setNomeAvviso(tempStr);

						tempStr = lista.get(i).getDescrizione();
						lista.get(i).setDescrizione(lista.get(i + 1).getDescrizione());
						lista.get(i + 1).setDescrizione(tempStr);

						tempStr = lista.get(i).getMatricolaDocente();
						lista.get(i).setMatricolaDocente(lista.get(i + 1).getMatricolaDocente());
						lista.get(i + 1).setMatricolaDocente(tempStr);

						tempOra = lista.get(i).getOraAvviso();
						lista.get(i).setOraAvviso(lista.get(i + 1).getOraAvviso());
						lista.get(i + 1).setOraAvviso(tempOra);

						tempStr = lista.get(i).getData();
						lista.get(i).setData(lista.get(i + 1).getData());
						lista.get(i + 1).setData(tempStr);
					}
				}
			}
		}

		return lista;
	}

	/**
	 * Aggiunge un avviso
	 * @param avv l'oggetto da aggiungere al database
	 * @return una stringa comunicante l'esito dell'operazione
	 * @throws SQLException 
	 */
	public static String agAvviso(Avviso avv) throws SQLException {
		String nomeAvviso = avv.getNomeAvviso();
		double oraAvviso = avv.getOraAvviso();
		String data = avv.getData();
		String descrizione = avv.getDescrizione();
		String matricolaDocente = avv.getMatricolaDocente();

		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con = DriverManagerConnectionPool.getConnection();
			// con = driverManagerConnectionPool.getConnection();
			String query = "insert into avviso (nomeAvviso,oraAvviso,data,descrizione,matricolaDocente)values (?,?,?,?,?)"; 
			preparedStatement = con.prepareStatement(query); 
			preparedStatement.setString(1, nomeAvviso);
			preparedStatement.setDouble(2, oraAvviso);
			preparedStatement.setString(3, data);
			preparedStatement.setString(4, descrizione);
			preparedStatement.setString(5, matricolaDocente);

			int i = preparedStatement.executeUpdate();

			return "SUCCESS";
			
		} catch (SQLException e) {
			DriverManagerConnectionPool.releaseConnection(con);
		}
		return "Oops.. Something went wrong there..!"; 

	}

	/**
	 * Permette di eliminare un'avviso riconosciuto dal suo id
	 * @param id id dell'avviso
	 * @return true se l'operazione va a buon fine altrimenti false
	 * @throws SQLException
	 */
	public static boolean eliminaAvviso(int id) throws SQLException {
		Connection conn = null;
		PreparedStatement preparedStatement1 = null;
		String selectSQL = "DELETE FROM avviso WHERE id = ?";
		
		try {
			conn = DriverManagerConnectionPool.getConnection();
			preparedStatement1 = conn.prepareStatement(selectSQL);
			preparedStatement1.setInt(1, id);
			preparedStatement1.executeUpdate();
		} catch (SQLException e) {
			DriverManagerConnectionPool.releaseConnection(conn);
		}
		return true;
	}

	/**
	 * Funzione che modifica un avviso indicato tramite id
	 * @param id serve a riconoscere l'avviso da modificare
	 * @param dato indica il nuovo dato da aggiungere
	 * @param action indica l'azione da compiere sull'avviso
	 * @return true se l'operazione va a buon fine altrimenti false
	 * @throws SQLException
	 */
	public static boolean modificaAvviso(int id, String dato, String action) throws SQLException{
		Connection conn = null;
		PreparedStatement preparedStatement5 = null;
		if (dato == null)
			return false;
		if (action.equals("oraAvviso")) {

			if (dato.length() > 30)
				return false;

			String SQL = " UPDATE avviso SET oraAvviso = ? WHERE id = ?";
			try {
				conn = DriverManagerConnectionPool.getConnection();
				preparedStatement5 = conn.prepareStatement(SQL);
				preparedStatement5.setString(1, dato);
				preparedStatement5.setInt(2, id);
				preparedStatement5.execute();
				return true;
			} catch (SQLException e) {
				conn.close();
			} 
		}
		if (action.equals("nomeAvviso")) {

			if (dato.length() > 30)
				return false;
			for (int i = 0; i < dato.length(); i++) {
				if (!Character.isLetter(dato.charAt(i)) && !Character.isWhitespace(dato.charAt(i))
						&& dato.charAt(i) != '\'') {
					return false;

				}
			}
			String SQL = " UPDATE avviso SET nomeAvviso = ? WHERE id = ?";
			try {

				conn = DriverManagerConnectionPool.getConnection();
				preparedStatement5 = conn.prepareStatement(SQL);
				preparedStatement5.setString(1, dato);
				preparedStatement5.setInt(2, id);
				preparedStatement5.execute();
				return true;
			} catch (SQLException e) {
				conn.close();
			} 

		}
		if (action.equals("descrizione")) {
			if (dato.length() > 3000)
				return false;

			String SQL = " UPDATE avviso SET descrizione = ? WHERE id = ?";
			try {
				conn = DriverManagerConnectionPool.getConnection();
				preparedStatement5 = conn.prepareStatement(SQL);
				preparedStatement5.setString(1, dato);
				preparedStatement5.setInt(2, id);
				preparedStatement5.execute();
				return true;
			} catch (SQLException e) {
				conn.close();
			} 
		}
		return false;

	}

	/**
	 * Ritorna un avviso distinguendolo grazie al suo id
	 * @param id parametro che permette di riconoscere l'avviso da ritornare
	 * @return l'avviso
	 * @throws SQLException
	 */
	public Avviso doRetrieveByKey(int id) throws SQLException {

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		Avviso bean = null;

		String selectSQL = "SELECT * FROM avviso WHERE id = ?";

		try {
			connection = DriverManagerConnectionPool.getConnection();
			preparedStatement = connection.prepareStatement(selectSQL);
			preparedStatement.setInt(1, id);

			ResultSet rs = preparedStatement.executeQuery();

			if (rs.next()) {
				bean = new Avviso();
				bean.setId(rs.getInt("id"));
				bean.setNomeAvviso(rs.getString("nomeAvviso"));
				bean.setOraAvviso(rs.getDouble("oraAvviso"));
				bean.setData(rs.getString("data"));
				bean.setDescrizione(rs.getString("descrizione"));
				bean.setMatricolaDocente(rs.getString("matricolaDocente"));
			}

		} catch (SQLException e) {
			connection.close();
		} 
		return bean;
	}

}// fine classe
