<%@ include file="/commons/taglibs.jsp"%>
<pronto:icons name="kanban.png" title="Ver Kanban" onclick="goTo('${raiz}kanban/${sprintKey}')"/>
<pronto:icons name="ver_estorias.gif" title="Ver Estórias" onclick="goTo('${raiz}backlogs/sprints/${sprint.sprintKey}')"/>
<pronto:icons name="burndown_chart.png" title="Burndown Chart do Sprint" onclick="goTo('${raiz}/burndown/${sprint.sprintKey}')"/>
<pronto:icons name="retrospectiva.png" title="Retrospectiva" onclick="goTo('${raiz}/retrospectivas/sprints/${sprint.sprintKey}')"/>