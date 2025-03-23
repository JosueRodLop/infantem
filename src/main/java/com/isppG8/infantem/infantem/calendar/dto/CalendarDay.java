package com.isppG8.infantem.infantem.calendar.dto;

import java.util.List;

import com.isppG8.infantem.infantem.disease.dto.DiseaseSummary;
import com.isppG8.infantem.infantem.dream.dto.DreamSummary;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CalendarDay {
    private Integer babyId;
    private List<DreamSummary> dreams;
    private List<DiseaseSummary> diseases;

    public CalendarDay(Integer babyId) {
        this.babyId = babyId;
    }
}
