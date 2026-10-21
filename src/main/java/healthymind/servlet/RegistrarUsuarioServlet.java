package healthymind.servlet;

import healthymind.dao.UsuarioDAO;
import healthymind.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/registrar")
public class RegistrarUsuarioServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // POST: recibe los datos del formulario y los guarda en la base de datos
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html; charset=UTF-8");

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String contrasena = request.getParameter("contrasena");
        String nacionalidad = request.getParameter("nacionalidad");
        String telefono = request.getParameter("telefono");

        String mensaje;
        try {
            int edad = Integer.parseInt(request.getParameter("edad"));
            Usuario usuario = new Usuario(nombre, edad, correo, contrasena,
                    nacionalidad, telefono, "PACIENTE");

            if (usuarioDAO.insertar(usuario)) {
                mensaje = "Usuario registrado correctamente.";
            } else {
                mensaje = "No se pudo registrar (revisa si el correo ya existe).";
            }
        } catch (NumberFormatException e) {
            mensaje = "La edad debe ser un numero.";
        }

        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><head><meta charset=\"UTF-8\"><title>HealthyMind</title></head><body>");
        out.println("<h1>" + mensaje + "</h1>");
        out.println("<a href=\"registro.html\">Registrar otro usuario</a>");
                out.println("<a href=\"listar\">Ver usuarios</a>");
        out.println("</body></html>");
    }
}