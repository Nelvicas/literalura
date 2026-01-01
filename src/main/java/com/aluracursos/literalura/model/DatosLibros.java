package com.aluracursos.literalura.model;


import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties (ignoreUnknown = true)

public record DatosLibros(

        @JsonAlias("title") String titulo,
        @JsonAlias("languages") List<String> idioma,
        @JsonAlias("authors") List<DatosAutor> autor,
        @JsonAlias("download_count") Integer descargas
        //@JsonAlias("summaries") String sinopsis

) {

}
