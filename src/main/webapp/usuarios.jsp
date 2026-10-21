<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List, healthymind.modelo.Usuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>HealthyMind - Usuarios</title>
</head>
<body>
    <%@ include file="encabezado.jsp" %>

    <h2>Lista de usuarios</h2>

    <%
        List<Usuario> usuarios = (List<Usuario>) request.getAttribute("usuarios");
    %>

    <% if (usuarios == null || usuarios.isEmpty()) { %>
        <p>No hay usuarios registrados.</p>
    <% } else { %>
        <table border="1" cellpadding="6">
            <tr>
                <th>Id</th>
                <th>Nombre</th>
                <th>Edad</th>
                <th>Correo</th>
                <th>Nacionalidad</th>
                <th>Teléfono</th>
                <th>Tipo</th>
             <th>Acción</th>
            </tr>
            <% for (Usuario u : usuarios) { %>
            <tr>
                <td><%= u.getIdUsuario() %></td>
                <td><%= u.getNombre() %></td>
                <td><%= u.getEdad() %></td>
                <td><%= u.getCorreo() %></td>
                <td><%= u.getNacionalidad() %></td>
                <td><%= u.getTelefono() %></td>
                <td><%= u.getTipoUsuario() %></td>
                                <td><a href="eliminar?id=<%= u.getIdUsuario() %>"
                       onclick="return confirm('¿Eliminar este usuario?');">Eliminar</a></td>
            </tr>
            <% } %>
        </table>
    <% } %>
</body>
</html>