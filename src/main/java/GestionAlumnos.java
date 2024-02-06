import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GestionAlumnos {

    // Establecer conexión con la base de datos

    //Importante en el script poner esta sentencia para evitar fallos: ALTER TABLE alumnos MODIFY COLUMN id_alumno INT AUTO_INCREMENT;
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/matricula";
    static final String USER = "root";
    static final String PASS = "";

    static Connection conn = null;
    static PreparedStatement stmt = null;

    public static void main(String[] args) {
        try {
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Scanner scanner = new Scanner(System.in);

            int opcion;
            do {
                System.out.println("Menú:");
                System.out.println("1. Dar de alta un alumno");
                System.out.println("2. Eliminar un alumno");
                System.out.println("3. Matricular a un alumno en asignaturas");
                System.out.println("4. Ver asignaturas asociadas a un alumno");
                System.out.println("5. Salir");
                System.out.print("Seleccione una opción: ");
                opcion = scanner.nextInt();

                switch (opcion) {
                    case 1:
                        darAltaAlumno();
                        break;
                    case 2:
                        eliminarAlumno();
                        break;
                    case 3:
                        matricularAlumno();
                        break;
                    case 4:
                        verAsignaturasAlumno();
                        break;
                    case 5:
                        System.out.println("Saliendo del programa...");
                        break;
                    default:
                        System.out.println("Opción no válida");
                }
            } while (opcion != 5);

            scanner.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Método para dar de alta a un alumno
     * @throws SQLException
     */
    public static void darAltaAlumno() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese los datos del alumno:");

        int id = 0;
        String apellidos = "";
        String nombre = "";
        int curso = 0;
        int titulacion = 0;

        boolean entradaCorrectaId = false;
        boolean entradaCorrectaCurso = false;
        boolean entradaCorrectaTitulacion = false;

        do {
            try {
                System.out.print("ID: ");
                id = scanner.nextInt();
                entradaCorrectaId = true;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número entero para el ID.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        } while (!entradaCorrectaId);

        scanner.nextLine(); // Consume newline

        System.out.print("Apellidos: ");
        apellidos = scanner.nextLine();

        System.out.print("Nombre: ");
        nombre = scanner.nextLine();

        do {
            try {
                System.out.print("Curso: ");
                curso = scanner.nextInt();
                entradaCorrectaCurso = true;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número entero para el curso.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        } while (!entradaCorrectaCurso);

        do {
            try {
                System.out.print("Titulación: ");
                titulacion = scanner.nextInt();
                entradaCorrectaTitulacion = true;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número entero para la titulación.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        } while (!entradaCorrectaTitulacion);

        String sql = "INSERT INTO alumnos VALUES (?, ?, ?, ?, ?)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.setString(2, apellidos);
        stmt.setString(3, nombre);
        stmt.setInt(4, curso);
        stmt.setInt(5, titulacion);

        int rowsAffected = stmt.executeUpdate();
        System.out.println(rowsAffected + " alumno(s) insertado(s) correctamente.");
    }

    /**
     * Método para eliminar un alumno de la base de datos
     * @throws SQLException
     */
    public static void eliminarAlumno() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el ID del alumno a eliminar: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM alumnos WHERE id_alumno = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);

        int rowsAffected = stmt.executeUpdate();
        System.out.println(rowsAffected + " alumno(s) eliminado(s) correctamente.");
    }

    /**
     * Método para matricular a un alumno
     * @throws SQLException
     */
    public static void matricularAlumno() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int idAlumno = 0;
        int idAsignatura = 0;

        boolean entradaCorrectaIdAlumno = false;
        boolean entradaCorrectaIdAsignatura = false;

        do {
            try {
                System.out.print("Ingrese el ID del alumno: ");
                idAlumno = scanner.nextInt();
                entradaCorrectaIdAlumno = true;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número entero para el ID del alumno.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        } while (!entradaCorrectaIdAlumno);

        do {
            try {
                System.out.print("Ingrese el ID de la asignatura: ");
                idAsignatura = scanner.nextInt();
                entradaCorrectaIdAsignatura = true;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número entero para el ID de la asignatura.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        } while (!entradaCorrectaIdAsignatura);

        String sql = "INSERT INTO alumnos_asignaturas VALUES (?, ?, 0)";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idAlumno);
        stmt.setInt(2, idAsignatura);

        int rowsAffected = stmt.executeUpdate();
        System.out.println(rowsAffected + " matrícula(s) realizada(s) correctamente.");
    }

    /**
     * Método para ver asignaturas de un alumno
     * @throws SQLException
     */
    public static void verAsignaturasAlumno() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        int idAlumno = 0;

        boolean entradaCorrectaIdAlumno = false;

        do {
            try {
                System.out.print("Ingrese el ID del alumno: ");
                idAlumno = scanner.nextInt();
                entradaCorrectaIdAlumno = true;
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingrese un número entero para el ID del alumno.");
                scanner.nextLine(); // Limpiar el buffer del scanner
            }
        } while (!entradaCorrectaIdAlumno);

        String sql = "SELECT * FROM alumnos JOIN alumnos_asignaturas ON alumnos.id_alumno = alumnos_asignaturas.id_alumno JOIN asignaturas ON alumnos_asignaturas.id_asignatura = asignaturas.id_asignatura WHERE alumnos.id_alumno = ?";
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, idAlumno);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            System.out.println("ID Alumno: " + rs.getInt("id_alumno"));
            System.out.println("Apellidos: " + rs.getString("apellidos"));
            System.out.println("Nombre: " + rs.getString("nombre"));
            System.out.println("Curso: " + rs.getInt("curso"));
            System.out.println("Titulación: " + rs.getInt("titulacion"));
            System.out.println("ID Asignatura: " + rs.getInt("id_asignatura"));
            System.out.println("Tipo: " + rs.getString("tipo"));
            System.out.println("Nombre Asignatura: " + rs.getString("nombre"));
            System.out.println("Creditos: " + rs.getFloat("creditos"));
            System.out.println("Cursada: " + (rs.getBoolean("cursada") ? "Sí" : "No"));
            System.out.println("-----------------------------");
        }
        rs.close();
    }
}
