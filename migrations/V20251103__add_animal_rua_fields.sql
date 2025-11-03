-- Migration para adicionar campos de Animal de Rua
-- Data: 03/11/2025

-- Adicionar coluna para responsável pela entrega
ALTER TABLE animal.animal 
ADD COLUMN IF NOT EXISTS responsavel_entrega VARCHAR(100);

-- Adicionar coluna para endereço
ALTER TABLE animal.animal 
ADD COLUMN IF NOT EXISTS endereco_entrega VARCHAR(200);

-- Adicionar coluna para celular
ALTER TABLE animal.animal 
ADD COLUMN IF NOT EXISTS celular_entrega VARCHAR(20);

-- Comentários para documentação
COMMENT ON COLUMN animal.animal.responsavel_entrega IS 'Nome do responsável pela entrega do animal de rua';
COMMENT ON COLUMN animal.animal.endereco_entrega IS 'Endereço onde o animal de rua foi encontrado/entregue';
COMMENT ON COLUMN animal.animal.celular_entrega IS 'Celular do responsável pela entrega do animal de rua';
