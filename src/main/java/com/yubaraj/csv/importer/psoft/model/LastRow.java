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

    @Column(name = "rowId")
    private Long rowId;

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
     * @return the rowId
     */
    public Long getRowId() {
	return rowId;
    }

    /**
     * @param rowId
     *            the rowId to set
     */
    public void setRowId(Long rowId) {
	this.rowId = rowId;
    }

}
