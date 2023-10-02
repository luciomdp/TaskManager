package com.advenio.medere.emr.objects;

import java.time.LocalDate;

import javax.persistence.Entity;
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
    private Long taskid;
    private String title;
    private String description;
    private LocalDate dateLimit;
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
    @JoinColumn(name = "sector")
    private Sector sector;
    @ManyToOne
    @JoinColumn(name = "category")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "priority")
    private Priority priority;


}
