INSERT INTO users (id, email, password, role, first_name, last_name)
VALUES (3, 'test@test.com', crypt('test', gen_salt('bf')), 'USER', 'Test', 'Testov');
SELECT SETVAL('users_id_seq', (SELECT MAX(id) FROM users));

INSERT INTO countries (id, name)
VALUES (1, 'France'),
       (2, 'Belarus'),
       (3, 'Georgia'),
       (4, 'USA');
SELECT SETVAL('countries_id_seq', (SELECT MAX(id) FROM countries));

INSERT INTO cities (id, name, logo_id, country_entity_id)
VALUES (1, 'Brest', gen_random_uuid(), 1),
       (2, 'Brest', gen_random_uuid(), 2),
       (3, 'Minsk', gen_random_uuid(), 2),
       (5, 'Batumi', gen_random_uuid(), 3),
       (6, 'Miami', gen_random_uuid(), 4);
SELECT SETVAL('cities_id_seq', (SELECT MAX(id) FROM cities));