CREATE TABLE causa_de_defeito (
	causa_de_defeito_key integer primary key,
	descricao varchar(75)
);

alter table ticket add causa_de_defeito_key integer references causa_de_defeito;
CREATE INDEX idx_ticket_causa_de_defeito ON TICKET USING btree (causa_de_defeito_key);

create sequence SEQ_CAUSA_DE_DEFEITO;