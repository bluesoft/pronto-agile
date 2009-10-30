/* Cria usuários para a versão de demonstração */
delete from usuario_papel where usuario_key in ('justus', 'coach', 'time', 'cliente');
delete from usuario where username in ('justus', 'coach', 'time', 'cliente');
delete from cliente where cliente_key = 1;

/* Product Owner */
INSERT INTO usuario VALUES ('justus', 'O31RL8cW9ISDGdCghWotXw==', 'Justus P.O.', 'roberto@justus.com.br');
INSERT INTO usuario_papel VALUES('justus',1);

/* Scrum Master */
INSERT INTO usuario VALUES ('coach', '+TGxOurQAtf82wL4Tg95Tw==', 'Coach S.M.', 'coach@coach.com.br');
INSERT INTO usuario_papel VALUES('coach',2);

/* Equipe */
INSERT INTO usuario VALUES ('time', 'B8xpS5s/xjZxD6CLaSLEKw==', 'Membro do Time', 'time@time.com.br');
INSERT INTO usuario_papel VALUES('time',9);

/* Cliente */
INSERT INTO cliente values(1, 'Cliente Teste');
INSERT INTO usuario VALUES ('cliente', 'SYOgq4PthuDnITyHg5QBkw==', 'Usuário Cliente', 'cliente@cliente.com.br', null, 1);
INSERT INTO usuario_papel VALUES('cliente',7);