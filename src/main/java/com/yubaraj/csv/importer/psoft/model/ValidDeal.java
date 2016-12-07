package com.yubaraj.csv.importer.psoft.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Stores all valid deal details.
 *
 * @author Yuba Raj Kalathoki
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "validDeal")
public class ValidDeal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "dealUniqueId")
    private String dealUniqueId;

    @Column(name = "fromCurrencyIsoCode")
    private String fromCurrencyIsoCode;

    @Column(name = "toCurrencyIsoCode")
    private String toCurrencyIsoCode;

    @Column(name = "dealTimestamp")
    private String dealTimestamp;

    @Column(name = "dealAmount")
    private String dealAmount;

    @Column(name = "sourceFile")
    private String sourceFile;

    public ValidDeal() {
    }

    /**
     * @param dealUniqueId
     * @param fromCurrencyIsoCode
     * @param toCurrencyIsoCode
     * @param dealTimestamp
     * @param dealAmount
     * @param sourceFile
     */
    public ValidDeal(String dealUniqueId, String fromCurrencyIsoCode, String toCurrencyIsoCode, String dealTimestamp,
	    String dealAmount, String sourceFile) {
	super();
	this.dealUniqueId = dealUniqueId;
	this.fromCurrencyIsoCode = fromCurrencyIsoCode;
	this.toCurrencyIsoCode = toCurrencyIsoCode;
	this.dealTimestamp = dealTimestamp;
	this.dealAmount = dealAmount;
	this.sourceFile = sourceFile;
    }



    public String getDealUniqueId() {
	return dealUniqueId;
    }

    public void setDealUniqueId(String dealUniqueId) {
	this.dealUniqueId = dealUniqueId;
    }

    public String getFromCurrencyIsoCode() {
	return fromCurrencyIsoCode;
    }

    public void setFromCurrencyIsoCode(String fromCurrencyIsoCode) {
	this.fromCurrencyIsoCode = fromCurrencyIsoCode;
    }

    public String getToCurrencyIsoCode() {
	return toCurrencyIsoCode;
    }

    public void setToCurrencyIsoCode(String toCurrencyIsoCode) {
	this.toCurrencyIsoCode = toCurrencyIsoCode;
    }

    public String getDealTimestamp() {
	return dealTimestamp;
    }

    public void setDealTimestamp(String dealTimestamp) {
	this.dealTimestamp = dealTimestamp;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getDealAmount() {
	return dealAmount;
    }

    public void setDealAmount(String dealAmount) {
	this.dealAmount = dealAmount;
    }

    public String getSourceFile() {
	return sourceFile;
    }

    public void setSourceFile(String sourceFile) {
	this.sourceFile = sourceFile;
    }

}
