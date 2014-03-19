package model;

public class Usuario {
	
	// Nome
	private String nome;
	
	// Login
	private String login;
	
	// Senha
	private String senha;
	
	// Grade
	private Grade grade;
	
	public Usuario(String nome, String login, String senha) {
		this.nome = nome;
		this.login = login;
		this.senha = senha;
		grade = new Grade();
	}

	public String getNome() {
		return nome;
	}

	public String getLogin() {
		return login;
	}

	public Grade getGrade() {
		return grade;
	}
}
