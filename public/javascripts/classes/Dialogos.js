/*
	Classe Dialogos
		Cuida das caixas de "diálogo" (erro, informação, confirmação, loading)
*/

var Dialogos = {
	erro: {
		/**
		 * Exibe a caixa de erro.
		 * @param mensagem Mensagem a ser exibida na caixa.
		**/
		mostrar: function(mensagem) {
			$("#errorDivMsg").html(mensagem);
			$("#errorDiv").show(500);
		},

		/**
		 * Esconde a caixa de erro.
		**/
		esconder: function() {
			$("#errorDiv").hide(500);
		}
	},

	confirmar: {
		/**
		 * Mostra a caixa de confirmação.
		 * @param mensagem Mensagem a ser exibida na caixa.
		 * @param data Dados que serão passados para a função de Confirmar/Cancelar.
		 * @return Retorna uma referência para o objeto 'confirmar' de Dialogos.
		**/
		mostrar: function(mensagem, data) {
			this.__data = data;
			$("#confirmBoxDivMsg").html(mensagem + "<br />");
			$("#confirmBoxDivButtonOk").click(this.__aoSelecionarOpcao);
			$("#confirmBoxDivButtonCancel").click(this.__aoSelecionarOpcao);
			$("#confirmBoxDiv").show(500);
			return this;
		},

		/**
		 * Altera a função de confirmação.
		 * @param funcao A nova função.
		 * @return Retorna uma referência para o objeto 'confirmar' de Dialogos.
		**/
		aoConfirmar: function(funcao) {
			$("#confirmBoxDivButtonOk").click(function() { funcao(Dialogos.confirmar.__data); });
			return this;
		},

		/**
		 * Altera a função de cancelamento.
		 * @param funcao A nova função.
		 * @return Retorna uma referência para o objeto 'confirmar' de Dialogos.
		**/
		aoCancelar: function(funcao) {
			$("#confirmBoxDivButtonCancel").click(function() { funcao(Dialogos.confirmar.__data); });
			return this;
		},

		/**
		 * Esconde a caixa de confirmar.
		**/
		esconder: function() {
			$("#confirmBoxDiv").hide(500);
		},

		/**
		 * Função padrão usada para Confirmar/Cancelar, quando nenhuma é informada.
		**/
		__aoSelecionarOpcao: function() {
			Dialogos.confirmar.esconder();
		}
	},

	loading: {
		/**
		 * Exibe o loading.
		**/
		mostrar: function() {
			$("#loadingDiv").show();
		},

		/**
		 * Esconde o loading.
		**/
		esconder: function() {
			$("#loadingDiv").hide();
		}
	},

	info: {
		/**
		 * Exibe a caixa de informação.
		 * @param mensagem Mensagem a ser exibida na caixa.
		**/
		mostrar: function(mensagem) {
			$("#infoDiv").html(mensagem);
			$("#infoDiv").show(500);
		},

		/**
		 * Esconde a caixa de informação.
		**/
		esconder: function() {
			$("#infoDiv").hide(500);
		}
	}
};