create table usuarios(
    email varchar(100) not null primary key,
    nome varchar(100) not null,
	senha varchar(500) not null,
	ativo boolean not null
);

create table autorizacao (
	email varchar(100) not null,
	autorizacao varchar(50) not null,
	constraint fk_autorizacao_usuarios foreign key(email) references usuarios(email)
);

create unique index autorizacao_email on autorizacao (email,autorizacao);