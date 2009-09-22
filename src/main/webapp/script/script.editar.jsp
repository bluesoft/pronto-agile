<%@ include file="/commons/taglibs.jsp"%>
<html>
	<head>
		<title>Scripts</title>
		<script type="text/javascript">

		function voltar() {
			goTo('listar.action');
		}

		function salvar() {
			$('#form').submit();
		}

		function selecionarTodos() {
			$(':checkbox').attr('checked','checked');
			$('#todos').css('color','green');
		}

		function bancos() {
			if (todosEstadoSelecionados()) {
				selecionarTodos();
			} else {
				$('input[name="todos"]').removeAttr('checked');
				$('#todos').css('color','red');
			}
		}

		function todosEstadoSelecionados() {
			var todosSelecionados = true;
			$('input[name="bancoDeDadosKey"]').each(function (i,e){
				todosSelecionados = todosSelecionados && $(e).attr('checked'); 
			});
			return todosSelecionados;
		}

		$(function(){
			 $('#form').validate();
			 $('input[name=descricao]').get(0).focus();
			 bancos();
		});
		</script>
		
		
		<c:if test="${script.scriptKey eq 0}">
			<script>
				$(function(){
					selecionarTodos();
				});
			</script>	 	
		</c:if>
	</head>
	<body>
		
		<h1> ${script.scriptKey eq 0 ? 'Incluir' : 'Editar'} Script </h1>
		
		<form action="salvar.action" method="post" id="form">
			<form:hidden path="script.scriptKey"/>
			
			<div class="group">
					
				<div>
					<form:input path="script.descricao" cssClass="required" size="90"/>
					<p>Descrição</p>
				</div>
				<div>
					<form:textarea path="script.script" id="script" cssClass="mono"/>
					<p>Script</p>
				</div>

				<h5>Executar nos Bancos de Dados:</h5>
				
				<input type="checkbox" name="todos" onchange="selecionarTodos();"/> <span id="todos">Todos</span>
				
				<c:forEach items="${bancos}" var="b">
					<c:set var="checked" value="false"/>
					<c:forEach var="bd" items="${script.bancosDeDados}">
						<c:if test="${bd.bancoDeDadosKey eq b.bancoDeDadosKey}">
							<c:set var="checked" value="true"/>
						</c:if>
					</c:forEach>
					<input type="checkbox" name="bancoDeDadosKey" value="${b.bancoDeDadosKey}"  ${checked ? 'checked="checked"' : ''} onchange="bancos()"/> ${b.nome}
				</c:forEach>
				
				<div align="center">
					<button type="button" onclick="voltar()">Cancelar</button>
					<button type="button" onclick="salvar()">Salvar</button><br/>
				</div>
				
			</div>
		</form>		
	</body>
</html>