create table hsc(
    hsc_reg_no varchar(8) not null primary key,
    board varchar(16) not null,
    percentage double not null
);

create table entrance(
    entrance_reg_no varchar(8) not null primary key,
    percentile double not null,
    score double not null
);

create table application_form(
    unique_id varchar(16) not null primary key ,
    id_type enum('UIDAI','PASSPORT','PAN','DRIVING_LICENSE','VOTER_ID') default 'UIDAI',
    first_name varchar(16) not null,
    middle_name varchar(16),
    last_name varchar(16) not null,
    email varchar(64) not null,
    phone varchar(12) not null,
    caste enum ('GEN','OBC','SC/ST','NT') default 'GEN',
    entrance_reg_no varchar(8) not null unique ,
    hsc_reg_no varchar(8) not null unique ,
    branch_name varchar(8) not null
);

create table applicant(
    applicant_id varchar(16) not null primary key,
    enrollment_id varchar(16) unique,
    password varchar(16) not null,
    unique_id varchar(16) not null unique ,
    status enum ('PENDING','APPLIED','SHORTLISTED','LOCKED','REJECTED','FLOATED','UNDER_VERIFICATION','ENROLLED','NOT_FOUND','FAILED') not null default 'PENDING'


);

create table enrollment_form(
    applicant_id varchar(16) not null primary key ,
    placeholder varchar(16) not null

);

create table admin(
    username varchar(16) primary key,
    name varchar(32),
    password varchar(16) not null
);

alter table application_form add constraint fk_hsc_reg_no
foreign key(hsc_reg_no) references hsc(hsc_reg_no)
on delete cascade;

alter table application_form add constraint fk_entrance_reg_no
foreign key(entrance_reg_no) references entrance(entrance_reg_no)
on delete cascade;

alter table applicant add constraint fk_unique_id
foreign key(unique_id) references application_form(unique_id)
on delete cascade;

alter table enrollment_form add constraint fk_applicant_id
foreign key(applicant_id) references applicant(applicant_id)
on delete cascade;

