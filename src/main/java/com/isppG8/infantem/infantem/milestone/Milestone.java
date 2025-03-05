package com.isppG8.infantem.infantem.milestone;

import com.isppG8.infantem.infantem.milestoneCompleted.MilestoneCompleted;
import java.util.*;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Milestone {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MilestoneCompleted> milestonesCompleted = new ArrayList<>();

    public Milestone() {
    }

    public Milestone(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
