-- MIGRATION: Corrigir a chave estrangeira de tratamento_id para referenciar a tabela tratamento

-- Remover a constraint incorreta que referencia procedimento
ALTER TABLE atendimento.atendimento
DROP CONSTRAINT IF EXISTS atendimento_tratamento_id_fkey;

-- Adicionar a constraint correta que referencia a tabela tratamento
ALTER TABLE atendimento.atendimento
ADD CONSTRAINT atendimento_tratamento_id_fkey 
FOREIGN KEY (tratamento_id) REFERENCES atendimento.tratamento(id_tratamento);
