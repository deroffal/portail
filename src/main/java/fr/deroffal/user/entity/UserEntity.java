package fr.deroffal.user.entity;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Entity
@Table(name = "UTILISATEUR", schema = "PUBLIC")
public class UserEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_utilisateur_generator")
	@SequenceGenerator(name = "seq_utilisateur_generator", sequenceName = "seq_utilisateur", allocationSize = 1)
	@Column(name = "ID", updatable = false, nullable = false)
	private Long id;

	@Column(name = "LOGIN", unique = true, nullable = false)
	private String login;

	@Column(name = "PASSWORD", nullable = false)
	private String password;

	@ManyToMany
	@JoinTable(
			name = "UTILISATEUR_ROLE",
			joinColumns = @JoinColumn(name = "USER_ID"),
			inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
	)
	private Collection<RoleEntity> roles;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(final String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(final String password) {
		this.password = password;
	}

	public Collection<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(final Collection<RoleEntity> roles) {
		this.roles = roles;
	}
}
