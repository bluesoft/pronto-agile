<%@ include file="/commons/taglibs.jsp"%>
<c:url var="ticketEditarUrl" value="/ticket/editar.action"/>
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
		<style type="text/css">
			.pendente { color: red}
			.executado { color: #0066cc}
		</style>
		
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
			
			<input type="hidden" name="ticketKey" value="${param.ticketKey}"/>
			
			<form:hidden path="script.scriptKey"/>
			
			<div class="group">
				
				<div>
					<form:input path="script.descricao" cssClass="required" size="90"/>
					<p>Descrição</p>
				</div>
				
				<c:if test="${script.ticket ne null}">
					<div>
						<a href="${ticketEditarUrl}?ticketKey=${script.ticket.ticketKey}"><pronto:icons name="estoria.png" title="Ir para Estória" /></a>
						<b>${script.ticket.ticketKey}</b> ${script.ticket.titulo}
						<p>Estória</p>
					</div>
				</c:if>
				
				<div>
					<form:textarea path="script.script" id="script" cssClass="mono"/>
					<p>Script</p>
				</div>

				<h5>Executar nos Bancos de Dados:</h5>
				
				<ul>
					<li><input type="checkbox" name="todos" onchange="selecionarTodos();"/> <span id="todos">Todos</span></li>
					<c:forEach items="${bancos}" var="b">
						<c:set var="checked" value="false"/>
						<c:set var="pendente" value="false"/>
						<c:set var="executado" value="false"/>
						<c:forEach var="e" items="${script.execucoes}">
							<c:if test="${e.bancoDeDados.bancoDeDadosKey eq b.bancoDeDadosKey}">
								<c:set var="checked" value="true"/>
								<c:set var="pendente" value="${e.pendente}"/>
								<c:set var="executado" value="${e.executado}"/>
							</c:if>
						</c:forEach>
						<li>
							<input type="checkbox" name="bancoDeDadosKey" value="${b.bancoDeDadosKey}"  ${checked ? 'checked="checked"' : ''} onchange="bancos()"/> 
							${b.nome} 
							${pendente ? '<span class="pendente">(Pendente)</span>' : ''}
							${executado ? '<span class="executado">(Executado)</span>' : ''}
						</li> 
					</c:forEach>
				</ul>
				
				<div align="center">
					<button type="button" onclick="voltar()">Cancelar</button>
					<button type="button" onclick="salvar()">Salvar</button><br/>
				</div>
				
			</div>
		</form>		
	</body>
</html>