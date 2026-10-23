

package healthymind.servlet;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;

import healthymind.dao.UsuarioDAO;
import healthymind.modelo.Usuario;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/api/usuarios")
public class UsuariosApiServlet extends HttpServlet {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    // GET: consultar todos los usuarios
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Usuario> usuarios = usuarioDAO.listar();
        JsonArray resultado = new JsonArray();

        for (Usuario usuario : usuarios) {
            JsonObject objeto = new JsonObject();

            objeto.addProperty("idUsuario", usuario.getIdUsuario());
            objeto.addProperty("nombre", usuario.getNombre());
            objeto.addProperty("edad", usuario.getEdad());
            objeto.addProperty("correo", usuario.getCorreo());
            objeto.addProperty("nacionalidad", usuario.getNacionalidad());
            objeto.addProperty("telefono", usuario.getTelefono());
            objeto.addProperty("tipoUsuario", usuario.getTipoUsuario());

            // No devolver contraseñas ni hashes.
            resultado.add(objeto);
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(resultado.toString());
    }

    // POST: registrar un usuario mediante JSON
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String contentType = request.getContentType();

        if (contentType == null
                || !contentType.toLowerCase().startsWith("application/json")) {
            responder(response, 415,
                    "El contenido debe ser application/json.");
            return;
        }

        StringBuilder cuerpo = new StringBuilder();

        try (BufferedReader reader = request.getReader()) {
            String linea;

            while ((linea = reader.readLine()) != null) {
                cuerpo.append(linea);

                if (cuerpo.length() > 20_000) {
                    responder(response, 413,
                            "La solicitud es demasiado grande.");
                    return;
                }
            }
        }

        JsonObject datos;

        try {
            datos = JsonParser.parseString(cuerpo.toString()).getAsJsonObject();
        } catch (JsonParseException | IllegalStateException e) {
            responder(response, 400, "El JSON enviado no es válido.");
            return;
        }

        String nombre = obtenerTexto(datos, "nombre");
        String correo = obtenerTexto(datos, "correo");
        String contrasena = obtenerTexto(datos, "contrasena");
        String nacionalidad = obtenerTexto(datos, "nacionalidad");
        String telefono = obtenerTexto(datos, "telefono");

        if (nombre == null || nombre.isBlank()
                || correo == null || correo.isBlank()
                || contrasena == null || contrasena.isBlank()
                || nacionalidad == null || nacionalidad.isBlank()
                || telefono == null || telefono.isBlank()
                || !datos.has("edad")
                || datos.get("edad").isJsonNull()
                || !datos.get("edad").isJsonPrimitive()
                || !datos.get("edad").getAsJsonPrimitive().isNumber()) {

            responder(response, 400,
                    "Todos los campos son obligatorios y la edad debe ser numérica.");
            return;
        }

        int edad;

        try {
            edad = datos.get("edad").getAsInt();

            // Evitar valores decimales o notación no entera.
            if (!datos.get("edad").getAsString()
                    .matches("-?\\d+")) {
                responder(response, 400,
                        "La edad debe ser un número entero.");
                return;
            }
        } catch (NumberFormatException e) {
            responder(response, 400, "La edad no es válida.");
            return;
        }

        if (edad < 1 || edad > 120) {
            responder(response, 400,
                    "La edad debe estar entre 1 y 120.");
            return;
        }

        if (nombre.length() > 100 || correo.length() > 254
                || nacionalidad.length() > 100 || telefono.length() > 30) {
            responder(response, 400,
                    "Uno o más campos superan la longitud permitida.");
            return;
        }

        if (!Pattern.matches(
                "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$",
                correo.trim())) {
            responder(response, 400,
                    "El formato del correo no es válido.");
            return;
        }

        if (contrasena.length() < 12 || contrasena.length() > 128) {
            responder(response, 400,
                    "La contraseña debe tener entre 12 y 128 caracteres.");
            return;
        }

        Usuario usuario = new Usuario(
                nombre.trim(),
                edad,
                correo.trim(),
                contrasena,
                nacionalidad.trim(),
                telefono.trim(),
                "PACIENTE"
        );

        try {
            if (usuarioDAO.insertar(usuario)) {
                JsonObject resultado = new JsonObject();
                resultado.addProperty(
                        "mensaje", "Usuario registrado correctamente.");

                response.setStatus(HttpServletResponse.SC_CREATED);
                response.getWriter().write(resultado.toString());
            } else {
                responder(response, 500,
                        "No se pudo registrar el usuario. "
                        + "Puede existir un correo duplicado "
                        + "o un problema con la base de datos.");
            }
        } catch (RuntimeException e) {
            getServletContext().log(
                    "Error al registrar usuario mediante la API.", e);

            responder(response, 500,
                    "No se pudo completar el registro.");
        }
    }

    // Obtener un campo JSON únicamente si es texto.
    private String obtenerTexto(JsonObject datos, String campo) {
        if (!datos.has(campo)
                || datos.get(campo).isJsonNull()
                || !datos.get(campo).isJsonPrimitive()
                || !datos.get(campo).getAsJsonPrimitive().isString()) {
            return null;
        }

        return datos.get(campo).getAsString();
    }

    // Enviar errores con formato JSON.
    private void responder(HttpServletResponse response,
                           int estado, String mensaje)
            throws IOException {

        response.setStatus(estado);

        JsonObject resultado = new JsonObject();
        resultado.addProperty("error", mensaje);

        response.getWriter().write(resultado.toString());
    }
}
