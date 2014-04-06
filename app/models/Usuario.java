package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

/**
 * Entidade responsável por armazenar as informações de um usuário,
 * incluindo nome (real e de usuário), senha (hasheada), salt e grade.
 */
@Entity
public class Usuario extends Model {
	private static final long serialVersionUID = 8941221341321L;

	@Id
	private Long id;

	private String nome;

	private String usuario;
	
	private String senhaHasheada;
	
	private String salt;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Grade grade;
	
	/**
	 * Construtor de novo usuário
	 * 
	 * @param nome Nome do aluno
	 * @param usuario Nome de usuário do aluno
	 * @param senhaHasheada Senha, já hasheada, do aluno
	 * @param salt Salt usado ao hashear a senha
	 */
	public Usuario(String nome, String usuario, String senhaHasheada, String salt) {
		setNome(nome);
		setUsuario(usuario);
		setSenhaHasheada(senhaHasheada);
		setSalt(salt);
		setGrade(new Grade());
	}

	/**
	 * Obtém o nome real do usuário.
	 * 
	 * @return Nome real do usuário.
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Atribui um nome real ao usuário.
	 * 
	 * @param nome Nome atribuído.
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Obtém o nome de usuário deste usuário.
	 * 
	 * @return Nome de usuário.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Atribui um nome de usuário.
	 * 
	 * @param usuario Usuário atribuido.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Obtém o id do usuário
	 * 
	 * @return Id do aluno
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Atribui um id ao usuário
	 * 
	 * @param Id atribuído
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Obtém o salt usado para hashear a senha deste usuário.
	 * 
	 * @return Salt do usuário.
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * Atribui um salt.
	 * 
	 * @param salt Salt a ser atribuído.
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * Obtém a senha (hasehada) do usuário.
	 * 
	 * @return Senha hasheada
	 */
	public String getSenhaHasheada() {
		return senhaHasheada;
	}

	/**
	 * Altera a senha (hasheada) do usuário.
	 * 
	 * @param senhaHasheada Senha a ser atribuída
	 */
	public void setSenhaHasheada(String senhaHasheada) {
		this.senhaHasheada = senhaHasheada;
	}

	/**
	 * Retorna a grade do usuário.
	 * 
	 * @return Grade do usuário.
	 */
	public Grade getGrade() {
		return grade;
	}

	/**
	 * Atribui uma grade ao usuário.
	 * 
	 * @param grade Grade atribuída.
	 */
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
}
