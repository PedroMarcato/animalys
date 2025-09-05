-- Adicionar novas colunas à tabela animal
ALTER TABLE animal ADD COLUMN IF NOT EXISTS datanascimento timestamp;
ALTER TABLE animal ADD COLUMN IF NOT EXISTS peso varchar(255);
ALTER TABLE animal ADD COLUMN IF NOT EXISTS observacoes varchar(1000);

-- Comentários para documentação
COMMENT ON COLUMN animal.datanascimento IS 'Data de nascimento do animal';
COMMENT ON COLUMN animal.peso IS 'Peso do animal';
COMMENT ON COLUMN animal.observacoes IS 'Observações gerais sobre o animal';
