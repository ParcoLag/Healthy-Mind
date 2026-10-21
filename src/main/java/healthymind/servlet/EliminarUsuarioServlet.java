package healthymind.servlet;

import healthymind.dao.UsuarioDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/eliminar")
public class EliminarUsuarioServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // GET: recibe el id por la URL, elimina el usuario y vuelve a la lista
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int id = Integer.parseInt(request.getParameter("id"));
            usuarioDAO.eliminar(id);
        } catch (NumberFormatException e) {
            // si el id no es un número, no se hace nada
        }

        response.sendRedirect("listar");
    }
}