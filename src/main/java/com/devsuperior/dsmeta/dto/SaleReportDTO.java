package com.devsuperior.dsmeta.dto;

import java.time.LocalDate;

public class SaleReportDTO {

    private Long id;
    private LocalDate date;
    private Double amount;
    private String name;

    public SaleReportDTO(Long id, Double amount, LocalDate date, String name) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SaleReportDTO{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", name='" + name + '\'' +
                '}';
    }
}
