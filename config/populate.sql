INSERT INTO resume (uuid, full_name)
VALUES ('215ae369-7e86-4a18-bd75-b51651f68be3', 'Name1'),
       ('e28ca97d-f32e-4387-ac38-8185ae22ffc3', 'Name2'),
       ('f0f9998f-7581-499a-99bd-8305a6c8a4e2', 'Name3');

INSERT INTO contact (resume_uuid, "type", "value")
values ('55d174ca-aa3d-409e-8ed9-59b03da7282a', 'SKYPE', 'skype1'),
       ('55d174ca-aa3d-409e-8ed9-59b03da7282a', 'PHONE_NUMBER', '900');