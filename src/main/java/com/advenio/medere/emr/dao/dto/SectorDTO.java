package com.advenio.medere.emr.dao.dto;

import java.math.BigDecimal;
import java.math.BigInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SectorDTO {

    private BigInteger sector;
    private String name;
    private String description;
    private String sectormanagermame;
    private String area;
    private BigInteger qtyemployeers;
    private BigDecimal loadfactor;
}
