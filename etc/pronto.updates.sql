-- 2010 05 26

CREATE TABLE causa_de_defeito (
	causa_de_defeito_key integer primary key,
	descricao varchar(75)
);

alter table ticket add causa_de_defeito_key integer references causa_de_defeito;
CREATE INDEX idx_ticket_causa_de_defeito ON TICKET USING btree (causa_de_defeito_key);

create sequence SEQ_CAUSA_DE_DEFEITO;
create sequence SEQ_MOTIVO_REPROVACAO;

CREATE TABLE MOTIVO_REPROVACAO (
	MOTIVO_REPROVACAO_key integer primary key,
	descricao varchar(75)
);

create sequence SEQ_MOVIMENTO_KANBAN;


CREATE TABLE movimento_kanban (
	movimento_kanban_key integer primary key,
	ticket_key integer references ticket not null,
	kanban_status_key integer references kanban_status not null,
	motivo_reprovacao_key integer references MOTIVO_REPROVACAO,
	data timestamp without time zone  not null,
	username character varying(100) references usuario not null 	
);


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

--2010 09 20
CREATE TABLE modulo (
	modulo_key integer primary key,
	descricao varchar(75)
);

alter table ticket add modulo_key integer references modulo;
CREATE INDEX idx_ticket_modulo ON TICKET USING btree (modulo_key);

create sequence SEQ_MODULO;

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

update ticket set tipo_de_ticket_key = 2 where tipo_de_ticket_key = 1;
delete from tipo_de_ticket where tipo_de_ticket_key = 1;

insert into backlog (backlog_key, descricao, slug) values (6, 'Futuro', 'futuro');

--2011 04 04
alter table usuario add telefone varchar(20);

--2011 04 08
alter table ticket add notas_para_release text;

--2011 04 13
CREATE TABLE projeto (
	projeto_key integer primary key,
	nome varchar(75)
);

ALTER TABLE projeto OWNER TO pronto;
alter table ticket add projeto_key integer references projeto;

CREATE INDEX idx_ticket_projeto ON TICKET USING btree (projeto_key);

create sequence SEQ_PROJETO;
ALTER TABLE SEQ_PROJETO OWNER TO pronto;


--2011 04 14
update ticket set projeto_key = null;
delete from projeto;
insert into projeto values (1, 'Projeto');
alter table kanban_status add inicio boolean default false NOT NULL;
alter table kanban_status add fim boolean default false NOT NULL;
alter table kanban_status add projeto_key integer references projeto;
update kanban_status set projeto_key = 1;
alter table kanban_status alter column projeto_key set not null;
CREATE INDEX idx_kanban_status_projeto ON kanban_status USING btree (projeto_key);
update ticket set projeto_key = 1;
alter table ticket alter column projeto_key set not null;

CREATE SEQUENCE seq_kanban_status
   INCREMENT 1
   START 110;
ALTER TABLE seq_kanban_status OWNER TO pronto;

alter table sprint add projeto_key integer references projeto;
CREATE INDEX idx_sprint_projeto ON sprint USING btree (projeto_key);
update sprint set projeto_key = 1;
update ticket set projeto_key = 1;
alter table sprint alter column projeto_key set not null;

alter table kanban_status drop column fixo;

alter table kanban_status drop constraint kanban_status_ordem_key;
update kanban_status set inicio = true where kanban_status_key = 1;
update kanban_status set fim = true where kanban_status_key = 100;

--2011 05 05
create sequence SEQ_CHECKLIST;
create sequence SEQ_CHECKLIST_ITEM;

create table checklist (
	checklist_key integer primary key,
	nome varchar(75),
	ticket_key integer references ticket not null
);

create table checklist_item (
	checklist_item_key integer primary key,
	checklist_key integer references checklist not null,
	descricao varchar(120) not null,
	marcado boolean default false NOT NULL
);

CREATE INDEX idx_checklist_ticket ON checklist USING btree (ticket_key);
CREATE INDEX idx_checklist_item_checklist ON checklist_item USING btree (checklist_key);

--2011 05 10
alter table checklist alter column ticket_key drop not null;

--2011 07 25
alter table ticket add responsavel_key varchar(100) references usuario;
CREATE INDEX idx_ticket_responsavel ON ticket USING btree (responsavel_key);