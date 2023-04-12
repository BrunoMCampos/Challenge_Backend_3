create table usuarios(
    id bigint not null primary key auto_increment,
    nome varchar(100) not null,
	email varchar(100) not null,
	senha varchar(500) not null,
	ativo boolean not null
);

create table autorizacao (
	id bigint not null,
	autorizacao varchar(50) not null,
	constraint fk_autorizacao_usuarios foreign key(id) references usuarios(id)
);
create unique index ix_auth_email on autorizacao (id,autorizacao);