package com.advenio.medere.emr.objects;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
@Entity
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long task;
    private String title;
    private String description;
    private LocalDate datelimit;
    @ManyToOne
    @JoinColumn(name = "parenttask")
    private Task parentTask;
    @ManyToOne
    @JoinColumn(name = "state")
    private State state;
    @ManyToOne
    @JoinColumn(name = "owner")
    private User owner;
    @ManyToOne
    @JoinColumn(name = "solver")
    private User solver;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "priority")
    private Priority priority;


}
