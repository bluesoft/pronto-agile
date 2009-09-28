ALTER TABLE ticket_comentario rename usuario to usuario_key;

UPDATE ticket_comentario set usuario_key = (select u.username from usuario u where u.nome = ticket_comentario.usuario_key);

ALTER TABLE ONLY ticket_comentario
    ADD CONSTRAINT fk_ticket_comentario_usuario FOREIGN KEY (usuario_key) REFERENCES usuario(username);