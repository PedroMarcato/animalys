CREATE TABLE IF NOT EXISTS public.documentospessoais (
    id_documentos SERIAL PRIMARY KEY,
    card_unico VARCHAR(255),
    documento_com_foto VARCHAR(255),
    comprovante_endereco VARCHAR(255)
);

-- Adiciona coluna de referência na tabela animal.proprietario, se não existir
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = 'animal'
          AND table_name = 'proprietario'
          AND column_name = 'documentos'
    ) THEN
        ALTER TABLE animal.proprietario
        ADD COLUMN documentos INTEGER REFERENCES public.documentospessoais(id_documentos);
    END IF;
END
$$;
