package com.aluracursos.literalura.model;

import jakarta.persistence.*;

@Entity
@Table (name ="autores")

public class Autor {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String  nombre;
    private Integer añoNacimiento;
    private Integer añoFallecimiento;

}
