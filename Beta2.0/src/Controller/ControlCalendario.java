package Controller;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.PrenotazioneManager;

/**
 * Servlet implementation class ControlCalendario
 */
@WebServlet("/ControlCalendario")
public class ControlCalendario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ControlCalendario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			request.setAttribute("RicPren", PrenotazioneManager.returnPrenotazionebyDocente(request.getParameter("matricolaDocente")));
			
			RequestDispatcher requestDispatcher = request.getRequestDispatcher("Calendario.jsp");
			requestDispatcher.forward(request, response);
		} catch (SQLException e) {
		}
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("Home.jsp");
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}