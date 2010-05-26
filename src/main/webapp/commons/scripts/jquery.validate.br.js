/**
 * Translated default messages for the jQuery validation plugin.
 * Language: PT_BR
 * Translator: Francisco Ernesto Teixeira <fco_ernesto@yahoo.com.br>
 */
jQuery.extend(jQuery.validator.messages, {
	required: "Este campo &eacute; obrigatório.",
	remote: "Por favor, corrija este campo.",
	email: "Por favor, forne&ccedil;a um endere&ccedil;o eletr&ocirc;nico v&aacute;lido.",
	url: "Por favor, forne&ccedil;a uma URL v&aacute;lida.",
	date: "Por favor, forne&ccedil;a uma data v&aacute;lida.",
	dateBr: "Por favor, forne&ccedil;a uma data v&aacute;lida.",
	dateISO: "Por favor, forne&ccedil;a uma data v&aacute;lida (ISO).",
	dateDE: "Bitte geben Sie ein gÃ¼ltiges Datum ein.",
	number: "Por favor, forne&ccedil;a um n&uacute;mero v&aacute;lido.",
	numberDE: "Bitte geben Sie eine Nummer ein.",
	digits: "Por favor, forne&ccedil;a somente números inteiros.",
	creditcard: "Por favor, forne&ccedil;a um cart&atilde;o de cr&eacute;dito v&aacute;lido.",
	equalTo: "Por favor, forne&ccedil;a o mesmo valor novamente.",
	accept: "Por favor, forne&ccedil;a um valor com uma extens&atilde;o v&aacute;lida.",
	maxlength: jQuery.format("Por favor, forne&ccedil;a n&atilde;o mais que {0} caracteres."),
	minlength: jQuery.format("Por favor, forne&ccedil;a ao menos {0} caracteres."),
	rangelength: jQuery.format("Por favor, forne&ccedil;a um valor entre {0} e {1} caracteres de comprimento."),
	rangeValue: jQuery.format("Por favor, forne&ccedil;a um valor entre {0} e {1}."),
	range: jQuery.format("Por favor, forne&ccedil;a um valor entre {0} e {1}."),
	maxValue: jQuery.format("Por favor, forne&ccedil;a um valor menor que ou igual a {0}."),
	max: jQuery.format("Por favor, forne&ccedil;a um valor menor ou igual a {0}."),
	minValue: jQuery.format("Por favor, forne&ccedil;a um valor maior ou igual a {0}."),
	min: jQuery.format("Por favor, forne&ccedil;a um valor maior ou igual a {0}.")
	
});

$(function() {
	
	jQuery.validator.addMethod("dateBr", function(value, element) { 
		  return this.optional(element) || /^\d\d?\/\d\d?\/\d\d\d?\d?$/.test(value); 
	}, "Por favor, forne&ccedil;a uma data v&aacute;lida.");

	jQuery.validator.addClassRules("dateBr", {dateBr: true});
	
	jQuery.validator.addMethod("requiredCombo", function(value, element) { 
		  return this.optional(element) || (value != null && value > 0); 
	}, "Selecione um dos itens da lista.");

	jQuery.validator.addClassRules("requiredCombo", {requiredCombo: true});
		
}); 

/* Brazilian initialisation for the jQuery UI date picker plugin. */
/* Written by Leonildo Costa Silva (leocsilva@gmail.com). */
jQuery(function($){
	$.datepicker.regional['pt-BR'] = {clearText: 'Limpar', clearStatus: '',
		closeText: 'Fechar', closeStatus: '',
		prevText: '<--', prevStatus: '',
		nextText: '-->', nextStatus: '',
		currentText: 'Hoje', currentStatus: '',
		monthNames: ['Janeiro','Fevereiro','Mar&ccedil;o','Abril','Maio','Junho', 'Julho','Agosto','Setembro','Outubro','Novembro','Dezembro'],
		monthNamesShort: ['Jan','Fev','Mar','Abr','Mai','Jun', 'Jul','Ago','Set','Out','Nov','Dez'],
		monthStatus: '', yearStatus: '',
		weekHeader: 'Sm', weekStatus: '',
		dayNames: ['Domingo','Segunda','Ter&ccedil;a','Quarta','Quinta','Sexta','Sabado'],
		dayNamesShort: ['Dom','Seg','Ter','Qua','Qui','Sex','Sab'],
		dayNamesMin: ['D','S','T','Q','Q','S','S'],
		dayStatus: 'DD', dateStatus: 'D, M d',
		dateFormat: 'dd/mm/yy', firstDay: 0, 
		initStatus: '', isRTL: false};
	
	$.datepicker.setDefaults($.datepicker.regional['pt-BR']);
});
