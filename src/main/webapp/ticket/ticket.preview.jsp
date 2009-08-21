<%@ include file="/commons/taglibs.jsp"%>
${ticket.html}
<c:if test="${!empty anexos}">
<br/>
<h2>Anexos</h2>
	<ul style="list-style-type: none;">
	<c:forEach items="${anexos}" var="anexo">
		<li>
			<c:choose>
				<c:when test="${anexo.imagem}">
					<pronto:icons name="imagem.gif" title="Imagem ${anexo.extensao}"/>
				</c:when>
				<c:when test="${anexo.planilha}">
					<pronto:icons name="excel.png" title="Planílha ${anexo.extensao}"/>
				</c:when>
				<c:when test="${anexo.extensao eq 'pdf'}">
					<pronto:icons name="pdf.png" title="Arquivo PDF"/>
				</c:when>
				<c:otherwise>
					<pronto:icons name="anexo.png" title="${anexo.extensao}"/>
				</c:otherwise>
			</c:choose>
			
			${anexo.nomeParaExibicao}
			<pronto:icons name="download.gif" title="Baixar Anexo" onclick="goTo('download.action?ticketKey=${ticket.ticketKey}&file=${anexo}')"/>
			<pronto:icons name="excluir.png" title="Excluir Anexo" onclick="excluirAnexo(${ticket.ticketKey},'${anexo}');"/>
		</li>
	</c:forEach>
</ul>
</c:if>
