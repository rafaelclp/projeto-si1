package models;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;

import play.db.ebean.Model.Finder;

/**
 * Responsável por gerenciar o cadastro de usuários.
 * Sabe verificar se usuário, nome e senha são válidos,
 * sabe como a senha deve ser hasheada, e sabe se comunicar
 * com o bd para obter, logar e registrar usuários.
 */
public class CadastroDeUsuario {
	private static final int TAMANHO_USUARIO_MINIMO = 3;
	private static final int TAMANHO_USUARIO_MAXIMO = 20;

	private static final int TAMANHO_NOME_MINIMO = 3;
	private static final int TAMANHO_NOME_MAXIMO = 64;

	private static final int TAMANHO_SENHA_MINIMO = 6;
	private static final int TAMANHO_SENHA_MAXIMO = 64;

	/**
	 * Gera um salt aleatório.
	 * 
	 * @return O salt gerado.
	 */
	private String gerarSalt() {
		return (new BigInteger(64, new SecureRandom())).toString(32);
	}

	/**
	 * Gera o hash da senha com o salt.
	 * 
	 * @param senha Senha a ser aplicado o hash.
	 * @param salt Salt a ser aplicado o hash.
	 * @return Senha "hasheada".
	 */
	private String hashearSenha(String senha, String salt) {
		String senhaComSalt = (senha+salt);
		String senhaHasheada = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(senhaComSalt.getBytes(), 0, senhaComSalt.length());
			senhaHasheada = new BigInteger(1, md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return senhaHasheada;
	}

	/**
	 * Pesquisa uma lista de usuários por seu nome real.
	 * 
	 * @param query Nome (parcial) real do usuário que se quer obter.
	 * @return Lista de usuários cujo nome se parece com o especificado.
	 * @throws DataNotFoundException se não for possível obter um usuário.
	 */
	public List<Usuario> pesquisarUsuarioPorNome(String query) throws DataNotFoundException {
		/* Montamos uma query no formato "%<nome1>%<nome2>%...%<nomeN>%"
		 * assim, pesquisar por "Pedro de Lima" em vez de "Pedro Silva"
		 * para um usuário "Pedro Silva de Lima" também funcionará;
		 * assim como "Pedro ilva" ou "Pedro lima".
		 */
		String[] partesDaQuery = query.trim().split(" ");
		String queryFormatada = "%";
		
		for (String parte : partesDaQuery) {
			if (!parte.isEmpty()) {
				queryFormatada += parte + "%";
			}
		}

		Finder<String, Usuario> find = new Finder<String, Usuario>(String.class, Usuario.class);

		List<Usuario> listaUsuarios = find
				.where()
					.ilike("nome", queryFormatada)
					.setMaxRows(50)
				.findList();

		if (listaUsuarios.isEmpty()) {
			throw new DataNotFoundException("Não foi possível encontrar nenhum usuário com nome similar ao especificado.");
		}

		return listaUsuarios;
	}

	/**
	 * Obtém um usuário por seu nome de usuário.
	 * 
	 * @param usuario Nome de usuário que se quer obter.
	 * @return Usuário obtido.
	 * @throws DataNotFoundException se não for possível obter o usuário.
	 */
	public Usuario obterUsuarioPorNomeDeUsuario(String usuario) throws DataNotFoundException {
		Finder<String, Usuario> find = new Finder<String, Usuario>(String.class, Usuario.class);

		List<Usuario> listaUsuarios = find
				.where()
					.eq("usuario", usuario)
				.findList();

		if (listaUsuarios.isEmpty()) {
			throw new DataNotFoundException("O usuário especificado não existe.");
		}

		return listaUsuarios.get(0);
	}

	/**
	 * Obtém um usuário por seu id numérico.
	 * 
	 * @param id ID numérico do usuário a ser procurado.
	 * @return Usuário obtido.
	 * @throws DataNotFoundException se não for possível obter o usuário.
	 */
	public Usuario obterUsuarioPorId(Long id) throws DataNotFoundException {
		Finder<Long, Usuario> find = new Finder<Long, Usuario>(Long.class, Usuario.class);

		Usuario u = find.byId(id);

		if (u == null) {
			throw new DataNotFoundException("O id de usuário especificado não existe.");
		}

		return u;
	}

	/**
	 * Verifica se um usuário existe.
	 * 
	 * @param usuario Nome de usuário que se quer verificar.
	 * @return Se existe ou não o usuário.
	 */
	public boolean existeUsuario(String usuario) {
		try {
			obterUsuarioPorNomeDeUsuario(usuario);
		} catch (DataNotFoundException e) {
			return false;
		}
		return true;
	}

	/**
	 * Verifica se o usuario é válido.
	 * 
	 * @param usuario Usuário que se quer verificar se é válido.
	 * @return Se o usuário é válido.
	 */
	private boolean ehUsuarioValido(String usuario) {
		return usuario.length() >= TAMANHO_USUARIO_MINIMO && usuario.length() <= TAMANHO_USUARIO_MAXIMO;
	}

	/**
	 * Verifica se o nome é válido.
	 * 
	 * @param nome Nome que se quer verificar se é válido.
	 * @return Se o nome é válido.
	 */
	private boolean ehNomeValido(String nome) {
		return nome.length() >= TAMANHO_NOME_MINIMO && nome.length() <= TAMANHO_NOME_MAXIMO;
	}

	/**
	 * Verifica se a senha é válida.
	 * 
	 * @param senha Senha que se quer verificar se é válida.
	 * @return Se a senha é válida.
	 */
	private boolean ehSenhaValida(String senha) {
		return senha.length() >= TAMANHO_SENHA_MINIMO && senha.length() <= TAMANHO_SENHA_MAXIMO;
	}

	/**
	 * Tenta registrar um usuário.
	 * 
	 * @param nome Nome do aluno
	 * @param usuario Usuário do aluno
	 * @param senha Senha do aluno
	 * @throws InvalidOperationException Se não puder criar o usuario
	 */
	public Usuario registrar(String nome, String usuario, String senha) throws InvalidOperationException {
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

		String salt = gerarSalt();
		String senhaHasheada = hashearSenha(senha, salt);

		Usuario u = new Usuario(nome, usuario, senhaHasheada, salt);
		u.save();

		return u;
	}
	
	/**
	 * Loga o usuário com os dados especificados.
	 * 
	 * @param usuario Nome de usuário a ser autenticado.
	 * @param senha Senha do usuário especificado.
	 * @return O objeto do usuário que foi logado.
	 * @throws DataNotFoundException Caso não seja possível encontrar o usuário no bd.
	 * @throws InvalidOperationException Caso tente logar com uma senha inválida.
	 */
	public Usuario logar(String usuario, String senha) throws InvalidOperationException, DataNotFoundException {
		Usuario u = obterUsuarioPorNomeDeUsuario(usuario);
		String senhaHasheada = hashearSenha(senha, u.getSalt());
		
		if (!senhaHasheada.equals(u.getSenhaHasheada())) {
			throw new InvalidOperationException("A senha está incorreta.");
		}
		
		return u;
	}
}
