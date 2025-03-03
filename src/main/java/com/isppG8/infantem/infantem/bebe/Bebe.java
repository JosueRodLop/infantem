package com.isppG8.infantem.infantem.bebe;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
public class Bebe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE	, generator = "entity_seq")
    private Integer Id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String nombre;

    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate fechaNacimiento;

    @NotBlank
    @Enumerated(EnumType.STRING)
    private Sexo sexo;

    @NotBlank
    private Double peso;

    @NotBlank
    private Integer altura;

    @NotBlank
    private Integer perimetroCefalico;

    @NotBlank
    private String preferenciaAlimenticia;

    //Relaciones

    
}


