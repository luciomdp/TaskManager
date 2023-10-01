package com.advenio.medere.emr.objects;

import javax.persistence.Entity;

@Entity
public class Sector {
    private Long sectorid;
    private String name;
    private User sector_manager;
}
