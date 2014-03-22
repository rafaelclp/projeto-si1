/*
	Classe Controlador
		Controla as ações do usuário referentes a requisições:
		alocar, desalocar e resetar disciplinas.
*/

var Controlador = {
	/**
	 * Inicializa o controlador.
	**/
	inicializar: function() {
		ControladorHTML.inicializar();
		ControladorHTML.desenharPaineis();
	},

	/**
	 * Altera o periodo que o usuario está cursando (requisita a mudança ao servidor).
	 * @param periodo Novo período.
	 */
	alterarPeriodoCursando: function(periodo) {
		this.__requisitarPagina("/alterarPeriodoCursando/" + periodo);
	},

	/**
	 * Seta o periodo que o usuario está cursando.
	 * @param periodo Novo período.
	 */
	setarPeriodoCursando: function(periodo) {
		Grade.periodoCursando = periodo;
	},

	/**
	 * Tenta logar um usuario@senha.
	 * @param usuario Nome de usuário a ser logado.
	 * @param senha Senha do usuário.
	 */
	logar: function(usuario, senha) {
		data = {
			"usuario": usuario,
			"senha": senha
		};

		var jqxhr = this.__requisitarPagina("/logar/", false, data);

		jqxhr.aoTratarRequisicao = function(data, textStatus) {
			var parts = data.split(":");
			if (parts[0] == "sucesso") {
				location.href = "/";
				return true;
			}
			return false;
		};
	},

	/**
	 * Tenta registrar um usuário.
	 * @param nome Nome do usuário.
	 * @param usuario Nome de usuário a ser logado.
	 * @param senha Senha do usuário.
	 */
	registrar: function(nome, usuario, senha) {
		data = {
			"nome": nome,
			"usuario": usuario,
			"senha": senha
		};

		var jqxhr = this.__requisitarPagina("/registrar/", false, data);

		jqxhr.aoTratarRequisicao = function(data, textStatus) {
			var parts = data.split(":");
			if (parts[0] == "sucesso") {
				location.href = "/";
				return true;
			}
			return false;
		};
	},

	/**
	 * Altera a lista de disciplinas da grade.
	 * @param lista_de_listas Uma lista com listas no formato:
	 *			[id, "nome", creditos, dificuldade, periodo_previsto, periodo]
	 */
	setarDisciplinas: function(lista_de_disciplinas) {
		for (var i = 0; i < lista_de_disciplinas.length; i++)
			lista_de_disciplinas[i] = new Disciplina(lista_de_disciplinas[i]);
		Grade.setarDisciplinas(lista_de_disciplinas);
	},

	/**
	 * Requisita o reset da grade de disciplinas.
	**/
	resetarGrade: function() {
		Dialogos.confirmar.mostrar("Deseja resetar toda a grade de disciplinas alocadas?<br />")
			.aoConfirmar(function() {
				location.href = "/resetar";
			});
	},

	/**
	 * Move uma disciplina para outro periodo.
	 * @id ID da disciplina a ser movida.
	 * @periodo Período em que deve ser alocada.
	**/
	moverDisciplina: function(id, periodo) {
		var jqxhr = this.__requisitarPagina("moverDisciplina/" + id + "/" + periodo);

		jqxhr.aoTratarRequisicao = function(data, textStatus) {
			var parts = data.trim().split(":", 2);
			if (parts[0] == "irregulares")
				Grade.alocarDisciplina(id, periodo);
			return false;
		};
	},

	/**
	 * Requisita a alocação de uma disciplina.
	 * @id ID da disciplina a ser alocada.
	 * @periodo Período em que deve ser alocada.
	**/
	alocarDisciplina: function(id, periodo) {
		var jqxhr = this.__requisitarPagina("obterPreRequisitosNaoAlocados/" + id + "/" + periodo, true);

		jqxhr.aoTratarRequisicao = function(data, textStatus) {
			// parametros extra: id, periodo
			var parts = data.trim().split(":", 2);
			if (parts[0] == "ids") {
				var ids = parts[1].split(",");
				if (ids.length < 2) { // só tem ele mesmo
					Controlador.__requisitarPagina("alocarDisciplina/" + id + "/" + periodo);
					return true;
				}
				var nomes = [];
				for (var i = 0; i < ids.length; i++) {
					var disciplina = Grade.procurarDisciplina(parseInt(ids[i]));
					if (disciplina != null && ids[i] != id)
						nomes.push(disciplina.nome);
				}
				var erro = "Pré-requisitos não cumpridos:<br />" + GeradorHTML.gerarLista(nomes);
				Dialogos.erro.mostrar(erro);
			}
			return false;
		};
	},

	/**
	 * Requisita a desalocação de uma disciplina.
	 * @id ID da disciplina a ser desalocada.
	**/
	desalocarDisciplina: function(id) {
		var jqxhr = this.__requisitarPagina("obterPosRequisitosAlocados/" + id, true);

		jqxhr.aoTratarRequisicao = function(data, textStatus) {
			// parametros extra: id
			var parts = data.trim().split(":", 2);
			if (parts[0] == "ids") {
				var ids = parts[1].split(",");
				if (ids.length < 2) { // só tem ele mesmo
					Controlador.__requisitarPagina("desalocarDisciplina/" + id);
					return true;
				}
				var nomes = [];
				for (var i = 0; i < ids.length; i++) {
					var disciplina = Grade.procurarDisciplina(parseInt(ids[i]));
					if (disciplina != null && ids[i] != id)
						nomes.push(disciplina.nome);
				}
				var mensagemConfirmacao = "Ao desalocar esta disciplina, serão desalocadas também estas outras:<br />";
				mensagemConfirmacao += GeradorHTML.gerarLista(nomes);
				Dialogos.confirmar.mostrar(mensagemConfirmacao, "desalocarDisciplina/" + id)
					.aoConfirmar(function(pagina) {
						Controlador.__requisitarPagina(pagina);
					});
				return true;
			}
			return false;
		};
	},

	/**
	 * Requisita uma página por método POST (padrão) ou GET.
	 * @param url URI da página a ser requisitada.
	 * @param usarGET Usar método GET? (true: sim)
	 * @param data Dados a serem enviados para o servidor no formato {"chave": valor, ...}
	 * @return jqxhr da requisição em aberto.
	**/
	__requisitarPagina: function(url, usarGET, data) {
		Dialogos.loading.mostrar();
		var funcao = usarGET ? $.get : $.post;
		return funcao(url, data)
			.done(this.__tratarRequisicao)
			.fail(this.__aoFalharRequisicao);
	},

	/**
	 * Função padrão a ser executada quando uma requisição funciona
	 * e uma resposta é recebida.
	 * @param data Conteúdo da resposta da requisição.
	 * @param textStatus Status da resposta.
	 * @param jqxhr Objeto da requisição AJAX pelo jQuery (retornado por $.post/$.get)
	**/
	__tratarRequisicao: function(data, textStatus, jqxhr) {
		Dialogos.loading.esconder();

		//alert(data);
		if (jqxhr && jqxhr.aoTratarRequisicao)
			if (jqxhr.aoTratarRequisicao(data, textStatus))
				return; // true = resolveu toda a requisição

		var split = function(s,d,l) {
			l = l || 0;
			var lista = [];
			var k = 0;
			while (l && (k = s.search(d)) >= 0) {
				lista.push(s.substr(0, k));
				s = s.substr(k + d.length);
				l--;
			}
			lista.push(s);
			return lista;
		};

		var parts = split(data.trim(), ":", 1);
		if (parts.length < 2) return;

		var operacao = parts[0].trim();
		var parametros = parts[1].trim();

		switch(operacao) {
		case "alocar":
			parametros = parametros.split(",");
			var id = parseInt(parametros[0].trim());
			var periodo = parseInt(parametros[1].trim());
			Grade.alocarDisciplina(id, periodo);
			ControladorHTML.desenharPaineis();
			break;

		case "desalocar":
			parametros = parametros.split(",");
			var lista_de_ids = [];
			for (var i = 0; i < parametros.length; i++)
				lista_de_ids.push(parseInt(parametros[i].trim()));
			Grade.desalocarDisciplinas(lista_de_ids);
			ControladorHTML.desenharPaineis();
			break;

		case "irregulares":
			parametros = parametros.split(",");
			var disciplinas_irregulares = [];
			for (var i = 0; i < parametros.length; i++)
				if (parametros[i].trim().length > 0)
					disciplinas_irregulares.push(parseInt(parametros[i].trim()));
			Grade.alterarRegularidade(disciplinas_irregulares);
			ControladorHTML.desenharPaineis();
			break;

		case "periodoCursando":
			setarPeriodoCursando(parseInt(parametros.trim()));
			ControladorHTML.desenharPaineis();
			break;

		case "erro":
			Dialogos.erro.mostrar(parametros);
			break;
		}
	},

	/**
	 * Função padrão a ser executada quando uma requisição falha.
	**/
	__aoFalharRequisicao: function(data, textStatus, jqxhr) {
		Dialogos.loading.esconder();
		Dialogos.erro.mostrar("Não foi possível se conectar ao servidor.");
	}
};