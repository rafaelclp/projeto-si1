package model;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import play.db.ebean.Model;

import com.avaje.ebean.Ebean;

/**
 * 
 * Classe que representa o aluno
 *
 */
@Entity
public class Usuario extends Model {
	private static final long serialVersionUID = 8941221341321L;

	private static final int TAMANHO_USUARIO_MINIMO = 4;
	private static final int TAMANHO_USUARIO_MAXIMO = 20;

	private static final int TAMANHO_NOME_MINIMO = 8;
	private static final int TAMANHO_NOME_MAXIMO = 64;

	private static final int TAMANHO_SENHA_MINIMO = 6;
	private static final int TAMANHO_SENHA_MAXIMO = 64;
	
	@Id
	private int id;

	private String nome;

	private String usuario;
	
	private String senhaHasheada;
	
	private String salt;
	
	@OneToOne(cascade=CascadeType.ALL)
	private Grade grade;
	
	/**
	 * Constructor
	 * 
	 * @param nome do aluno
	 * @param usuario do aluno
	 * @param senha do aluno
	 */
	public Usuario(String nome, String usuario, String senha) {
		setNome(nome);
		setUsuario(usuario);
		setSenha(senha);
		setGrade(new Grade());
	}

	/**
	 * Gera o salt
	 * 
	 * @return salt gerado
	 */
	private String gerarSalt() {
		return (new BigInteger(128, new SecureRandom())).toString(32);
	}

	/**
	 * Retorna o nome do aluno
	 * 
	 * @return nome do aluno
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * Atribui um nome ao aluno
	 * 
	 * @param nome atribuido
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * Retorna o usuario do aluno
	 * 
	 * @return usuario do aluno
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * Atribui um usuario ao aluno
	 * 
	 * @param usuario atribuido
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * Retorna o id do aluno
	 * 
	 * @return id do aluno
	 */
	public int getId() {
		return id;
	}

	/**
	 * Atribui um id ao aluno
	 * 
	 * @param id atribuido
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Retorna o salt
	 * 
	 * @return salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * Atribui um salt
	 * 
	 * @param salt atribuido
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * Retorna a senha do aluno apos passar pelo hash
	 * 
	 * @return senha hasheada
	 */
	public String getSenhaHasheada() {
		return senhaHasheada;
	}

	/**
	 * Atribui um valor a senha hasheada do aluno
	 * 
	 * @param senhaHasheada atribuida
	 */
	public void setSenhaHasheada(String senhaHasheada) {
		this.senhaHasheada = senhaHasheada;
	}
	
	/**
	 * Faz o hash da senha do aluno
	 * 
	 * @param senha do aluno
	 * @param salt
	 * @return senha hasheada
	 */
	private static String hashearSenha(String senha, String salt) {
		String senhaHasheada = null;
		try {
			byte[] senhaComSalt = (senha+salt).getBytes("UTF-8");
			MessageDigest md = MessageDigest.getInstance("MD5");

			byte[] senhaMD5 = md.digest(senhaComSalt);
			senhaHasheada = senhaMD5.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return senhaHasheada;
	}

	/**
	 * Atribui um valor a senha do aluno
	 * 
	 * @param senha valor atribuido
	 */
	public void setSenha(String senha) {
		setSalt(gerarSalt());
		setSenhaHasheada(hashearSenha(senha, salt));
	}

	/**
	 * Retorna a grade do aluno
	 * 
	 * @return grade do aluno
	 */
	public Grade getGrade() {
		return grade;
	}

	/**
	 * Atribui uma grade ao aluno
	 * 
	 * @param grade atribuida
	 */
	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	/**
	 * Verifica se o usuario ja existe
	 * 
	 * @param usuario que se quer verificar se existe
	 * @return se o usuario existe
	 */
	public static boolean existeUsuario(String usuario) {
		List<Usuario> listaUsuarios = Ebean.find(Usuario.class)
				.where()
					.eq("usuario", usuario)
				.findList();

		return listaUsuarios.size() >= 1;
	}

	/**
	 * Verifica se o usuario e valido
	 * 
	 * @param usuario que se quer verificar se e valido
	 * @return se o usuario e valido
	 */
	private static boolean ehUsuarioValido(String usuario) {
		return usuario.length() >= TAMANHO_USUARIO_MINIMO && usuario.length() <= TAMANHO_USUARIO_MAXIMO;
	}

	/**
	 * Verifica se o nome e valido
	 * 
	 * @param nome que se quer verificar se e valido
	 * @return se o nome e valido
	 */
	private static boolean ehNomeValido(String nome) {
		return nome.length() >= TAMANHO_NOME_MINIMO && nome.length() <= TAMANHO_NOME_MAXIMO;
	}

	/**
	 * Verifica se a senha e valida
	 * 
	 * @param senha que se quer verificar se e valida
	 * @return se a senha e valida
	 */
	private static boolean ehSenhaValida(String senha) {
		return senha.length() >= TAMANHO_SENHA_MINIMO && senha.length() <= TAMANHO_SENHA_MAXIMO;
	}

	/**
	 * Cria o usuario
	 * 
	 * @param nome do aluno
	 * @param usuario do aluno
	 * @param senha do aluno
	 * @throws InvalidOperationException se nao puder criar o usuario
	 */
	public static Usuario registrar(String nome, String usuario, String senha) throws InvalidOperationException {
		if (!ehUsuarioValido(usuario)) {
			throw new InvalidOperationException("O usuário é inválido.");
		}
		if (!ehNomeValido(nome)) {
			throw new InvalidOperationException("O nome é inválido.");
		}
		if (!ehSenhaValida(senha)) {
			throw new InvalidOperationException("A senha é inválida.");
		}
		if (existeUsuario(usuario)) {
			throw new InvalidOperationException("O usuário informado já está cadastrado.");
		}

		Usuario u = new Usuario(nome, usuario, senha);
		u.save();
		return u;
	}
	
	/**
	 * Loga o usuario
	 * 
	 * @param usuario do aluno
	 * @param senha do aluno
	 * @return o usuario que foi logado
	 * @throws InvalidOperationException caso nao possa logar o usuario
	 */
	public static Usuario logar(String usuario, String senha) throws InvalidOperationException {
		Usuario u = null;
		List<Usuario> listaUsuarios = Ebean.find(Usuario.class)
				.where()
					.eq("usuario", usuario)
				.findList();

		if (listaUsuarios.size() >= 1) {
			u = listaUsuarios.get(0);
			if (!u.getSenhaHasheada().equals(hashearSenha(senha, u.getSalt()))) {
				throw new InvalidOperationException("A senha está incorreta.");
			}
		} else {
			throw new InvalidOperationException("O usuário é inexistente.");
		}
		return u;
	}
}
