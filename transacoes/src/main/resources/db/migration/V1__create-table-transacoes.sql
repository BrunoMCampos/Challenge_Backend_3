create table transacoes (
    id bigint not null auto_increment,
    banco_origem varchar(100) not null,
    agencia_origem varchar(4) not null,
    conta_origem varchar(7) not null,
    banco_destino varchar(100) not null,
    agencia_destino varchar(4) not null,
    conta_destino varchar(7) not null,
    valor_transacao decimal(15,2) not null,
    data_hora_transacao datetime not null,

    primary key(id)
);