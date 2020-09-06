CREATE TABLE resume
(
    uuid char(36),
    full_name text,
    CONSTRAINT resume_pkey PRIMARY KEY (uuid)
);

CREATE TABLE contact
(
    id serial NOT NULL,
    resume_uuid char(36) NOT NULL,
    type text,
    value text
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact
    (resume_uuid, type);

