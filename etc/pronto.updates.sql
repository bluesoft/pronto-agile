--2009 07 24
alter table usuario add email_md5 character varying(170);

--2009 08 04
ALTER TABLE ticket_comentario rename usuario to usuario_key;

UPDATE ticket_comentario set usuario_key = (select u.username from usuario u where u.nome = ticket_comentario.usuario_key);

ALTER TABLE ONLY ticket_comentario
    ADD CONSTRAINT fk_ticket_comentario_usuario FOREIGN KEY (usuario_key) REFERENCES usuario(username);
    
-- 2009 08 21
alter table ticket add prioridade integer;
INSERT INTO papel VALUES (6, 'Administrador');

-- 2009 08 27
CREATE SEQUENCE seq_retrospectiva
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE seq_retrospectiva OWNER TO pronto;

CREATE SEQUENCE seq_retrospectiva_item
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE seq_retrospectiva_item OWNER TO pronto;


CREATE TABLE tipo_retrospectiva_item (
    tipo_retrospectiva_item_key integer NOT NULL,
    descricao character varying(120)
);

INSERT INTO tipo_retrospectiva_item VALUES (1, 'O que foi bem');
INSERT INTO tipo_retrospectiva_item VALUES (2, 'O que pode ser melhorado');

ALTER TABLE public.tipo_retrospectiva_item OWNER TO pronto;

ALTER TABLE ONLY tipo_retrospectiva_item
    ADD CONSTRAINT tipo_retrospectiva_item_pkey PRIMARY KEY (tipo_retrospectiva_item_key);
    

CREATE TABLE retrospectiva (
    retrospectiva_key integer NOT NULL,
    sprint_key integer NOT NULL
    
);   

ALTER TABLE ONLY retrospectiva
    ADD CONSTRAINT retrospectiva_pkey PRIMARY KEY (retrospectiva_key);
    
ALTER TABLE ONLY retrospectiva
    ADD CONSTRAINT fk_retrospectiva_sprint FOREIGN KEY (sprint_key) REFERENCES sprint(sprint_key);

ALTER TABLE public.retrospectiva OWNER TO pronto;
    
CREATE TABLE retrospectiva_item (
    retrospectiva_item_key integer NOT NULL,
    retrospectiva_key integer NOT NULL,
    tipo_retrospectiva_item_key integer NOT NULL,
    descricao character varying(255) NOT NULL 
);

ALTER TABLE public.retrospectiva_item OWNER TO pronto;

ALTER TABLE ONLY retrospectiva_item
    ADD CONSTRAINT retrospectiva_item_pkey PRIMARY KEY (retrospectiva_item_key);    
    
ALTER TABLE ONLY retrospectiva_item
    ADD CONSTRAINT fk_retrospectiva_item_retrospectiva FOREIGN KEY (retrospectiva_key) REFERENCES retrospectiva(retrospectiva_key);    

ALTER TABLE ONLY tipo_retrospectiva_item
    ADD CONSTRAINT fk_retrospectiva_item_tipo_retrospectiva_item FOREIGN KEY (tipo_retrospectiva_item_key) REFERENCES tipo_retrospectiva_item(tipo_retrospectiva_item_key);
    
    
INSERT INTO tipo_retrospectiva_item VALUES (3, 'Chapéu Azul - Objetivos', 2);
INSERT INTO tipo_retrospectiva_item VALUES (4, 'Chapéu Branco - Fatos e Informações', 2);
INSERT INTO tipo_retrospectiva_item VALUES (5, 'Chapéu Amarelo - Acontecimentos Positivos', 2);
INSERT INTO tipo_retrospectiva_item VALUES (6, 'Chapéu Preto - Acontecimentos Negativos', 2);
INSERT INTO tipo_retrospectiva_item VALUES (7, 'Chapéu Verde - Ideias', 2);
INSERT INTO tipo_retrospectiva_item VALUES (8, 'Chapéu Vermelho - Sentimentos', 2);    

-- 2009 09 21

CREATE SEQUENCE seq_script
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE seq_script OWNER TO pronto;

CREATE SEQUENCE seq_banco_de_dados
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE seq_banco_de_dados OWNER TO pronto;


CREATE SEQUENCE seq_execucao
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE seq_execucao OWNER TO pronto;


CREATE TABLE banco_de_dados (
    banco_de_dados_key integer NOT NULL,
    nome character varying(120)
);

ALTER TABLE banco_de_dados OWNER TO pronto;

ALTER TABLE ONLY banco_de_dados
    ADD CONSTRAINT banco_de_dados_pkey PRIMARY KEY (banco_de_dados_key);
    
CREATE TABLE script (
    script_key integer NOT NULL,
    descricao character varying(120) not null,
    script text    
);   

ALTER TABLE ONLY script
    ADD CONSTRAINT script_pkey PRIMARY KEY (script_key);
    
ALTER TABLE script OWNER TO pronto;
    
CREATE TABLE execucao (
    execucao_key integer NOT NULL,
    script_key integer NOT NULL,
    banco_de_dados_key integer NOT NULL,
    username character varying(255),
    data  timestamp without time zone
);   

ALTER TABLE execucao OWNER TO pronto;

ALTER TABLE ONLY execucao
    ADD CONSTRAINT execucao_pkey PRIMARY KEY (execucao_key);    
    
ALTER TABLE ONLY execucao
    ADD CONSTRAINT fk_execucao_script FOREIGN KEY (script_key) REFERENCES script(script_key);    

ALTER TABLE ONLY execucao
    ADD CONSTRAINT fk_exeucao_banco_de_dados FOREIGN KEY (banco_de_dados_key) REFERENCES banco_de_dados(banco_de_dados_key);   
    
ALTER TABLE ONLY execucao
    ADD CONSTRAINT fk_execucao_usuario FOREIGN KEY (username) REFERENCES usuario(username);
    
-- 2009 09 28
CREATE SEQUENCE seq_cliente
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE seq_cliente OWNER TO pronto;

CREATE TABLE cliente (
    cliente_key integer NOT NULL,
    nome character varying(120)
);

ALTER TABLE cliente OWNER TO pronto;

ALTER TABLE ONLY cliente
    ADD CONSTRAINT cliente_pkey PRIMARY KEY (cliente_key);
    
ALTER TABLE USUARIO ADD CLIENTE_KEY INTEGER;
  
ALTER TABLE ONLY USUARIO
    ADD CONSTRAINT FK_USUARIO_CLIENTE FOREIGN KEY (CLIENTE_KEY) REFERENCES CLIENTE (CLIENTE_KEY);    
 
ALTER TABLE TICKET ADD CLIENTE_KEY INTEGER; 

ALTER TABLE TICKET ADD PRIORIDADE_DO_CLIENTE INTEGER;

ALTER TABLE ONLY TICKET
    ADD CONSTRAINT FK_TICKET_CLIENTE FOREIGN KEY (CLIENTE_KEY) REFERENCES CLIENTE (CLIENTE_KEY);    


--Apagamos os tipos desenvolvedor, suporte e testador e mantivemos apenas equipe para ficar mais leal ao Scrum.
insert into papel VALUES (9,'Equipe');
INSERT INTO papel VALUES (7, 'Cliente');

insert into usuario_papel select distinct usuario_key, 9 from usuario_papel where papel_key in (3,4,5,8);
delete from usuario_papel where papel_key in (3,4,5,8);
delete from papel where papel_key in (3,4,5,8);

--O cliente do ticket agora deve apontar para um cliente da tabela cliente.
--Por isso, antes de rodar o script abaixo cadastre os seus cliente na tabela cliente
--com os mesmos nomes que você digitava no campo cliente dos tickets.
--exemplo: update ticket set cliente = 'Bluesoft';
--exemplo: insert into cliente values( nextval('seq_cliente'), 'Bluesoft');

--o script abaixo vai buscar um cliente de mesmo nome e fazer a link entre as tabelas tarefa e cliente
--depois apagará o campo cliente do ticket mantendo somente o cliente_key.
update ticket set cliente_key = (select cliente_key from cliente c where c.nome = cliente);

-- 2009 10 01

CREATE INDEX idx_ticket_cliente ON ticket USING btree (cliente_key);
CREATE INDEX idx_ticket_branch ON ticket USING btree (branch);
CREATE INDEX idx_execucao_script ON execucao USING btree (script_key);
CREATE INDEX idx_execucao_banco_de_dados ON execucao USING btree (banco_de_dados_key);

-- 2009 10 28

alter table ticket alter column data_de_criacao set default now();

-- 2009 11 05

alter table ticket add script_key integer;

ALTER TABLE ONLY TICKET
    ADD CONSTRAINT FK_TICKET_SCRIPT FOREIGN KEY (SCRIPT_KEY) REFERENCES SCRIPT (SCRIPT_KEY);
    
CREATE INDEX idx_ticket_script ON TICKET USING btree (SCRIPT_KEY);

-- 2009 12 16

CREATE TABLE tipo_retrospectiva (
    tipo_retrospectiva_key integer NOT NULL,
    descricao character varying(120)
);

INSERT INTO tipo_retrospectiva VALUES (1, 'Tradicional');
INSERT INTO tipo_retrospectiva VALUES (2, '6 Chapéus');

ALTER TABLE public.tipo_retrospectiva OWNER TO pronto;

ALTER TABLE ONLY tipo_retrospectiva
    ADD CONSTRAINT tipo_retrospectiva_pkey PRIMARY KEY (tipo_retrospectiva_key);
    
ALTER TABLE retrospectiva add tipo_retrospectiva_key integer;

update retrospectiva set tipo_retrospectiva_key = 1;

ALTER TABLE retrospectiva alter tipo_retrospectiva_key set not null;
    
ALTER TABLE ONLY retrospectiva
    ADD CONSTRAINT fk_retrospectiva_tipo_retrospectiva FOREIGN KEY (tipo_retrospectiva_key) REFERENCES tipo_retrospectiva;
    
    
alter table tipo_retrospectiva_item add tipo_retrospectiva_key integer;
update tipo_retrospectiva_item set tipo_retrospectiva_key = 1;

ALTER TABLE ONLY tipo_retrospectiva_item
    ADD CONSTRAINT fk_tipo_retrospectiva_item_tipo_retrospectiva FOREIGN KEY (tipo_retrospectiva_key) REFERENCES tipo_retrospectiva;
    
-- 2010 02 09

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

ALTER TABLE configuracoes OWNER TO pronto;

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

ALTER TABLE categoria OWNER TO pronto;

alter table ticket add categoria_key integer references categoria;
CREATE INDEX idx_ticket_categoria ON TICKET USING btree (categoria_key);

create sequence SEQ_CATEGORIA;
	
alter table seq_categoria OWNER to pronto;

-- Até aqui já está no script principal
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
