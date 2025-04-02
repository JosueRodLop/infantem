package com.isppG8.infantem.infantem.metric.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MetricDTO {
    private Integer id;
    private Double weight;
    private Double height;
    private Double headCircumference;
    private Double armCircumference;
    private Double weightForHeight;

    public MetricDTO(Integer id, Double weight, Double height, Double headCircumference, Double armCircumference) {
        this.id = id;
        this.weight = weight;
        this.height = height;
        this.headCircumference = headCircumference;
        this.armCircumference = armCircumference;
        this.weightForHeight = calculateWeightForHeight();
    }

    private Double calculateWeightForHeight() {
        return (height != null && height > 0) ? weight / height : 0;
    }
}
