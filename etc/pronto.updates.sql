-- 2010 05 26

CREATE TABLE causa_de_defeito (
	causa_de_defeito_key integer primary key,
	descricao varchar(75)
);

ALTER TABLE causa_de_defeito OWNER TO pronto;

alter table ticket add causa_de_defeito_key integer references causa_de_defeito;
CREATE INDEX idx_ticket_causa_de_defeito ON TICKET USING btree (causa_de_defeito_key);

create sequence SEQ_CAUSA_DE_DEFEITO;

	ALTER TABLE seq_causa_de_defeito OWNER TO pronto;

create sequence SEQ_MOTIVO_REPROVACAO;

	ALTER TABLE seq_motivo_reprovacao OWNER TO pronto;

CREATE TABLE MOTIVO_REPROVACAO (
	MOTIVO_REPROVACAO_key integer primary key,
	descricao varchar(75)
);

ALTER TABLE motivo_reprovacao OWNER TO pronto;

create sequence SEQ_MOVIMENTO_KANBAN;

	ALTER TABLE seq_movimento_kanban OWNER TO pronto;

CREATE TABLE movimento_kanban (
	movimento_kanban_key integer primary key,
	ticket_key integer references ticket not null,
	kanban_status_key integer references kanban_status not null,
	motivo_reprovacao_key integer references MOTIVO_REPROVACAO,
	data timestamp without time zone  not null,
	username character varying(100) references usuario not null 	
);

ALTER TABLE movimento_kanban OWNER TO pronto;

CREATE INDEX idx_movimento_kanban_ticket ON movimento_kanban USING btree (ticket_key);
CREATE INDEX idx_movimento_kanban_kanban_status ON movimento_kanban USING btree (kanban_status_key);
CREATE INDEX idx_movimento_kanban_motivo_reprovacao ON movimento_kanban USING btree (motivo_reprovacao_key);
CREATE INDEX idx_movimento_kanban_motivo_data ON movimento_kanban USING btree (data);
CREATE INDEX idx_movimento_kanban_motivo_usuario ON movimento_kanban USING btree (username);

-- 2010 08 11

create table integracao_zendesk (
	ticket_key integer references ticket not null,
	zendesk_ticket_key integer not null
);

alter table integracao_zendesk add primary key (ticket_key, zendesk_ticket_key);
CREATE INDEX idx_integracao_zendesk_pronto ON integracao_zendesk USING btree (ticket_key);
CREATE INDEX idx_integracao_zendesk_zendesk ON integracao_zendesk USING btree (zendesk_ticket_key);

-- 2010 08 17
alter table retrospectiva add descricao text;

-- 2010 09 02
alter table ticket add ticket_origem_key integer references ticket;

-- 2010 09 06
alter table sprint add meta  character varying(255);
alter table usuario add jabber_username  character varying(255);

--2010 09 09
alter table integracao_zendesk add constraint UK_INTEGRACAO_ZENDESK unique (ticket_key, zendesk_ticket_key);

--2010 09 10
ALTER TABLE integracao_zendesk OWNER TO pronto;

--2010 09 20
CREATE TABLE modulo (
	modulo_key integer primary key,
	descricao varchar(75)
);

ALTER TABLE modulo OWNER TO pronto;

alter table ticket add modulo_key integer references modulo;
CREATE INDEX idx_ticket_modulo ON TICKET USING btree (modulo_key);

create sequence SEQ_MODULO;

	ALTER TABLE SEQ_MODULO OWNER TO pronto;

--2011 02 23
alter table backlog add slug varchar(50);
update backlog set slug = 'ideias' where backlog_key = 1;
update backlog set slug = 'productBacklog' where backlog_key = 2;
update backlog set slug = 'lixeira' where backlog_key = 4;
update backlog set slug = 'impedimentos' where backlog_key = 5;

--2011 03 17
alter table script add total_de_execucoes integer;
alter table script add execucoes_pendentes integer;

update script s set total_de_execucoes = ( select count(*) from execucao where script_key = s.script_key);
update script s set execucoes_pendentes = ( select count(*) from execucao where script_key = s.script_key and data is null);

--2011 03 24
update backlog set descricao = 'Inbox', slug = 'inbox' where backlog_key = 1;

--2011 03 24
update ticket set tipo_de_ticket_key = 2 where tipo_de_ticket_key = 1;
delete from tipo_de_ticket where tipo_de_ticket_key = 1;