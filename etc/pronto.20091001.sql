CREATE INDEX idx_ticket_cliente ON ticket USING btree (cliente_key);
CREATE INDEX idx_ticket_branch ON ticket USING btree (branch);
CREATE INDEX idx_execucao_script ON execucao USING btree (script_key);
CREATE INDEX idx_execucao_banco_de_dados ON execucao USING btree (banco_de_dados_key);