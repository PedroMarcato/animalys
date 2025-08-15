-- Script para criar a tabela foto_animal
-- Execute este script no seu banco PostgreSQL

CREATE TABLE IF NOT EXISTS animal.foto_animal (
    id BIGSERIAL PRIMARY KEY,
    caminho_arquivo VARCHAR(500) NOT NULL,
    nome_original VARCHAR(255) NOT NULL,
    tipo_mime VARCHAR(100) NOT NULL,
    tamanho BIGINT NOT NULL,
    data_upload TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    principal BOOLEAN NOT NULL DEFAULT FALSE,
    animal_id INTEGER NOT NULL,
    
    CONSTRAINT fk_foto_animal_animal 
        FOREIGN KEY (animal_id) 
        REFERENCES animal.animal(idanimal) 
        ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_foto_animal_animal_id ON animal.foto_animal(animal_id);
CREATE INDEX IF NOT EXISTS idx_foto_animal_principal ON animal.foto_animal(principal);
CREATE INDEX IF NOT EXISTS idx_foto_animal_data_upload ON animal.foto_animal(data_upload);

-- Comentários nas colunas
COMMENT ON TABLE animal.foto_animal IS 'Tabela para armazenar fotos dos animais';
COMMENT ON COLUMN animal.foto_animal.id IS 'Identificador único da foto';
COMMENT ON COLUMN animal.foto_animal.caminho_arquivo IS 'Caminho completo do arquivo no sistema de arquivos';
COMMENT ON COLUMN animal.foto_animal.nome_original IS 'Nome original do arquivo enviado pelo usuário';
COMMENT ON COLUMN animal.foto_animal.tipo_mime IS 'Tipo MIME do arquivo (image/jpeg, image/png, etc.)';
COMMENT ON COLUMN animal.foto_animal.tamanho IS 'Tamanho do arquivo em bytes';
COMMENT ON COLUMN animal.foto_animal.data_upload IS 'Data e hora do upload da foto';
COMMENT ON COLUMN animal.foto_animal.principal IS 'Indica se esta é a foto principal do animal';
COMMENT ON COLUMN animal.foto_animal.animal_id IS 'Referência para o animal proprietário da foto';
