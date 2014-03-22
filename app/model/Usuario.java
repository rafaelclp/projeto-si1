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
	
	public Usuario(String nome, String usuario, String senha) {
		setNome(nome);
		setUsuario(usuario);
		setSenha(senha);
		setGrade(new Grade());
	}

	private String gerarSalt() {
		return (new BigInteger(128, new SecureRandom())).toString(32);
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

	public void setSenha(String senha) {
		setSalt(gerarSalt());
		setSenhaHasheada(hashearSenha(senha, salt));
	}

	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}
	
	public static boolean existeUsuario(String usuario) {
		List<Usuario> listaUsuarios = Ebean.find(Usuario.class)
				.where()
					.eq("usuario", usuario)
				.findList();

		return listaUsuarios.size() >= 1;
	}

	private static boolean ehUsuarioValido(String usuario) {
		return usuario.length() >= TAMANHO_USUARIO_MINIMO && usuario.length() <= TAMANHO_USUARIO_MAXIMO;
	}

	private static boolean ehNomeValido(String nome) {
		return nome.length() >= TAMANHO_NOME_MINIMO && nome.length() <= TAMANHO_NOME_MAXIMO;
	}

	private static boolean ehSenhaValida(String senha) {
		return senha.length() >= TAMANHO_SENHA_MINIMO && senha.length() <= TAMANHO_SENHA_MAXIMO;
	}

	public static void registrar(String nome, String usuario, String senha) throws InvalidOperationException {
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
	}
	
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
