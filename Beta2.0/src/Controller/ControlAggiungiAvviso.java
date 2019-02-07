package Controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.*;

/**
 * Servlet implementation class ControlAggiungiAvviso
 */
@WebServlet("/ControlAggiungiAvviso")
public class ControlAggiungiAvviso extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ControlAggiungiAvviso() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String nomeAvviso = request.getParameter("nomeAvviso");
		double oraAvviso = Double.parseDouble(request.getParameter("oraAvviso"));
		String data = request.getParameter("data");
		String descrizione = request.getParameter("descrizione");
		String matricolaDocente = request.getParameter("matricolaDocente");

		Avviso bean = new Avviso();

		bean.setDescrizione(descrizione);
		bean.setNomeAvviso(nomeAvviso);
		bean.setData(data);
		bean.setOraAvviso(oraAvviso);
		bean.setMatricolaDocente(matricolaDocente);

		AvvisoManager adFilmDAO = new AvvisoManager();

		// The core Logic of the Registration application is present here. We are going
		// to insert user data in to the database.
		String userRegistered = AvvisoManager.agAvviso(bean);

		if (userRegistered.equals("SUCCESS")) // On success, you can display a message to user on Home page
		{
			request.getRequestDispatcher("ModificaDocente.jsp").forward(request, response);
		} else // On Failure, display a meaningful message to the User.
		{
			request.setAttribute("errMessage", userRegistered);
			request.getRequestDispatcher("/exception.jsp").forward(request, response);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
