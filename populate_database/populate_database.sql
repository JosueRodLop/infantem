SET FOREIGN_KEY_CHECKS = 0;

-- Eliminación de datos existentes
TRUNCATE TABLE baby_allergen;
TRUNCATE TABLE baby_table;
TRUNCATE TABLE allergen;
TRUNCATE TABLE dream_table;
TRUNCATE TABLE recipe_table;
TRUNCATE TABLE user_table;
TRUNCATE TABLE disease_table;
TRUNCATE TABLE vaccine_table;
TRUNCATE TABLE baby_disease;
TRUNCATE TABLE vaccine_baby;
TRUNCATE TABLE food_nutrient;
TRUNCATE TABLE food_source;
TRUNCATE TABLE intake_table;
TRUNCATE TABLE intake_recipe;
TRUNCATE TABLE milestone;
TRUNCATE TABLE milestone_completed;
TRUNCATE TABLE nutrient_table;
TRUNCATE TABLE nutr_contr_table;
TRUNCATE TABLE nutr_contr_food_source_table;
TRUNCATE TABLE nutr_contr_nutrient_table;
TRUNCATE TABLE user_baby;
TRUNCATE TABLE authorities;
TRUNCATE TABLE user_authority;

-- Inserción de usuarios con contraseñas encriptadas
-- La contraseña que se ha codificado es: user
INSERT INTO user_table (id, surname, password, email, name, username) VALUES
(1, '1', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiUXA4zXYp8Pjz0ABT9FrS3LjE9c3G6', 'user1@gmail.com', 'user', 'user1'),
(2, '2', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiUXA4zXYp8Pjz0ABT9FrS3LjE9c3G6', 'user2@gmail.com', 'user', 'user2'),
(3, '3', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiUXA4zXYp8Pjz0ABT9FrS3LjE9c3G6', 'user3@gmail.com', 'user', 'user3'),
(4, '4', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiUXA4zXYp8Pjz0ABT9FrS3LjE9c3G6', 'user4@gmail.com', 'user', 'user4'),
(5, '5', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiUXA4zXYp8Pjz0ABT9FrS3LjE9c3G6', 'user5@gmail.com', 'user', 'user5'),
(6, '6', '$2a$10$7EqJtq98hPqEX7fNZaFWoOHiUXA4zXYp8Pjz0ABT9FrS3LjE9c3G6', 'user6@gmail.com', 'user', 'user6');

-- Inserción de roles
INSERT INTO authorities (id, authority) VALUES (1, 'user'), (2, 'admin');

-- Relación entre usuarios y roles
INSERT INTO user_authority (user_id, authority_id) VALUES
(1, 1), (2, 1), (3, 1), (4, 1), (5, 1), (6, 1);

-- Inserción de alérgenos
INSERT INTO allergen (id, name, description) VALUES
(1, 'Gluten', 'Presente en trigo, cebada, centeno y sus derivados.'),
(2, 'Lactosa', 'Azúcar presente en la leche y productos lácteos.'),
(3, 'Frutos secos', 'Incluye almendras, avellanas, nueces, anacardos, pistachos, etc.'),
(4, 'Huevo', 'Puede encontrarse en salsas, repostería y productos procesados.'),
(5, 'Mariscos', 'Incluye gambas, langostinos, cangrejos, y otros crustáceos.'),
(6, 'Pescado', 'Presente en alimentos procesados como caldos y salsas.'),
(7, 'Soja', 'Se encuentra en productos como salsa de soja, tofu y alimentos procesados.'),
(8, 'Mostaza', 'Ingrediente común en salsas y condimentos.'),
(9, 'Sésamo', 'Presente en panes, galletas, tahini y otros productos.'),
(10, 'Sulfitos', 'Utilizados como conservantes en vinos, frutas secas y productos procesados.');

-- Inserción de bebés
INSERT INTO baby_table (id, name, birth_date, genre, weight, height, cephalic_perimeter, food_preference) VALUES
(1, 'John', '2023-01-01', 'MALE', 3.5, 50, 35, 'Milk'),
(2, 'Jane', '2023-02-01', 'FEMALE', 3.2, 48, 34, 'Milk'),
(3, 'Alice', '2023-03-01', 'FEMALE', 3.8, 52, 36, 'Milk'),
(4, 'Bob', '2023-04-01', 'MALE', 3.6, 51, 35, 'Milk'),
(5, 'Charlie', '2023-05-01', 'MALE', 3.4, 49, 34, 'Milk');

-- Inserción de relaciones entre bebés y alérgenos
INSERT INTO baby_allergen (allergen_id, baby_id) VALUES
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Inserción de registros de sueño
INSERT INTO dream_table (baby_id, start, end, num_wakeups, dream_type) VALUES
(1, '2024-03-03 22:00:00', '2024-03-04 06:30:00', 2, 1),
(2, '2024-03-04 23:15:00', '2024-03-05 07:00:00', 3, 2);

-- Inserción de vacunas
INSERT INTO vaccine_table (id, type, vaccination_date) VALUES
(1, 'MMR', '2023-06-01'),
(2, 'DTaP', '2023-07-01'),
(3, 'HepB', '2023-08-01'),
(4, 'Polio', '2023-09-01'),
(5, 'Hib', '2023-10-01');

-- Relación entre vacunas y bebés
INSERT INTO vaccine_baby (vaccine_id, baby_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Relación entre usuarios y bebés
INSERT INTO user_baby (user_id, baby_id) VALUES
(1, 1), (1, 2), (2, 3), (3, 4), (4, 5);

-- Inserción de hitos
INSERT INTO milestone (id, name, description) VALUES
(1, 'Primer paso', 'El bebé da su primer paso sin apoyo.');

-- Inserción de hitos completados
INSERT INTO milestone_completed (id, baby_id, milestone_id, date) VALUES
(1, 1, 1, '2024-01-01');