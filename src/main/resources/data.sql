-- Inserción de roles
INSERT INTO authorities (id, authority) VALUES (1, 'user'), (2, 'admin');

-- Inserción de usuarios con contraseñas encriptadas
-- La contraseña que se ha codificado es: user

INSERT INTO user_table (name, surname, username, password, email, profile_photo_route, authority_id) 
VALUES ('user', 'user', 'user1', '$2a$10$quXOAFytwj43GJuecSaM7.nrieG6RG4GVUZASDEefCcEfzk.lPMo6', 'user1@example.com', 'a', 1),
       ('user', 'user', 'user2', '$2a$10$quXOAFytwj43GJuecSaM7.nrieG6RG4GVUZASDEefCcEfzk.lPMo6', 'user2@example.com', 'a', 1),
       ('admin', 'admin', 'admin1', '$2a$10$quXOAFytwj43GJuecSaM7.nrieG6RG4GVUZASDEefCcEfzk.lPMo6', 'admin1@example.com', 'a', 2);

-- Inserción de alérgenos
INSERT INTO allergen (name, description) VALUES
('Gluten', 'Presente en trigo, cebada, centeno y sus derivados.'),
('Lactosa', 'Azúcar presente en la leche y productos lácteos.'),
('Frutos secos', 'Incluye almendras, avellanas, nueces, anacardos, pistachos, etc.'),
('Huevo', 'Puede encontrarse en salsas, repostería y productos procesados.'),
('Mariscos', 'Incluye gambas, langostinos, cangrejos, y otros crustáceos.'),
('Pescado', 'Presente en alimentos procesados como caldos y salsas.'),
('Soja', 'Se encuentra en productos como salsa de soja, tofu y alimentos procesados.'),
('Mostaza', 'Ingrediente común en salsas y condimentos.'),
('Sésamo', 'Presente en panes, galletas, tahini y otros productos.'),
('Sulfitos', 'Utilizados como conservantes en vinos, frutas secas y productos procesados.');

-- Inserción de bebés
INSERT INTO baby_table (name, birth_date, genre, weight, height, cephalic_perimeter, food_preference) VALUES
('Juan', '2023-01-01', 'MALE', 3.5, 49, 35, 'Leche'),
('María', '2023-02-01', 'FEMALE', 3.2, 48, 34, 'Leche'),
('Alicia', '2023-03-01', 'FEMALE', 3.8, 36, 36, 'Leche'),
('Bruno', '2023-04-01', 'MALE', 3.6, 41, 35, 'Leche'),
('Carlos', '2023-05-01', 'MALE', 3.4, 49, 34, 'Leche');

-- Inserción de relaciones entre bebés y alérgenos
INSERT INTO baby_allergen (allergen_id, baby_id) VALUES
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Inserción de registros de sueño
INSERT INTO dream_table (baby_id, date_start, date_end, num_wakeups, dream_type) VALUES
(1, '2024-03-03 22:00:00', '2024-03-04 06:30:00', 2, 1),
(2, '2024-03-04 23:15:00', '2024-03-05 07:00:00', 3, 2);

-- Inserción de vacunas
INSERT INTO vaccine_table (type, vaccination_date) VALUES
('MMR', '2023-06-01'),
('DTaP', '2023-07-01'),
('HepB', '2023-08-01'),
('Polio', '2023-09-01'),
('Hib', '2023-10-01');

-- Relación entre vacunas y bebés
INSERT INTO vaccine_baby (vaccine_id, baby_id) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(5, 5);

-- Relación entre usuarios y bebés
INSERT INTO user_baby (user_id, baby_id) VALUES
(1, 1), (2, 2);

-- Inserción de hitos
INSERT INTO milestone (name, description) VALUES
('Primer paso', 'El bebé da su primer paso sin apoyo.');

-- Inserción de hitos completados
INSERT INTO milestone_completed (baby_id, milestone_id, date) VALUES
(1, 1, '2024-01-01');

-- Inerción de recetas
INSERT INTO recipe_table(max_recommended_age, min_recommended_age, user_id, description, elaboration, ingredients, name, photo_route) VALUES
(10, 3, 1, 'Descripción1', 'Elaboración1', 'Ingredientes1', 'Receta1', 'a'),
(1, 1, 2, 'Descripción2', 'Elaboración2', 'Ingredientes2', 'Receta2', 'a'),
(5, 1, 2, 'Descripción3', 'Elaboración3', 'Ingredientes3', 'Receta3', 'a'),
(10, 3, 1, 'Descripción4', 'Elaboración4', 'Ingredientes4', 'Receta4', 'a'),
(4, 1, 1, 'Descripción5', 'Elaboración5', 'Ingredientes5', 'Receta5', 'a'),
(10, 1, null, 'Descripción6', 'Elaboración6', 'Ingredientes6', 'Receta6', 'a'),
(10, 1, null, 'Descripción7', 'Elaboración7', 'Ingredientes7', 'Receta7', 'a'),
(16, 7, null, 'Descripción8', 'Elaboración8', 'Ingredientes8', 'Receta8', 'a'),
(15, 6, null, 'Descripción9', 'Elaboración9', 'Ingredientes9', 'Receta9', 'a');

INSERT INTO nutrient_table(id,type,name,unit) VALUES
(1,'MACRONUTRIENTE','nutriente1','g'),
(2,'MICRONUTRIENTE','nutriente2','mg'),
(3,'MACRONUTRIENTE','nutriente3','mg'),
(4,'MICRONUTRIENTE','nutriente4','g'),
(5,'MACRONUTRIENTE','nutriente5','g'),
(6,'MICRONUTRIENTE','nutriente6','mg'),
(7,'MACRONUTRIENTE','nutriente7','mg'),
(8,'MICRONUTRIENTE','nutriente8','g');


INSERT INTO food_nutrient_table(id, amount, nutrient_id, recipe_id) VALUES
(1, 10, 1, 1),
(2, 5, 2, 1),
(3, 8, 3, 2),
(4, 12, 4, 2),
(5, 7, 5, 3),
(6, 9, 6, 3),
(7, 11, 7, 4),
(8, 6, 8, 4),
(9, 15, 1, 5),
(10, 4, 2, 5),
(11, 13, 3, 6),
(12, 3, 4, 6),
(13, 14, 5, 7),
(14, 2, 6, 7),
(15, 16, 7, 8),
(16, 1, 8, 8),
(17, 18, 1, 9),
(18, 20, 2, 9);


INSERT INTO recipe_allergen(allergen_id,recipe_id) VALUES
(1,1),
(2,2),
(3,3),
(4,4),
(5,5);