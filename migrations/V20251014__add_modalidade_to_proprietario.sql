-- Adiciona coluna modalidade na tabela proprietario
ALTER TABLE animal.proprietario 
ADD modalidade VARCHAR(50);

-- Comentário da coluna
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'Modalidade do proprietário/solicitante: PESSOA_FISICA, PROTETOR_INDIVIUAL_ANIMAIS, CAO_COMUNITARIO, TUTOR_VULNERABILIDADE_SOCIAL', 
    @level0type = N'SCHEMA', @level0name = 'animal',
    @level1type = N'TABLE',  @level1name = 'proprietario',
    @level2type = N'COLUMN', @level2name = 'modalidade';
