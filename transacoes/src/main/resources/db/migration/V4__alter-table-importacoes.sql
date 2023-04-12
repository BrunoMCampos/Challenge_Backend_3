alter table importacoes add column fk_id_usuario bigint;

update importacoes set fk_id_usuario = 1 where id > 0;

alter table importacoes modify fk_id_usuario bigint not null;

alter table importacoes add constraint fk_id_usuario_importacoes foreign key (fk_id_usuario) references usuarios(id);