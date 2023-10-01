package com.advenio.medere.emr.objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sector {
    @Id
    private Long sector;
    private String name;
    private String description;
    @OneToOne
    @JoinColumn(name = "sectormanager")
    private User sector_manager;
}
