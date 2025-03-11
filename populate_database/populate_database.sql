SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE baby_allergen;
TRUNCATE TABLE baby_table;
TRUNCATE TABLE allergen;
TRUNCATE TABLE dream_table;
TRUNCATE TABLE recipe_table;
TRUNCATE TABLE user_table;
TRUNCATE TABLE disease_table;
TRUNCATE TABLE vaccine_table;

INSERT INTO user_table (surname, password , email, name, name_user) VALUES
('1', 'pwd', 'user1@gmail.com', 'user', 'user1'),
('2', 'pwd', 'user2@gmail.com', 'user', 'user2'),
('3', 'pwd', 'user3@gmail.com', 'user', 'user3'),
('4', 'pwd', 'user4@gmail.com', 'user', 'user4'),
('5', 'pwd', 'user5@gmail.com', 'user', 'user5'),
('6', 'pwd', 'user6@gmail.com', 'user', 'user6');

insert into allergen (name, description) values
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

insert into dream_table (end, start, num_wakeups, dream_type) values
('2024-03-03 22:00:00.000000', '2024-03-04 06:30:00.000000', 2, 1),
('2024-03-04 23:15:00.000000', '2024-03-05 07:00:00.000000', 3, 2),
('2024-03-05 21:45:00.000000', '2024-03-06 05:20:00.000000', 1, 1),
('2024-03-06 00:30:00.000000', '2024-03-06 08:10:00.000000', 4, 3),
('2024-03-07 22:20:00.000000', '2024-03-08 06:00:00.000000', 2, 2),
('2024-03-08 23:50:00.000000', '2024-03-09 07:30:00.000000', 5, 1),
('2024-03-09 22:10:00.000000', '2024-03-10 05:50:00.000000', 1, 2),
('2024-03-10 23:40:00.000000', '2024-03-11 06:45:00.000000', 3, 3),
('2024-03-11 21:30:00.000000', '2024-03-12 07:10:00.000000', 4, 1);

INSERT INTO recipe_table (name, description, photo_route, min_recommended_age, max_recommended_age, elaboration) VALUES
('Puré de Zanahoria', 'Un delicioso puré de zanahoria para bebés.', '/images/pure_zanahoria.jpg', 2, 12, 'Cocinar las zanahorias y triturarlas.'),
('Papilla de Frutas', 'Papilla de frutas variadas para bebés.', '/images/papilla_frutas.jpg', 2, 10, 'Mezclar y triturar frutas frescas.'),
('Sopa de Pollo', 'Sopa de pollo suave y nutritiva.', '/images/sopa_pollo.jpg', 1, 24, 'Cocinar pollo con verduras y triturar.'),
('Galletas de Avena', 'Galletas saludables de avena.', '/images/galletas_avena.jpg', 4, 36, 'Mezclar avena con plátano y hornear.'),
('Puré de Calabaza', 'Puré de calabaza suave y dulce.', '/images/pure_calabaza.jpg', 3, 12, 'Cocinar la calabaza y triturarla.'),
('Compota de Manzana', 'Compota de manzana casera.', '/images/compota_manzana.jpg', 2, 10, 'Cocinar manzanas y triturarlas.'),
('Puré de Espinacas', 'Puré de espinacas rico en hierro.', '/images/pure_espinacas.jpg', 8, 18, 'Cocinar espinacas y triturarlas.'),
('Arroz con Leche', 'Postre de arroz con leche.', '/images/arroz_leche.jpg', 12, 36, 'Cocinar arroz con leche y canela.'),
('Puré de Patata', 'Puré de patata cremoso.', '/images/pure_patata.jpg', 3, 12, 'Cocinar patatas y triturarlas con leche.'),
('Yogur Natural', 'Yogur natural casero.', '/images/yogur_natural.jpg', 3, 24, 'Fermentar leche para hacer yogur.');


INSERT INTO baby_table (id, name, birth_date, genre, weight, height, cephalic_perimeter, food_preference) VALUES
(1, 'John', '2023-01-01', 'MALE', 3.5, 50, 35, 'Milk'),
(2, 'Jane', '2023-02-01', 'FEMALE', 3.2, 48, 34, 'Milk'),
(3, 'Alice', '2023-03-01', 'FEMALE', 3.8, 52, 36, 'Milk'),
(4, 'Bob', '2023-04-01', 'MALE', 3.6, 51, 35, 'Milk'),
(5, 'Charlie', '2023-05-01', 'MALE', 3.4, 49, 34, 'Milk');

INSERT INTO baby_allergen (baby_id, allergen_id) VALUES
(2, 2),
(3, 3),
(4, 4),
(5, 5);

INSERT INTO disease_table (id, baby_id, name, end_date, extra_observations, start_date, symptoms) VALUES
(1, 1, 'Flu', '2023-01-10', 'Mild fever', '2023-01-01', 'Cough, Fever'),
(2, 2, 'Cold', '2023-02-10', 'Runny nose', '2023-02-01', 'Sneezing, Congestion'),
(3, 3, 'Chickenpox', '2023-03-10', 'Itchy rash', '2023-03-01', 'Rash, Fever'),
(4, 4, 'Measles', '2023-04-10', 'High fever', '2023-04-01', 'Rash, Fever'),
(5, 5, 'Mumps', '2023-05-10', 'Swollen glands', '2023-05-01', 'Swelling, Fever');

-- Add vaccine records
INSERT INTO vaccine_table (id, baby_id, type, vaccination_date) VALUES
(1, 1, 'MMR', '2023-06-01'),
(2, 2, 'DTaP', '2023-07-01'),
(3, 3, 'HepB', '2023-08-01'),
(4, 4, 'Polio', '2023-09-01'),
(5, 5, 'Hib', '2023-10-01');
