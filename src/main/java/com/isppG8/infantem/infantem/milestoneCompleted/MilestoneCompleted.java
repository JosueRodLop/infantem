package com.isppG8.infantem.infantem.milestoneCompleted;

import java.util.*;

import com.isppG8.infantem.infantem.milestone.Milestone;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class MilestoneCompleted {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Milestone milestone;

    private Date date;

    public MilestoneCompleted() {
    }
    
    public MilestoneCompleted(Milestone milestone) {
        this.milestone = milestone;
        this.date = new Date();
    }

    public MilestoneCompleted(Milestone milestone, Date fecha) {
        this.milestone = milestone;
        this.date = fecha;
    }

}