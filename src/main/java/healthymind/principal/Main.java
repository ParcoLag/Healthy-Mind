package healthymind.principal;

import healthymind.dao.UsuarioDAO;
import healthymind.modelo.Usuario;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            int opcion;

            do {
                System.out.println("\n=== HEALTHYMIND - USUARIOS ===");
                System.out.println("1. Registrar usuario");
                System.out.println("2. Listar usuarios");
                System.out.println("3. Editar usuario");
                System.out.println("4. Eliminar usuario");
                System.out.println("0. Salir");
                System.out.print("Opcion: ");
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1:
                        System.out.print("Nombre: ");
                        String nombre = teclado.nextLine();
                        System.out.print("Edad: ");
                        int edad = Integer.parseInt(teclado.nextLine());
                        System.out.print("Correo: ");
                        String correo = teclado.nextLine();
                        System.out.print("Contrasena: ");
                        String contrasena = teclado.nextLine();
                        System.out.print("Nacionalidad: ");
                        String nacionalidad = teclado.nextLine();
                        System.out.print("Telefono: ");
                        String telefono = teclado.nextLine();

                        Usuario nuevo = new Usuario(nombre, edad, correo, contrasena,
                                nacionalidad, telefono, "PACIENTE");
                        if (usuarioDAO.insertar(nuevo)) {
                            System.out.println("Usuario registrado.");
                        }
                        break;

                    case 2:
                        for (Usuario u : usuarioDAO.listar()) {
                            System.out.println(u);
                        }
                        break;

                    case 3:
                        Usuario editado = new Usuario();
                        System.out.print("Id del usuario a editar: ");
                        editado.setIdUsuario(Integer.parseInt(teclado.nextLine()));
                        System.out.print("Nuevo nombre: ");
                        editado.setNombre(teclado.nextLine());
                        System.out.print("Nueva edad: ");
                        editado.setEdad(Integer.parseInt(teclado.nextLine()));
                        System.out.print("Nuevo correo: ");
                        editado.setCorreo(teclado.nextLine());
                        System.out.print("Nueva nacionalidad: ");

                                            editado.setNacionalidad(teclado.nextLine());
                        System.out.print("Nuevo telefono: ");
                        editado.setTelefono(teclado.nextLine());

                        if (usuarioDAO.actualizar(editado)) {
                            System.out.println("Usuario actualizado.");
                        } else {
                            System.out.println("No se encontro ese usuario.");
                        }
                        break;

                    case 4:
                        System.out.print("Id del usuario a eliminar: ");
                        int id = Integer.parseInt(teclado.nextLine());
                        if (usuarioDAO.eliminar(id)) {
                            System.out.println("Usuario eliminado.");
                        } else {
                            System.out.println("No se encontro ese usuario.");
                        }
                        break;

                    case 0:
                        System.out.println("Hasta pronto.");
                        break;

                    default:
                        System.out.println("Opcion no valida.");
                }
            } while (opcion != 0);
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}