package com.advenio.medere.emr.objects;

import javax.persistence.Entity;

@Entity
public class Task {
    private Long taskid;
    private String title;
    private String description;
}
