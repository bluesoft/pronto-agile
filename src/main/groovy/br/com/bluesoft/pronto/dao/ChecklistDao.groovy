package br.com.bluesoft.pronto.dao

import org.springframework.stereotype.Repository

import br.com.bluesoft.pronto.model.Checklist
import br.com.bluesoft.pronto.model.ChecklistItem;


@Repository
class ChecklistDao extends DaoHibernate {

	ChecklistDao() {
		super(Checklist.class)
	}

	ChecklistItem obterItem(def key) {
		return getSession().get(ChecklistItem.class, key)
	}

	ChecklistItem salvarItem(ChecklistItem item) {
		getSession().saveOrUpdate(item)
		getSession().flush()
		return item
	}
	
	void excluirItem(checklistItemKey) {
		getSession().delete(obterItem(checklistItemKey))
		getSession().flush()
	}
	
	def listarModelos() {
		getSession().createQuery("from Checklist cl where cl.ticket is null order by cl.nome").list()
	}
}