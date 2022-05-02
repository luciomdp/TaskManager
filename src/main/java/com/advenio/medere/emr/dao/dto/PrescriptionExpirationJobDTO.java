package com.advenio.medere.emr.dao.dto;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PrescriptionExpirationJobDTO {

    private Long id;
    private String medereUUID;
    private String companyName;
    private String cronExpression;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMedereUUID() {
        return medereUUID;
    }

    public void setMedereUUID(String medereUUID) {
        this.medereUUID = medereUUID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getCronExpressionReadable() {
        Arrays.stream(cronExpression.split(" ")).skip(1).limit(2).collect(Collectors.toCollection(LinkedList::new))

                .descendingIterator().forEachRemaining(System.out::println);
        List<String> cronString = List.of(cronExpression.split(" "));

        return String.format("%s:%s", cronString.get(2), cronString.get(1));
    }
}
