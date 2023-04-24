alter table importacoes add column fk_email_usuario bigint;

update importacoes set fk_email_usuario = 'admin@email.com.br' where id > 0;

alter table importacoes modify fk_email_usuario varchar(100) not null;

alter table importacoes add constraint fk_email_usuario_importacoes foreign key (fk_email_usuario) references usuarios(email);