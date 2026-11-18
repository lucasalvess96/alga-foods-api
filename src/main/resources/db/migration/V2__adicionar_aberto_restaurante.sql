ALTER TABLE foods.restaurante
    ADD aberto BIT NOT NULL CONSTRAINT DF_restaurante_aberto DEFAULT (0) WITH VALUES;
