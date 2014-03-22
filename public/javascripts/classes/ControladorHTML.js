/*
	Classe ControladorHTML
		Controla o código HTML da página.
*/

var ControladorHTML = {
	/**
	 * Inicializa o timer.
	**/
	inicializar: function() {
		if (!this.inicializado) {
			this.inicializado = true;
			setInterval(function() { ControladorHTML.__atualizarInfo() }, 500);
		}
	},

	/**
	 * Desenha os painéis de períodos para o div "main-container"
	**/
	desenharPaineis: function() {
		var periodos = []; // armazena as disciplinas por período: periodos[<periodo>-1][<idx>]
		var maximo_de_disciplinas = [];

		for (var i = 0; i < Configuracoes.NUMERO_DE_PERIODOS; i++) {
			if (i % 3 == 0) maximo_de_disciplinas.push(0);
			periodos.push([]);
		}

		for (var i = 0; i < Grade.disciplinas.length; i++) {
			var obj = Grade.disciplinas[i];

			if (obj.periodo > 0) {
				periodos[obj.periodo-1].push(i);

				var j = parseInt((obj.periodo-1)/3);
				maximo_de_disciplinas[j] = Math.max(
					maximo_de_disciplinas[j],
					periodos[obj.periodo-1].length
				);
			}
		}

		var html = "";
		for (var i = 0; i < Configuracoes.NUMERO_DE_PERIODOS; i++) {
			var conteudo = this.__gerarMenuAdicionarDisciplina(i+1);
			var total_creditos = 0;
			var total_dificuldade = 0;

			for (var j = 0; j < periodos[i].length; j++) {
				var index = periodos[i][j];
				var obj = Grade.disciplinas[index];

				conteudo += this.__obterMenuDropdownDeDisciplina(index);
				total_creditos += obj.creditos;
				total_dificuldade += obj.dificuldade;
			}

			if (total_creditos > 0) {
				conteudo += this.__gerarBotaoDeDisciplina('Créditos: ' + total_creditos + ' | Dificuldade: ' + total_dificuldade, "btn-info");
			}

			var j = parseInt(i/3);
			var altura_por_disciplina = 32;
			var altura_base = 20 + 32;
			html += GeradorHTML.gerarPainel('Periodo ' + (i+1), conteudo,
				(maximo_de_disciplinas[j] > 0 ? maximo_de_disciplinas[j] + 1 : 0) * altura_por_disciplina + altura_base);
		}

		var yPosition = $(document).scrollTop();
		document.getElementById("main-container").innerHTML = html;
		window.scrollTo(0, yPosition);
	},

	/**
	 * Atualiza a caixa de informação com os períodos incompletos.
	**/
	__atualizarInfo: function() {
		// Verifica quais períodos não têm o mínimo de créditos
		// e os armazena em 'periodos'
		var creditos = [], periodos = [];
		for (var i = 0; i <= Configuracoes.NUMERO_DE_PERIODOS; i++)
			creditos.push(0);
		for (var i = 0; i < Grade.disciplinas.length; i++)
			creditos[Grade.disciplinas[i].periodo] += Grade.disciplinas[i].creditos;
		for (var i = 1; i < creditos.length; i++)
			if (creditos[i] < Configuracoes.MIN_CREDITOS)
				periodos.push(i);

		// Remove os períodos não obrigatórios que têm 0 créditos
		var p = Configuracoes.NUMERO_DE_PERIODOS;
		for (var i = periodos.length-1; i >= 0; i--) {
			if (periodos[i] < p) break;
			if (periodos[i] <= Configuracoes.MIN_PERIODOS) break;
			if (creditos[periodos[i]] > 0) break;

			p = periodos[i] - 1;
			periodos.pop();
		}

		// Exibe ou esconde a mensagem dos períodos incompletos
		if (periodos.length > 0) {
			var texto = "";
			for (var i = 0; i < periodos.length; i++) {
				texto += periodos[i];
				if (i+2 < periodos.length)
					texto += ", ";
				else if (i+1 < periodos.length)
					texto += " e ";
			}
			if (periodos.length == 1)
				texto = "O periodo " + texto + " ainda não possui o mínimo de créditos.";
			else
				texto = "Os periodos " + texto + " ainda não possuem o mínimo de créditos.";

			Dialogos.info.mostrar(texto);
		}
		else
			Dialogos.info.esconder();
	},

	/**
	 * Gera o menu "Adicionar disciplina..." para um certo período.
	 * @param periodo Período para o qual será gerado o menu.
	 * @return O HTML do menu gerado.
	**/
	__gerarMenuAdicionarDisciplina: function(periodo) {
		var disciplinas_desalocadas = [];
		for (var i = 0; i < Grade.disciplinas.length; i++)
			if (Grade.disciplinas[i].periodo == 0)
				disciplinas_desalocadas.push(i);

		var opcoes = [];
		var ultimo_periodo_adicionado = -1;
		for (var i = 0; i < disciplinas_desalocadas.length; i++) {
			var index = disciplinas_desalocadas[i];
			var obj = Grade.disciplinas[index];

			if (obj.periodo_previsto != ultimo_periodo_adicionado) {
				if (opcoes.length > 0)
					opcoes.push({"type": "divider"});
				if (obj.periodo_previsto == 0)
					opcoes.push({"type": "header", "value": "Optativas"});
				else
					opcoes.push({"type": "header", "value": "Periodo " + obj.periodo_previsto});
				ultimo_periodo_adicionado = obj.periodo_previsto;
			}

			opcoes.push({
				"value": GeradorHTML.gerarBotao(obj.creditos, "btn-info") + ' ' + obj.nome + ' <span style="color:#bbb">(dificuldade ' + obj.dificuldade + ')</span>',
				"js": "Controlador.alocarDisciplina(" + obj.id + ", " + periodo + ")"
			});
		}

		return GeradorHTML.gerarMenuDropdown(this.__gerarBotaoDeDisciplina("Adicionar disciplina...", "btn-info"), opcoes, true);
	},

	/**
	 * Gera o menu de de uma disciplina (com opção Desalocar)
	 * @param index Index da disciplina na lista Grade.disciplinas
	 * @return O HTML do menu
	**/
	__obterMenuDropdownDeDisciplina: function(index) {
		var obj = Grade.disciplinas[index];
		var menu = [
			{"js": "Controlador.desalocarDisciplina(" + obj.id + ")", "value": "Desalocar"}
			{"type": "divider"},
			{"type": "header", "value": "Mover para..."}
		];
		for (var i = 1; i <= Configuracoes.NUMERO_DE_PERIODOS; i++)
			if (i != obj.periodo)
				menu.push({"js": "Controlador.moverDisciplina(" + obj.id + ", " + i + ")", "value": "Periodo " + i});
		return GeradorHTML.gerarMenuDropdown(this.__gerarBotaoDeDisciplinaPorIndex(index), menu);
	},

	/**
	 * Gera um botão para uma disciplina (classe 'subject-button' em um botão comum)
	 * @param texto Texto a ser exibido no botão.
	 * @param classe_cor Classe da cor (ver função GeradorHTML.gerarBotao)
	 * @return O HTML do botão
	**/
	__gerarBotaoDeDisciplina: function(texto, classe_cor) {
		return "<p>" + GeradorHTML.gerarBotao(texto, classe_cor, "btn-xs", "subject-button") + "</p>";
	},

	/**
	 * Gera um botão para uma disciplina (classe 'subject-button' em um botão comum)
	 * @param index Index da disciplina na lista Grade.disciplinas
	 * @return O HTML do botão
	**/
	__gerarBotaoDeDisciplinaPorIndex: function(index) {
		var obj = Grade.disciplinas[index];
		return this.__gerarBotaoDeDisciplina(obj.creditos + ' <span style="color:#bbb">|</span> ' + obj.nome, obj.irregular ? "btn-danger" : "btn-default");
	}
};