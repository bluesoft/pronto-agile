alter table ticket add data_da_ultima_alteracao timestamp without time zone;
update ticket set data_da_ultima_alteracao = coalesce(data_de_pronto, data_de_criacao);