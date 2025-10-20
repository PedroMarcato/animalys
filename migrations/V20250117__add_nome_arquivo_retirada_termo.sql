-- Migration para adicionar coluna de nome_arquivo nas tabelas de retirada de medicamento e termo itraconazol
-- Autor: Sistema Animalys
-- Data: 2025-01-17

-- Adicionar coluna nome_arquivo na tabela retirada_medicamento
ALTER TABLE estoque.retiradamedicamento 
ADD COLUMN nome_arquivo VARCHAR(255);

-- Adicionar comentário na coluna
COMMENT ON COLUMN estoque.retiradamedicamento.nome_arquivo IS 'Nome único do arquivo (UUID) de receita/laudo anexado';

-- Adicionar coluna nome_arquivo na tabela termo_itraconazol
ALTER TABLE animal.termo_itraconazol 
ADD COLUMN nome_arquivo VARCHAR(255);

-- Adicionar comentário na coluna
COMMENT ON COLUMN animal.termo_itraconazol.nome_arquivo IS 'Nome único do arquivo (UUID) de receita/laudo anexado';
