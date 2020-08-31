CREATE TABLE public.resume
(
    uuid character(36),
    full_name text,
    CONSTRAINT resume_pkey PRIMARY KEY (uuid)
);

CREATE TABLE public.contact
(
    id serial NOT NULL,
    resume_uuid character(36) NOT NULL,
    type text,
    value text,
    CONSTRAINT contact_pkey PRIMARY KEY (id),
    CONSTRAINT contact_resume_uuid_fk FOREIGN KEY (resume_uuid)
        REFERENCES public.resume (uuid) MATCH SIMPLE
        ON UPDATE RESTRICT
        ON DELETE CASCADE
);

CREATE UNIQUE INDEX contact_uuid_type_index
    ON public.contact USING btree
    (resume_uuid ASC NULLS LAST, type ASC NULLS LAST);

