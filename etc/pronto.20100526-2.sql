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
