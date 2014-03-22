package model;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

public class Usuario {
	
	@Id
	private int id;
	
	private String nome;
	
	private String usuario;
	
	private String senhaHasheada;
	
	private String salt;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Grade grade;
	
	public Usuario() {
		grade = new Grade();
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSenhaHasheada() {
		return senhaHasheada;
	}

	public void setSenhaHasheada(String senhaHasheada) {
		this.senhaHasheada = senhaHasheada;
	}
	
	
}
