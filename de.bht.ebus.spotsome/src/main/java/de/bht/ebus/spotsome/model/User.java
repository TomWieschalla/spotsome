package de.bht.ebus.spotsome.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import de.bht.ebus.spotsome.util.JsonViews;

@Entity
@Table(name = "app_user")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = "User.findByAccessTokenStr", 
		query ="SELECT u from User u JOIN u.accessToken a where a.token = ?1"),
	@NamedQuery(name = "User.findBySocialIdentity",
		query = "SELECT u FROM User u JOIN u.socialIdentity s WHERE s.id = ?1")})
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	@JsonView({JsonViews.Public.class, JsonViews.Sensible.class})
	private Long userId;
	
    @Column(name = "user_name")
    @JsonView({JsonViews.Public.class, JsonViews.Sensible.class})
    private String username;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "social_identity_fk")
    @JsonIgnore
    private SocialIdentity socialIdentity;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "access_token_fk")
    @JsonView(JsonViews.Sensible.class)
    private AccessToken accessToken;
    
    @Column(name = "device_token")
    @JsonIgnore
    private String deviceToken;
	
	public User(String username, SocialIdentity socialId, String deviceToken) {
		this.username = Objects.requireNonNull(username, "Username cannot be null.");
		this.socialIdentity = Objects.requireNonNull(socialId, "Social id cannot be null.");
		this.accessToken = new AccessToken();
		this.deviceToken = Objects.requireNonNull(deviceToken, "Device token cannot be null.");
	}
	
	protected User() { }

	public Long getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}
	
	public SocialIdentity getSocialIdentity() {
		return socialIdentity;
	}
	
	public AccessToken getAccessToken() {
		return accessToken;
	}
	
	public void setAccessToken(AccessToken accessToken) {
		this.accessToken = Objects.requireNonNull(accessToken);
	}
	
	public String getDeviceToken() {
		return deviceToken;
	}
	
	public void setDeviceToken(String deviceToken) {
		this.deviceToken = Objects.requireNonNull(deviceToken);
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		User other = (User) obj;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}
	
}
