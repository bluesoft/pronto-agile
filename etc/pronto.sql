/* Cria a Estrutura do Banco de Dados do Pronto com os registros default */

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;
COMMENT ON SCHEMA public IS 'Standard public schema';
SET search_path = public, pg_catalog;
SET default_tablespace = '';
SET default_with_oids = false;

CREATE TABLE backlog (
    backlog_key integer NOT NULL,
    descricao character varying(255)
);

ALTER TABLE public.backlog OWNER TO pronto;

CREATE SEQUENCE hibernate_sequence
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE public.hibernate_sequence OWNER TO pronto;

SELECT pg_catalog.setval('hibernate_sequence', 62, true);

CREATE SEQUENCE seq_ticket
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE public.seq_ticket OWNER TO pronto;


CREATE SEQUENCE seq_sprint
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE public.seq_sprint OWNER TO pronto;

    
CREATE SEQUENCE seq_ticket_comentario
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE public.seq_ticket_comentario OWNER TO pronto;

CREATE SEQUENCE seq_ticket_log
    INCREMENT BY 1
    NO MAXVALUE
    NO MINVALUE
    CACHE 1;

    ALTER TABLE public.seq_ticket_log OWNER TO pronto;
    
CREATE TABLE kanban_status (
    kanban_status_key integer NOT NULL,
    descricao character varying(255)
);

ALTER TABLE public.kanban_status OWNER TO pronto;

CREATE TABLE papel (
    papel_key integer NOT NULL,
    descricao character varying(255)
);

ALTER TABLE public.papel OWNER TO pronto;

CREATE TABLE sprint (
    sprint_key integer NOT NULL,
    nome character varying(255),
    data_inicial timestamp without time zone,
    data_final timestamp without time zone,    
    fechado boolean NOT NULL,
    atual boolean NOT NULL
);

ALTER TABLE public.sprint OWNER TO pronto;

CREATE TABLE ticket (
    ticket_key integer NOT NULL,
    titulo character varying(255),
    descricao text,
    cliente character varying(255),
    solicitador character varying(255),
    branch character varying(255),
    valor_de_negocio integer NOT NULL,
    esforco double precision NOT NULL,
    par boolean NOT NULL,
    planejado boolean NOT NULL,
    data_de_criacao timestamp without time zone,
    data_de_pronto timestamp without time zone,
    reporter_key character varying(255),
    pai integer,
    kanban_status_key integer,
    sprint integer,
    tipo_de_ticket_key integer,
    backlog_key integer
);

ALTER TABLE public.ticket OWNER TO pronto;

CREATE TABLE ticket_comentario (
    ticket_comentario_key integer NOT NULL,
    usuario character varying(255),
    data timestamp without time zone,
    texto text,
    ticket_key integer
);

ALTER TABLE public.ticket_comentario OWNER TO pronto;

CREATE TABLE ticket_desenvolvedor (
    ticket_key integer NOT NULL,
    usuario_key character varying(255) NOT NULL
);

ALTER TABLE public.ticket_desenvolvedor OWNER TO pronto;


CREATE TABLE ticket_testador (
    ticket_key integer NOT NULL,
    usuario_key character varying(255) NOT NULL
);

ALTER TABLE public.ticket_testador OWNER TO pronto;


CREATE TABLE ticket_log (
    ticket_history_key integer NOT NULL,
    campo character varying(255),
    operacao integer NOT NULL,
    valor_antigo text,
    valor_novo text,
    data timestamp without time zone,
    usuario character varying(255),
    ticket_key integer
);


ALTER TABLE public.ticket_log OWNER TO pronto;

CREATE TABLE tipo_de_ticket (
    tipo_de_ticket_key integer NOT NULL,
    descricao character varying(255)
);

ALTER TABLE public.tipo_de_ticket OWNER TO pronto;

CREATE TABLE usuario (
    username character varying(100) NOT NULL,
    "password" character varying(30),
    nome character varying(150),
    email character varying(170)
);

ALTER TABLE public.usuario OWNER TO pronto;

CREATE TABLE usuario_papel (
    usuario_key character varying(255) NOT NULL,
    papel_key integer NOT NULL
);


ALTER TABLE public.usuario_papel OWNER TO pronto;

INSERT INTO backlog VALUES (1, 'Idéias');
INSERT INTO backlog VALUES (5, 'Impedimentos');
INSERT INTO backlog VALUES (4, 'Lixeira');
INSERT INTO backlog VALUES (2, 'Product Backlog');
INSERT INTO backlog VALUES (3, 'Sprint Backlog');

INSERT INTO kanban_status VALUES (1, 'To Do');
INSERT INTO kanban_status VALUES (2, 'Doing');
INSERT INTO kanban_status VALUES (21, 'Testing');
INSERT INTO kanban_status VALUES (100, 'Done');

INSERT INTO papel VALUES (1, 'Product Owner');
INSERT INTO papel VALUES (4, 'Testador');
INSERT INTO papel VALUES (2, 'Scrum Master');
INSERT INTO papel VALUES (5, 'Suporte');
INSERT INTO papel VALUES (3, 'Desenvolvedor');

INSERT INTO tipo_de_ticket VALUES (1, 'Idéia');
INSERT INTO tipo_de_ticket VALUES (2, 'Estória');
INSERT INTO tipo_de_ticket VALUES (3, 'Defeito');
INSERT INTO tipo_de_ticket VALUES (5, 'Impedimento');
INSERT INTO tipo_de_ticket VALUES (6, 'Tarefa');

INSERT INTO usuario VALUES ('admin', 'ISMvKXpXpadDiUoOSoAfww==', 'Administrador do Pronto', 'adm@adm.com.br');

INSERT INTO usuario_papel VALUES('admin',1);
INSERT INTO usuario_papel VALUES('admin',2);
INSERT INTO usuario_papel VALUES('admin',3);
INSERT INTO usuario_papel VALUES('admin',4);
INSERT INTO usuario_papel VALUES('admin',5);

ALTER TABLE ONLY backlog
    ADD CONSTRAINT backlog_pkey PRIMARY KEY (backlog_key);

ALTER TABLE ONLY kanban_status
    ADD CONSTRAINT kanban_status_pkey PRIMARY KEY (kanban_status_key);

ALTER TABLE ONLY papel
    ADD CONSTRAINT papel_pkey PRIMARY KEY (papel_key);

ALTER TABLE ONLY sprint
    ADD CONSTRAINT sprint_pkey PRIMARY KEY (sprint_key);

ALTER TABLE ONLY ticket_comentario
    ADD CONSTRAINT ticket_comentario_pkey PRIMARY KEY (ticket_comentario_key);

ALTER TABLE ONLY ticket_desenvolvedor
    ADD CONSTRAINT ticket_desenvolvedor_pkey PRIMARY KEY (ticket_key, usuario_key);
    
ALTER TABLE ONLY ticket_testador
    ADD CONSTRAINT ticket_testador_pkey PRIMARY KEY (ticket_key, usuario_key);

ALTER TABLE ONLY ticket_log
    ADD CONSTRAINT ticket_log_pkey PRIMARY KEY (ticket_history_key);

ALTER TABLE ONLY ticket
    ADD CONSTRAINT ticket_pkey PRIMARY KEY (ticket_key);

ALTER TABLE ONLY tipo_de_ticket
    ADD CONSTRAINT tipo_de_ticket_pkey PRIMARY KEY (tipo_de_ticket_key);

ALTER TABLE ONLY usuario_papel
    ADD CONSTRAINT usuario_papel_pkey PRIMARY KEY (usuario_key, papel_key);

ALTER TABLE ONLY usuario
    ADD CONSTRAINT usuario_pkey PRIMARY KEY (username);

ALTER TABLE ONLY usuario_papel
    ADD CONSTRAINT fk4d25cd3566f20c10 FOREIGN KEY (papel_key) REFERENCES papel(papel_key);

ALTER TABLE ONLY usuario_papel
    ADD CONSTRAINT fk4d25cd358e8e74a4 FOREIGN KEY (usuario_key) REFERENCES usuario(username);

ALTER TABLE ONLY ticket_desenvolvedor
    ADD CONSTRAINT fk50e2688d6484a110 FOREIGN KEY (ticket_key) REFERENCES ticket(ticket_key);

ALTER TABLE ONLY ticket_desenvolvedor
    ADD CONSTRAINT fk50e2688d8e8e74a4 FOREIGN KEY (usuario_key) REFERENCES usuario(username);

ALTER TABLE ONLY ticket_testador
    ADD CONSTRAINT fk50e2688d6484a110 FOREIGN KEY (ticket_key) REFERENCES ticket(ticket_key);

ALTER TABLE ONLY ticket_testador
    ADD CONSTRAINT fk50e2688d8e8e74a4 FOREIGN KEY (usuario_key) REFERENCES usuario(username);
    
ALTER TABLE ONLY ticket_log
    ADD CONSTRAINT fk7c90d7916484a110 FOREIGN KEY (ticket_key) REFERENCES ticket(ticket_key);

ALTER TABLE ONLY ticket_comentario
    ADD CONSTRAINT fkae76b2f46484a110 FOREIGN KEY (ticket_key) REFERENCES ticket(ticket_key);

ALTER TABLE ONLY ticket
    ADD CONSTRAINT fkcbe86b0c6bb18e7e FOREIGN KEY (backlog_key) REFERENCES backlog(backlog_key);

ALTER TABLE ONLY ticket
    ADD CONSTRAINT fkcbe86b0c9fff54d7 FOREIGN KEY (reporter_key) REFERENCES usuario(username);

ALTER TABLE ONLY ticket
    ADD CONSTRAINT fkcbe86b0cb145678c FOREIGN KEY (sprint) REFERENCES sprint(sprint_key);

ALTER TABLE ONLY ticket
    ADD CONSTRAINT fkcbe86b0cc41181bf FOREIGN KEY (kanban_status_key) REFERENCES kanban_status(kanban_status_key);

ALTER TABLE ONLY ticket
    ADD CONSTRAINT fkcbe86b0cd1a566bc FOREIGN KEY (tipo_de_ticket_key) REFERENCES tipo_de_ticket(tipo_de_ticket_key);

ALTER TABLE ONLY ticket
    ADD CONSTRAINT fkcbe86b0ce7f57efc FOREIGN KEY (pai) REFERENCES ticket(ticket_key);
    
CREATE INDEX idx_ticket_sprint ON ticket USING btree (sprint);    
CREATE INDEX idx_ticket_tipo_de_ticket ON ticket USING btree (tipo_de_ticket_key);
CREATE INDEX idx_ticket_kaban_status ON ticket USING btree (kanban_status_key);
CREATE INDEX idx_ticket_backlog ON ticket USING btree (backlog_key);
CREATE INDEX idx_ticket_titulo ON ticket USING btree (titulo);

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;