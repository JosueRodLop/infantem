package com.isppG8.infantem.infantem.alimentNutrient;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import com.isppG8.infantem.infantem.nutrient.Nutrient;
import com.isppG8.infantem.infantem.ingesta.Alimento;


@Entity
@Table(name = "aliment_nutrient_table")
@Getter
@Setter
public class AlimentNutrient{

    //Id

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Atributos

    private Float amount;

    //Relaciones

    @ManyToOne
    private Nutrient nutrient;

    @ManyToOne
    private Alimento aliment;


}