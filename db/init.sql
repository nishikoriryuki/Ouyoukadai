PRAGMA foreign_keys = ON;

CREATE TABLE menus (
    menu_id INTEGER PRIMARY KEY AUTOINCREMENT,
    menu_name TEXT NOT NULL,
    calorie INTEGER NOT NULL,
    difficulty INTEGER NOT NULL
);

CREATE TABLE ingredients (
    ingredient_id INTEGER PRIMARY KEY AUTOINCREMENT,
    ingredient_name TEXT NOT NULL UNIQUE
);

CREATE TABLE allergens (
    allergen_id INTEGER PRIMARY KEY AUTOINCREMENT,
    allergen_name TEXT NOT NULL UNIQUE
);

CREATE TABLE menu_ingredients (
    menu_id INTEGER NOT NULL,
    ingredient_id INTEGER NOT NULL,
    PRIMARY KEY (menu_id, ingredient_id),
    FOREIGN KEY (menu_id) REFERENCES menus(menu_id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(ingredient_id)
);

CREATE TABLE ingredient_allergens (
    ingredient_id INTEGER NOT NULL,
    allergen_id INTEGER NOT NULL,
    PRIMARY KEY (ingredient_id, allergen_id),
    FOREIGN KEY (ingredient_id) REFERENCES ingredients(ingredient_id),
    FOREIGN KEY (allergen_id) REFERENCES allergens(allergen_id)
);

INSERT INTO menus (menu_name, calorie, difficulty) VALUES
('親子丼', 650, 2),
('カレーライス', 720, 2),
('クリームシチュー', 540, 2),
('えびチャーハン', 600, 2),
('豆腐ハンバーグ', 430, 1);

INSERT INTO allergens (allergen_name) VALUES
('卵'),
('乳'),
('小麦'),
('えび'),
('大豆');

INSERT INTO ingredients (ingredient_name) VALUES
('鶏肉'),
('卵'),
('玉ねぎ'),
('ご飯'),
('醤油'),
('みりん'),
('じゃがいも'),
('にんじん'),
('カレールウ'),
('牛肉'),
('牛乳'),
('小麦粉'),
('バター'),
('えび'),
('長ねぎ'),
('豆腐'),
('ひき肉'),
('パン粉'),
('塩'),
('こしょう');

INSERT INTO menu_ingredients (menu_id, ingredient_id) VALUES
(1, 1),
(1, 2),
(1, 3),
(1, 4),
(1, 5),
(1, 6),
(2, 10),
(2, 3),
(2, 7),
(2, 8),
(2, 9),
(2, 4),
(3, 1),
(3, 3),
(3, 7),
(3, 8),
(3, 11),
(3, 12),
(3, 13),
(4, 14),
(4, 2),
(4, 15),
(4, 4),
(4, 5),
(5, 16),
(5, 17),
(5, 3),
(5, 2),
(5, 18),
(5, 19),
(5, 20);

INSERT INTO ingredient_allergens (ingredient_id, allergen_id) VALUES
(2, 1),
(11, 2),
(13, 2),
(12, 3),
(18, 3),
(9, 3),
(14, 4),
(16, 5),
(5, 5);