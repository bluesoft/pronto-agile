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