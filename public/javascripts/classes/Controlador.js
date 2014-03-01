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
	 * Requisita a alocação de uma disciplina.
	 * @id ID da disciplina a ser alocada.
	 * @periodo Período em que deve ser alocada.
	**/
	alocarDisciplina: function(id, periodo) {
		this.__requisitarPagina("alocarDisciplina/" + id + "/" + periodo);
	},

	/**
	 * Requisita a desalocação de uma disciplina.
	 * @id ID da disciplina a ser desalocada.
	**/
	desalocarDisciplina: function(id) {
		this.__requisitarPagina("desalocarDisciplina/" + id + "/false");
	},

	/**
	 * Requisita uma página por método POST (padrão) ou GET.
	 * @param url URI da página a ser requisitada.
	 * @param usarGET Usar método GET? (true: sim)
	 * @param data Dados a serem enviados para o servidor no formato {"chave": valor, ...}
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

		var split = function(s,d,l) {
			l = l || 0;
			var lista = [];
			var k = 0;
			while (l && (k = s.search(d)) >= 0) {
				lista.push(s.substr(0, k));
				s = s.substr(k + d.length);
				l--;
			}
			if (s != "") lista.push(s);
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

		case "confirmar":
			parametros = split(parametros, ",", 1);
			var pagina = parametros[0];
			var mensagem = parametros[1];
			Dialogos.confirmar.mostrar(mensagem, pagina)
				.aoConfirmar(function(pagina) {
					Controlador.__requisitarPagina(pagina);
				});
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