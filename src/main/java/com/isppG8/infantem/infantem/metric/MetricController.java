package com.isppG8.infantem.infantem.metric;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.isppG8.infantem.infantem.baby.Baby;
import com.isppG8.infantem.infantem.baby.BabyService;
import com.isppG8.infantem.infantem.exceptions.ResourceNotFoundException;
import com.isppG8.infantem.infantem.exceptions.ResourceNotOwnedException;
import com.isppG8.infantem.infantem.metric.dto.MetricDTO;

@RestController
@RequestMapping("/api/v1/metrics")
public class MetricController {

    private final MetricService metricService;

    private final BabyService babyService;

    @Autowired
    public MetricController(MetricService metricService, BabyService babyService) {
        this.metricService = metricService;
        this.babyService = babyService;
    }

    @PostMapping
    public ResponseEntity<Metric> createMetric(@RequestBody Metric metricRequest, @RequestParam Integer babyId) {
        try {
            // Obtener el bebé desde el servicio
            Baby baby = babyService.findById(babyId);
            if (baby == null) {
                return ResponseEntity.badRequest().build();
            }
            
            // Crear y configurar la métrica
            Metric metric = new Metric();
            metric.setWeight(metricRequest.getWeight());
            metric.setHeight(metricRequest.getHeight());
            metric.setHeadCircumference(metricRequest.getHeadCircumference());
            metric.setArmCircumference(metricRequest.getArmCircumference());
            metric.setDate(metricRequest.getDate());
            metric.setBaby(baby); // Asignar el bebé obtenido

            Metric createdMetric = metricService.createMetric(metric);
            return new ResponseEntity<>(createdMetric, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetricDTO> getMetricById(@PathVariable Long id) {
        Metric metric = metricService.getMetricById(id);
        MetricDTO metricDTO = new MetricDTO(metric.getId(), metric.getWeight(), metric.getHeight(),
                metric.getHeadCircumference(), metric.getArmCircumference(), metric.getDate());
        return ResponseEntity.ok(metricDTO);
    }

    @GetMapping("/baby/{babyId}")
    public ResponseEntity<List<MetricDTO>> getAllMetricsByBabyId(@PathVariable Integer babyId) {
        List<MetricDTO> metrics = metricService.getAllMetricsByBabyId(babyId).stream()
                .map(metric -> new MetricDTO(metric.getId(), metric.getWeight(), metric.getHeight(),
                        metric.getHeadCircumference(), metric.getArmCircumference(), metric.getDate()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(metrics);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Metric> updateMetric(@PathVariable Long id, @RequestBody Metric metric) {
        Metric updatedMetric = metricService.updateMetric(id, metric);
        return ResponseEntity.ok(updatedMetric);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetric(@PathVariable Long id) {
        metricService.deleteMetric(id);
        return ResponseEntity.noContent().build();
    }
}
