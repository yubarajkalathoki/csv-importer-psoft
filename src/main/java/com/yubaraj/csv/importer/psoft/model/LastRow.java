package com.yubaraj.csv.importer.psoft.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@SuppressWarnings("serial")
@Entity
@Table(name = "lastRow")
public class LastRow implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "firstId")
    private Long firstId;

    @Column(name = "lastId")
    private Long lastId;

    /**
     * 
     */
    public LastRow() {
	super();
    }

    /**
     * @param id
     */
    public LastRow(Long id) {
	super();
	this.id = id;
    }

    /**
     * @return the id
     */
    public Long getId() {
	return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @return the firstId
     */
    public Long getFirstId() {
	return firstId;
    }

    /**
     * @param firstId
     *            the firstId to set
     */
    public void setFirstId(Long firstId) {
	this.firstId = firstId;
    }

    /**
     * @return the lastId
     */
    public Long getLastId() {
	return lastId;
    }

    /**
     * @param lastId
     *            the lastId to set
     */
    public void setLastId(Long lastId) {
	this.lastId = lastId;
    }
}
