/*
	Classe GeradorHTML
		Responsável por gerar códigos HTML (painéis, menus e botões) de
		acordo com alguns parâmetros.
*/

var GeradorHTML = {
	/**
	 * Gera um painel com o conteúdo especificado dentro.
	 * @param titulo Texto a ser exibido no título do painel.
	 * @param conteudo Conteúdo a ser exibido dentro do painel.
	 * @param altura Altura (height) do conteúdo do painel.
	 * @return O HTML do painel.
	**/
	gerarPainel: function(titulo, conteudo, altura, classe) {
		classe = classe || "panel-primary";
		var painel =  "";
		painel += '<div class="col-sm-4">';
		painel += '<div class="panel ' + classe + '">';
		painel += '<div class="panel-heading">';
		painel += '<h3 class="panel-title">' + titulo + '</h3>';
		painel += '</div>';
		painel += '<div class="panel-body" style="height: ' + altura + 'px">';
		painel += conteudo;
		painel += '</div>';
		painel += '</div>';
		painel += '</div>';
		return painel;
	},

	/**
	 * Gera um botão com o texto especificado.
	 * @param texto O texto a ser exibido no valor do botão.
	 * @param classe_cor Classe da cor (btn-[default,primary,success,info,warning,danger])
	 * @param classe_tamanho Classe do tamanho (btn-[lg, <empty>, sm, xs])
	 * @param outras_classes Classes adicionais para o botão, caso precise.
	 * @return O HTML do botão.
	**/
	gerarBotao: function(texto, classe_cor, classe_tamanho, outras_classes) {
		classe_tamanho = classe_tamanho || "btn-xs";
		classe_cor = classe_cor || "btn-default";
		outras_classes = outras_classes || "";

		var botao = "";
		//botao += '<p>';
		botao += '<button type="button" class="btn ' + classe_tamanho + ' ' + classe_cor + ' ' + outras_classes + '" style="text-align: left">';
		botao += texto;
		botao += '</button>';
		//botao += '</p>';
		return botao;
	},

	/**
	 * Gera um menu dropdown
	 * @param texto O texto que, quando clicado, exibe o menu.
	 * @param opcoes Lista de opções: [{'js': 'js_code', 'value': 'text to be shown'}, ...]
	 * @param incluir_scroll Incluir um scroll? O tamanho é fixo, ver classe CSS 'scrollable'.
	 * @return O HTML do menu gerado.
	**/
	gerarMenuDropdown: function(texto, opcoes, incluir_scroll) {
		var menu = "";
		menu += '<li class="dropdown menu-dropdown">';
		menu += '<a href="#" class="dropdown-toggle" data-toggle="dropdown">';
		menu += texto;
		menu += '</a>';
		menu += '<ul class="dropdown-menu' + (incluir_scroll ? ' scrollable' : '') + '">';

		for (var i = 0; i < opcoes.length; i++) {
			if (opcoes[i].type == "divider") {
				menu += '<li class="divider"></li>';
			} else if (opcoes[i].type == "header") {
				menu += '<li class="dropdown-header">' + opcoes[i].value + '</li>';
			} else {
				menu += '<li><a href="#!" onclick="' + opcoes[i].js + '">';
				menu += opcoes[i].value;
				menu += '</a></li>';
			}
		}

		menu += '</ul>';
		menu += '</li>';
		return menu;
	},

	/**
	 * Gera uma lista de entradas/elementos, um em cada linha, iniciados com um glyphicon-asterisk.
	 * @param entradas Valores a serem listados.
	 * @return O HTML da lista gerada.
	**/
	gerarLista: function(entradas) {
		var lista = "";
		for (var i = 0; i < entradas.length; i++)
			lista += '<span class=\"glyphicon glyphicon-asterisk\" style=\"font-size:10px\"></span> ' + entradas[i] + '<br />';
		return lista;
	}
};