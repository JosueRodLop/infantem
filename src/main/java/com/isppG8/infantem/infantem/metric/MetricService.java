package com.isppG8.infantem.infantem.metric;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.isppG8.infantem.infantem.metric.dto.MetricSummary;

@Service
public class MetricService {

    private MetricRepository metricRepository;

    @Autowired
    public MetricService(MetricRepository metricRepository) {
        this.metricRepository = metricRepository;
    }

    // TODO: finish service

    // Methods for calendar

    public List<Date> getMetricsByUserIdAndDate(Integer babyId, Date start, Date end) {
        LocalDate startDate = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endDate = end.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return this.metricRepository.findMetricsByUserIdAndDate(babyId, startDate, endDate);
    }

    public List<MetricSummary> getMetricSummaryByBabyIdAndDate(Integer babyId, LocalDate day) {
        return this.metricRepository.findMetricSummaryByBabyIdAndDate(babyId, day);
    }
}
