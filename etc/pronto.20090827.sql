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

    ALTER TABLE seq_retrospectiva OWNER TO pronto;


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
    
CREATE TABLE retrospectiva_item (
    retrospectiva_item_key integer NOT NULL,
    retrospectiva_key integer NOT NULL,
    tipo_retrospectiva_item_key integer NOT NULL,
    descricao character varying(255) NOT NULL 
);   

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
INSERT INTO tipo_retrospectiva_item VALUES (7, 'Chapéu Verde - Idéias', 2);
INSERT INTO tipo_retrospectiva_item VALUES (8, 'Chapéu Vermelho - Sentimentos', 2);    