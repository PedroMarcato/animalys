ALTER TABLE animal.animal
ADD COLUMN escore_corporal VARCHAR(255),
ADD COLUMN fc VARCHAR(50),
ADD COLUMN fr VARCHAR(50),
ADD COLUMN temperatura VARCHAR(50),
ADD COLUMN cor VARCHAR(100),
ADD COLUMN tratamento_id INTEGER REFERENCES atendimento.procedimento(idProcedimento);
