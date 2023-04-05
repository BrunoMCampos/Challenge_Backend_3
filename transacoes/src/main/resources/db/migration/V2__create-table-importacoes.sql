create table importacoes (
    id bigint not null auto_increment,
    data_transacoes datetime not null,
    data_upload datetime not null,

    primary key(id)
);