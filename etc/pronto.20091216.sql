CREATE TABLE tipo_retrospectiva (
    tipo_retrospectiva_key integer NOT NULL,
    descricao character varying(120)
);

INSERT INTO tipo_retrospectiva VALUES (1, 'Tradicional');
INSERT INTO tipo_retrospectiva VALUES (2, '6 Chapéus');

ALTER TABLE public.tipo_retrospectiva OWNER TO pronto;

ALTER TABLE ONLY retrospectiva
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