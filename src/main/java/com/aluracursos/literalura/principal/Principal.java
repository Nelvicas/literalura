package com.aluracursos.literalura.principal;

import com.aluracursos.literalura.model.Datos;
import com.aluracursos.literalura.model.DatosLibros;
import com.aluracursos.literalura.service.ConsumoAPI;
import com.aluracursos.literalura.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    String url = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosLibros> librosRegistrados = new ArrayList<>();



    public void muestraElMenu() {

        System.out.println("Elija una opcion a traves de su numero: ");
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar libro por titulo 
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un año determinado
                    5 - Listar libros por idioma
                    
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    librosReistrados();
                    break;
                case 3:
                    autoresRegistrados();
                    break;
                case 4:
                    autoresVivos();
                    break;
                case 5:
                    librosPorIdioma();
                    break;
                case 6:

                    break;
                default:
                    System.out.println("Ingrese un numero valido de las opciones a elegir");
                    break;


            }
        }
    }


    private void buscarLibroPorTitulo() {
        System.out.println("Ingrese el titulo del libro:");
        String tituloLibro = teclado.nextLine();

        String urlBusqueda = url + "?search=" + tituloLibro.replace(" ", "%20");

        String json = consumoApi.obtenerDatos(urlBusqueda);
        Datos datos = conversor.obtenerDatos(json, Datos.class);

        datos.resultados()
                .stream()
                .findFirst()
                .ifPresentOrElse(libro -> {
                    librosRegistrados.add(libro);

                    System.out.println("----- LIBRO ENCONTRADO -----");
                    System.out.println("Titulo: " + libro.titulo());
                    System.out.println("Autor: " +
                            libro.autor().stream()
                                    .map(a -> a.nombre())
                                    .findFirst()
                                    .orElse("Desconocido"));
                    System.out.println("Idioma: " +
                            libro.idioma().stream()
                                    .findFirst()
                                    .orElse("No informado"));
                    System.out.println("Descargas: " + libro.descargas());
                }, () -> {
                    System.out.println("No se encontró ningún libro con ese título.");
                });
    }


    private void librosReistrados() {
        if (librosRegistrados.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.println("----- LIBROS REGISTRADOS -----");
            for (DatosLibros libro : librosRegistrados) {
                System.out.println("Titulo: " + libro.titulo());
                System.out.println("Autor: " +
                        libro.autor().stream()
                                .map(a -> a.nombre())
                                .findFirst()
                                .orElse("Desconocido"));
                System.out.println("Idioma: " +
                        libro.idioma().stream()
                                .findFirst()
                                .orElse("No informado"));
                System.out.println("Descargas: " + libro.descargas());
                System.out.println("-------------------------------");
            }
        }



    }


    private void librosPorIdioma() {
        System.out.println("Ingrese el idioma (ej: en, es, fr):");
        String idiomaBuscado = teclado.nextLine().trim();

        List<DatosLibros> filtrados = librosRegistrados.stream()
                .filter(libro -> libro.idioma().stream()
                        .findFirst()
                        .map(id -> id.equalsIgnoreCase(idiomaBuscado))
                        .orElse(false))
                .toList();

        if (filtrados.isEmpty()) {
            System.out.println("No se encontraron libros en el idioma indicado.");
        } else {
            System.out.println("----- LIBROS EN EL IDIOMA " + idiomaBuscado + " -----");
            for (DatosLibros libro : filtrados) {
                System.out.println("Titulo: " + libro.titulo());
                System.out.println("Autor: " +
                        libro.autor().stream()
                                .map(a -> a.nombre())
                                .findFirst()
                                .orElse("Desconocido"));
                System.out.println("Descargas: " + libro.descargas());
                System.out.println("-------------------------------");
            }
        }
    }


    private void autoresRegistrados() {
        if (librosRegistrados.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("----- AUTORES REGISTRADOS -----");
        librosRegistrados.stream()
                .map(libro -> libro.autor().stream().findFirst().orElse(null))
                .distinct() // opcional: evita duplicados
                .forEach(autor -> {
                    if (autor != null) {
                        System.out.println("Nombre: " + autor.nombre());
                        System.out.println("Año de nacimiento: " +
                                (autor.añoNacimiento() != null ? autor.añoNacimiento() : "Desconocido"));
                        System.out.println("Año de fallecimiento: " +
                                (autor.añoFallecimiento() != null ? autor.añoFallecimiento() : "Desconocido"));
                        System.out.println("-------------------------------");
                    }
                });
    }


    private void autoresVivos() {
        if (librosRegistrados.isEmpty()) {
            System.out.println("No hay libros registrados.");
            return;
        }

        System.out.println("Ingrese el año:");
        int año = teclado.nextInt();
        teclado.nextLine();

        System.out.println("----- AUTORES VIVOS EN " + año + " -----");
        librosRegistrados.stream()
                .map(libro -> libro.autor().stream().findFirst().orElse(null))
                .distinct()
                .filter(autor -> autor != null &&
                        (autor.añoNacimiento() != null && autor.añoNacimiento() <= año) &&
                        (autor.añoFallecimiento() == null || autor.añoFallecimiento() >= año))
                .forEach(autor -> System.out.println(autor.nombre()));
    }
}