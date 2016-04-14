package de.bht.ebus.spotsome.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "social_identity")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = "SocialIdentity.findBySocialIdAndCompany", 
		query ="SELECT s from SocialIdentity s where s.socialId = ?1 and s.company = ?2")})
public class SocialIdentity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "social_id")
	private Long socialId;
	
	@Enumerated(EnumType.STRING)
	private Company company;
	
	public SocialIdentity(Long socialId, Company company) {
		this.socialId = Objects.requireNonNull(socialId);
		this.company = Objects.requireNonNull(company);
	}
	
	protected SocialIdentity() { }

	public Long getId() {
		return id;
	}
	
	public Long getSocialId() {
		return socialId;
	}
	
	public Company getCompany() {
		return company;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SocialIdentity other = (SocialIdentity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SocialIdentity [id=" + id + ", socialId=" + socialId
				+ ", company=" + company + "]";
	}
	
}
