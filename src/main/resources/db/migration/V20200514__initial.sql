create schema if not exists shmv;

create table shmv.operation_record
(
	account_id                      character varying not null,
	batch_id                        bigint not null,
	plan_id                         character varying not null,

	batch_hash                      bigint not null,
	kafka_offset                    bigint not null,
	operation_type                  integer not null,
	primary key (account_id, batch_id, plan_id)
);

create table shmv.failure_record
(
	account_id                      character varying not null,
	batch_id                        bigint not null,
	plan_id                         character varying not null,

	reason                          character varying not null,
	primary key (account_id, batch_id, plan_id)
);
