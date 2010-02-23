alter table ticket add data_da_ultima_alteracao timestamp without time zone;
update ticket set data_da_ultima_alteracao = coalesce(data_de_pronto, data_de_criacao);
CREATE INDEX idx_execucao_data ON EXECUCAO USING btree (data);
CREATE INDEX idx_ticket_data_de_pronto ON TICKET USING btree (data_de_pronto);
CREATE INDEX idx_ticket_pai ON TICKET USING btree (pai);

CREATE INDEX idx_ticket_comentario_ticket ON TICKET_COMENTARIO USING btree (ticket_key);
CREATE INDEX idx_ticket_comentario_data ON TICKET_COMENTARIO USING btree (data);
CREATE INDEX idx_ticket_comentario_usuario ON TICKET_COMENTARIO USING btree (usuario_key);

CREATE INDEX idx_ticket_log_ticket ON TICKET_LOG USING btree (ticket_key);

CREATE INDEX idx_sprint_atual ON SPRINT USING btree (atual);
CREATE INDEX idx_sprint_fechado ON SPRINT USING btree (fechado);

CREATE INDEX idx_retrospectiva_item_retrospectiva ON RETROSPECTIVA_ITEM USING btree (RETROSPECTIVA_KEY);

alter table ticket_log alter column ticket_key set not null;

CREATE TABLE configuracoes (
	chave varchar(50) primary key,
	valor varchar(100)	
);

insert into configuracoes values ('tipoDeEstimativa', 'PMG');

alter table kanban_status add ordem integer;
update kanban_status set ordem = kanban_status_key;
alter table kanban_status add unique (ordem);

alter table kanban_status add fixo integer default 0 not null;
update kanban_status set fixo = 1 where kanban_status_key in (1, 2, 31, 100);


CREATE TABLE categoria (
	categoria_key integer primary key,
	descricao varchar(75),
	cor varchar(60)
);

alter table ticket add categoria_key integer references categoria;
CREATE INDEX idx_ticket_categoria ON TICKET USING btree (categoria_key);

create sequence SEQ_CATEGORIA;