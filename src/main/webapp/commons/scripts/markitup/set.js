mySettings = {
	previewParserPath:	'../wiki/parse.action', // path to your Wiki parser
	onShiftEnter:		{keepDefault:false, replaceWith:'\n\n'},
	markupSet: [
		{name:'Título 1', key:'1', openWith:'== ', closeWith:' ==', placeHolder:'Seu título aqui...' },
		{name:'Título 2', key:'2', openWith:'=== ', closeWith:' ===', placeHolder:'Seu título aqui...' },
		{name:'Título 3', key:'3', openWith:'==== ', closeWith:' ====', placeHolder:'Seu título aqui...' },
		{name:'Título 4', key:'4', openWith:'===== ', closeWith:' =====', placeHolder:'Seu título aqui...' },
		{name:'Título 5', key:'5', openWith:'====== ', closeWith:' ======', placeHolder:'Seu título aqui...' },
		{separator:'---------------' },		
		{name:'Negrito', key:'B', openWith:"'''", closeWith:"'''"}, 
		{name:'Itálico', key:'I', openWith:"''", closeWith:"''"}, 
		{name:'Riscado', key:'S', openWith:'<s>', closeWith:'</s>'}, 
		{separator:'---------------' },
		{name:'Lista de Bullets', openWith:'(!(* |!|*)!)'}, 
		{name:'Lista Numérica', openWith:'(!(# |!|#)!)'}, 
		{separator:'---------------' },
		{name:'Link', key:"L", openWith:"[[![Link]!] ", closeWith:']', placeHolder:'Your text to link here...' },
		{name:'Url', openWith:"[[![Url:!:http://]!] ", closeWith:']', placeHolder:'Your text to link here...' },
		{separator:'---------------' },
		{name:'Quotes', openWith:'(!(> |!|>)!)', placeHolder:''},
		{name:'Código Fonte', openWith:'(!(<source lang="[![Language:!:php]!]">|!|<pre>)!)', closeWith:'(!(</source>|!|</pre>)!)'}, 
		{separator:'---------------' },
		{name:'Preview', call:'preview', className:'preview'}
	]
};
