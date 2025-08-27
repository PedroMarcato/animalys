-- MIGRATION: Remover campos de dados de consulta da tabela animal e adicionar na tabela atendimento

ALTER TABLE animal.animal
DROP COLUMN IF EXISTS escore_corporal,
DROP COLUMN IF EXISTS fc,
DROP COLUMN IF EXISTS fr,
DROP COLUMN IF EXISTS temperatura,
DROP COLUMN IF EXISTS tratamento_id;

ALTER TABLE atendimento.atendimento
ADD COLUMN escore_corporal VARCHAR(255),
ADD COLUMN fc VARCHAR(50),
ADD COLUMN fr VARCHAR(50),
ADD COLUMN temperatura VARCHAR(50),
ADD COLUMN tratamento_id INTEGER REFERENCES atendimento.procedimento(idProcedimento);
