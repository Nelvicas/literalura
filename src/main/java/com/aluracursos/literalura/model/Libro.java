package com.aluracursos.literalura.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name ="libros")

public class Libro {
    private Long id;
    private String titulo;
    private List<String> idioma;
    private List<DatosAutor> autor;
    private Integer descargas;
}
