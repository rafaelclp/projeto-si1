# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table disciplina (
  id                        bigint not null,
  nome                      varchar(255),
  creditos                  integer,
  dificuldade               integer,
  periodo_previsto          integer,
  constraint pk_disciplina primary key (id))
;

create table grade (
  id                        bigint not null,
  periodo_cursando          integer,
  constraint pk_grade primary key (id))
;

create table periodo (
  id                        bigint not null,
  constraint pk_periodo primary key (id))
;

create table usuario (
  id                        bigint not null,
  nome                      varchar(255),
  usuario                   varchar(255),
  senha_hasheada            varchar(255),
  salt                      varchar(255),
  grade_id                  bigint,
  constraint pk_usuario primary key (id))
;


create table disciplinas_preRequisitos (
  disciplina_id                  bigint not null,
  prerequisito_id                bigint not null,
  constraint pk_disciplinas_preRequisitos primary key (disciplina_id, prerequisito_id))
;

create table disciplinas_posRequisitos (
  disciplina_id                  bigint not null,
  posrequisito_id                bigint not null,
  constraint pk_disciplinas_posRequisitos primary key (disciplina_id, posrequisito_id))
;

create table grade_periodo (
  grade_id                       bigint not null,
  periodo_id                     bigint not null,
  constraint pk_grade_periodo primary key (grade_id, periodo_id))
;

create table periodo_disciplina (
  periodo_id                     bigint not null,
  disciplina_id                  bigint not null,
  constraint pk_periodo_disciplina primary key (periodo_id, disciplina_id))
;
create sequence disciplina_seq;

create sequence grade_seq;

create sequence periodo_seq;

create sequence usuario_seq;

alter table usuario add constraint fk_usuario_grade_1 foreign key (grade_id) references grade (id) on delete restrict on update restrict;
create index ix_usuario_grade_1 on usuario (grade_id);



alter table disciplinas_preRequisitos add constraint fk_disciplinas_preRequisitos__01 foreign key (disciplina_id) references disciplina (id) on delete restrict on update restrict;

alter table disciplinas_preRequisitos add constraint fk_disciplinas_preRequisitos__02 foreign key (prerequisito_id) references disciplina (id) on delete restrict on update restrict;

alter table disciplinas_posRequisitos add constraint fk_disciplinas_posRequisitos__01 foreign key (disciplina_id) references disciplina (id) on delete restrict on update restrict;

alter table disciplinas_posRequisitos add constraint fk_disciplinas_posRequisitos__02 foreign key (posrequisito_id) references disciplina (id) on delete restrict on update restrict;

alter table grade_periodo add constraint fk_grade_periodo_grade_01 foreign key (grade_id) references grade (id) on delete restrict on update restrict;

alter table grade_periodo add constraint fk_grade_periodo_periodo_02 foreign key (periodo_id) references periodo (id) on delete restrict on update restrict;

alter table periodo_disciplina add constraint fk_periodo_disciplina_periodo_01 foreign key (periodo_id) references periodo (id) on delete restrict on update restrict;

alter table periodo_disciplina add constraint fk_periodo_disciplina_discipl_02 foreign key (disciplina_id) references disciplina (id) on delete restrict on update restrict;

# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists disciplina;

drop table if exists disciplinas_preRequisitos;

drop table if exists disciplinas_posRequisitos;

drop table if exists grade;

drop table if exists grade_periodo;

drop table if exists periodo;

drop table if exists periodo_disciplina;

drop table if exists usuario;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists disciplina_seq;

drop sequence if exists grade_seq;

drop sequence if exists periodo_seq;

drop sequence if exists usuario_seq;

