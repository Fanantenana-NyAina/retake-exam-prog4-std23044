create table if not exists submission
(
    id             uuid
    constraint submission_pk primary key,
    email          varchar     not null,
    thumbnail_key  varchar,
    created_at     timestamp   not null
);