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

import java.text.MessageFormat;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SequenceGenerator(name = "SEQ_EXECUCAO", sequenceName = "SEQ_EXECUCAO")
public class Execucao {

	@Id
	@GeneratedValue(generator = "SEQ_EXECUCAO")
	private int execucaoKey;

	@ManyToOne
	@JoinColumn(name = "BANCO_DE_DADOS_KEY")
	private BancoDeDados bancoDeDados;

	@ManyToOne
	@JoinColumn(name = "SCRIPT_KEY")
	private Script script;

	@ManyToOne
	@JoinColumn(name = "USERNAME")
	private Usuario usuario;

	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	public int getExecucaoKey() {
		return execucaoKey;
	}

	public void setExecucaoKey(final int execucaoKey) {
		this.execucaoKey = execucaoKey;
	}

	public BancoDeDados getBancoDeDados() {
		return bancoDeDados;
	}

	public void setBancoDeDados(final BancoDeDados bancoDeDados) {
		this.bancoDeDados = bancoDeDados;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(final Script script) {
		this.script = script;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(final Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getData() {
		return data;
	}

	public void setData(final Date data) {
		this.data = data;
	}

	public boolean isExecutado() {
		return this.getData() != null;
	}

	public boolean isPendente() {
		return this.getData() == null;
	}

	public String getStatus() {

		if (isExecutado()) {
			return MessageFormat.format("Executado em {0} por {1}", data, usuario.getUsername());
		} else {
			return "Não Executado";
		}

	}

}
