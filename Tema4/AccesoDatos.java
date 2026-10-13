import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;
 

public class GestorAula {
 
    
    static File aula = new File("aula");
    static File documentos = new File("aula/documentos");
    static File copias = new File("aula/copias");
    static File notas = new File("aula/documentos/notas.txt");
    static File notasCopia = new File("aula/copias/notas_copia.txt");
    static File notasRespaldo = new File("aula/copias/notas_respaldo.txt");
 
    static Scanner teclado = new Scanner(System.in);
 
  
    public static void main(String[] args) {
        int opcion = -1;
 
        while (opcion != 0) {
            System.out.println();
            System.out.println("GESTOR DE ARCHIVOS DEL AULA ");
            System.out.println("1. Preparar el espacio de trabajo");
            System.out.println("2. Añadir una nota");
            System.out.println("3. Mostrar las notas");
            System.out.println("4. Copiar notas byte a byte");
            System.out.println("0. Salir");
 
            opcion = pedirNumero("Elige una opción: ");
 
            switch (opcion) {
                case 1:
                    prepararEspacio();
                    break;
                case 2:
                    anadirNota();
                    break;
                case 3:
                    mostrarNotas();
                    break;
                case 4:
                    copiarByteAByte();
                    break;
                case 0:
                    System.out.println("Saliendo del programa");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        }
        teclado.close();
    }
 
   
    static void prepararEspacio() {
       
        if (aula.exists()) {
            System.out.println("La carpeta aula ya existe.");
        } else {
            aula.mkdirs();
            System.out.println("Carpeta aula creada.");
        }
 
        if (documentos.exists()) {
            System.out.println(" carpeta documentos ya existe.");
        } else {
            documentos.mkdirs();
            System.out.println("Carpeta documentos creada.");
        }
 
        if (copias.exists()) {
            System.out.println(" carpeta copias ya existe.");
        } else {
            copias.mkdirs();
            System.out.println("Carpeta copias creada.");
        }
 
        try {
           
            if (notas.createNewFile()) {
                System.out.println("Fichero notas.txt creado.");
            } else {
                System.out.println("El fichero notas.txt ya existe.");
            }
        } catch (IOException e) {
            System.out.println("no se ha podido crear notas.txt.");
            return;
        }
 
        System.out.println("Nombre: " + notas.getName());
        System.out.println("Ruta absoluta: " + notas.getAbsolutePath());
       
        System.out.println("Último cambio : " + new Date(notas.lastModified()));
    }
 
    
    static void anadirNota() {
        if (!existeNotas()) {
            return;
        }
 
        System.out.print("Escribe la nota ");
        String nota = teclado.nextLine();
 
        if (nota.trim().isEmpty()) {
            System.out.println("La nota está vacía");
            return;
        }
 
       
        try (FileWriter escritor = new FileWriter(notas, true)) {
            escritor.write(nota + "\n");
            System.out.println("Nota guardada.");
        } catch (IOException e) {
            System.out.println("no se ha podido escribir en notas.txt.");
        }
    }
 
    static void mostrarNotas() {
        if (!existeNotas()) {
            return;
        }
 
        System.out.println("notas.txt");
        try (FileReader lector = new FileReader(notas)) {
            int data = lector.read();          
            while (data != -1) {               
                System.out.print((char) data); 
                data = lector.read();
            }
            System.out.println("---------------------");
        } catch (FileNotFoundException e) {
            System.out.println(" no se encuentra notas.txt.");
        } catch (IOException e) {
            System.out.println("Error al leer notas.txt.");
        }
    }
 
   
    static void copiarByteAByte() {
        if (!existeNotas()) {
            return;
        }
 
        if (notasCopia.exists() || notasRespaldo.exists()) {
            if (!confirmar("La copia ya existe. Quieres sobrescribirla (s/n): ")) {
                System.out.println("Copia cancelada.");
                return;
            }
           
            notasRespaldo.delete();
        }
 
        int bytesCopiados = 0;
 
        try (FileInputStream entrada = new FileInputStream(notas);
             FileOutputStream salida = new FileOutputStream(notasCopia)) {
 
            int data = entrada.read();     
            while (data != -1) {
                salida.write(data);        
                bytesCopiados++;
                data = entrada.read();
            }
 
        } catch (FileNotFoundException e) {
            System.out.println(" no se encuentra el fichero o la carpeta copias.");
            return;
        } catch (IOException e) {
            System.out.println(" al copiar el fichero.");
            return;
        }
 
        System.out.println("Bytes copiados: " + bytesCopiados);
        System.out.println("Tamaño de notas.txt: " + notas.length() + " bytes");
        System.out.println("Tamaño de notas_copia.txt: " + notasCopia.length() + " bytes");
 
        if (notas.length() == notasCopia.length()) {
            System.out.println("Los dos ficheros tienen el mismo tamaño.");
        } else {
            System.out.println("Los tamaños NO coinciden.");
        }
 
        
        if (notasCopia.renameTo(notasRespaldo)) {
            System.out.println("La copia se ha renombrado a notas_respaldo.txt");
        } else {
            System.out.println("No se ha podido renombrar la copia.");
        }
    }
 
    
 
    static boolean existeNotas() {
        if (!notas.exists()) {
            System.out.println("notas.txt no existe. Usa primero la opción 1.");
            return false;
        }
        return true;
    }
 
    
    static int pedirNumero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine();
            try {
                return Integer.parseInt(texto.trim());
            } catch (NumberFormatException e) {
                System.out.println("Tienes que escribir un número.");
            }
        }
    }
 
    static boolean confirmar(String mensaje) {
        System.out.print(mensaje);
        String respuesta = teclado.nextLine().trim().toLowerCase();
        return respuesta.equals("s");
    }
}