package de.bht.ebus.spotsome.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.data.annotation.AccessType;
import org.springframework.data.annotation.AccessType.Type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.util.AccessTokenGenerator;
import de.bht.ebus.spotsome.util.JsonViews;

@Entity
@Table(name = "access_token")
@AccessType(Type.FIELD)
@NamedQuery(name = "AccessToken.findByToken", query = "SELECT a from AccessToken a where a.token = ?1")
public class AccessToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	private Long id;

	@JsonView(JsonViews.Sensible.class)
	private String token;
	
	@Column(name = "creation_date")
	@JsonIgnore
	private Date creationDate;
	
	public AccessToken() {
		this.token = AccessTokenGenerator.generateAccessToken();
		this.creationDate = new Date();
	}
	
	public Long getId() {
		return id;
	}
	
	public String getToken() {
		return token;
	}
	
	public Date getCreationDate() {
		return new Date(creationDate.getTime());
	}
	
}
