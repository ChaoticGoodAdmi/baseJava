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
    value text,
    CONSTRAINT contact_pkey PRIMARY KEY (id),
    CONSTRAINT contact_resume_uuid_fk FOREIGN KEY (resume_uuid)
        REFERENCES resume (uuid)
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON contact
    (resume_uuid, type);

