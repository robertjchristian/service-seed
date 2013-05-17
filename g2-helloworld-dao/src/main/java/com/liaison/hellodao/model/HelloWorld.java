package com.liaison.hellodao.model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.List;


/**
 * The persistent class for the HELLO_WORLD database table.
 * 
 */
@Entity
@Table(name="HELLO_WORLD")
public class HelloWorld implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="HW_PGUID")
	private String hwPguid;

	private String name;

	@Column(name="SI_SGUID")
	private String siSguid;

	//bi-directional many-to-one association to HelloMoon
	@OneToMany(mappedBy="helloWorld", cascade={CascadeType.PERSIST}, fetch=FetchType.LAZY)
	private List<HelloMoon> helloMoons;

	public HelloWorld() {
	}

	public String getHwPguid() {
		return this.hwPguid;
	}

	public void setHwPguid(String hwPguid) {
		this.hwPguid = hwPguid;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSiSguid() {
		return this.siSguid;
	}

	public void setSiSguid(String siSguid) {
		this.siSguid = siSguid;
	}

	public List<HelloMoon> getHelloMoons() {
		return this.helloMoons;
	}

	public void setHelloMoons(List<HelloMoon> helloMoons) {
		this.helloMoons = helloMoons;
	}

}