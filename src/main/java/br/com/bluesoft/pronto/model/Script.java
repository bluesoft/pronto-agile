/*
 * Copyright 2009 Pronto Agile Project Management.
 *
 * This file is part of Pronto.
 *
 * Pronto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Pronto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Pronto. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package br.com.bluesoft.pronto.model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import com.google.common.collect.Sets;

@Entity
@SequenceGenerator(name = "SEQ_SCRIPT", sequenceName = "SEQ_SCRIPT")
public class Script {

	@Id
	@GeneratedValue(generator = "SEQ_SCRIPT")
	private int scriptKey;

	private String descricao;

	private String script;

	@OneToMany(mappedBy = "script", cascade = CascadeType.ALL)
	private Set<Execucao> execucoes;

	public int getScriptKey() {
		return scriptKey;
	}

	public void setScriptKey(final int scriptKey) {
		this.scriptKey = scriptKey;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(final String descricao) {
		this.descricao = descricao;
	}

	public String getScript() {
		return script;
	}

	public void setScript(final String script) {
		this.script = script;
	}

	public Set<Execucao> getExecucoes() {
		return execucoes;
	}

	public Set<BancoDeDados> getBancosDeDados() {
		final Set<BancoDeDados> bancos = Sets.newHashSet();
		if (execucoes != null) {
			for (final Execucao execucao : execucoes) {
				bancos.add(execucao.getBancoDeDados());
			}
		}
		return bancos;
	}

	public boolean temExecucaoParaOBancoDeDados(final int bancoDeDadosKey) {
		if (execucoes != null) {
			for (final Execucao execucao : execucoes) {
				if (execucao.getBancoDeDados().getBancoDeDadosKey() == bancoDeDadosKey) {
					return true;
				}
			}
		}
		return false;
	}

	public Execucao adicionarExecucaoParaOBanco(final BancoDeDados bancoDeDados) {
		if (this.execucoes == null) {
			this.execucoes = Sets.newHashSet();
		}

		final Execucao execucao = new Execucao();
		execucao.setBancoDeDados(bancoDeDados);
		execucao.setScript(this);
		this.execucoes.add(execucao);
		return execucao;
	}

	public Execucao getExecucaoByBancoDeDados(final int bancoDeDadosKey) {
		if (this.execucoes != null) {
			for (final Execucao execucao : execucoes) {
				if (execucao.getBancoDeDados().getBancoDeDadosKey() == bancoDeDadosKey) {
					return execucao;
				}
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return this.scriptKey + " - " + this.getDescricao();
	}

	public void removerExecucao(final Execucao execucao) {
		if (execucoes != null) {
			execucoes.remove(execucao);
		}

	}

}
