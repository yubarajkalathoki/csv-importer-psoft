package com.yubaraj.csv.importer.psoft.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Stores total number of deals per currency.
 *
 * @author Yuba Raj Kalathoki
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "dealDetails")
public class DealDetails implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "currencyIsoCode")
    private String currencyIsoCode;

    @Column(name = "countOfDeals")
    private Long countOfDeals;

    public DealDetails(Long id) {
	this.id = id;
    }

    public DealDetails() {
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getCurrencyIsoCode() {
	return currencyIsoCode;
    }

    public void setCurrencyIsoCode(String currencyIsoCode) {
	this.currencyIsoCode = currencyIsoCode;
    }

    public Long getCountOfDeals() {
	return countOfDeals;
    }

    public void setCountOfDeals(Long countOfDeals) {
	this.countOfDeals = countOfDeals;
    }

}
