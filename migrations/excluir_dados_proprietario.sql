-- Script SQL para excluir todos os atendimentos e solicitações de um proprietário específico
-- Este script considera todas as dependências de chave estrangeira identificadas no sistema
-- 
-- IMPORTANTE: Substituir :id_proprietario pelo ID do proprietário desejado
-- 
-- ATENÇÃO: Este script irá excluir PERMANENTEMENTE todos os dados relacionados
-- Fazer backup antes de executar!

-- Definir o ID do proprietário (ALTERAR ESTE VALOR)
-- Exemplo: Substituir por WHERE p.idproprietario = 123
-- Para testar primeiro, use SELECT para ver quais dados serão excluídos

-- Iniciar transação com tratamento de erro
DO $$
DECLARE
    proprietario_id INTEGER := :id_proprietario; -- ALTERAR ESTE VALOR
    cnt INTEGER;
BEGIN
    -- Verificar se o proprietário existe
    IF NOT EXISTS (SELECT 1 FROM animal.proprietario WHERE idproprietario = proprietario_id) THEN
        RAISE EXCEPTION 'Proprietário com ID % não encontrado', proprietario_id;
    END IF;

    RAISE NOTICE 'Iniciando exclusão para proprietário ID: %', proprietario_id;

    -- 1. Excluir itens de lote dos atendimentos (ItemLoteAtendimento)
    DELETE FROM atendimento.item_lote_atendimento 
    WHERE atendimento IN (
        SELECT a.idatendimento 
        FROM atendimento.atendimento a
        INNER JOIN animal.animal an ON a.animal = an.idanimal
        INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
        WHERE p.idproprietario = proprietario_id
    );
    
    GET DIAGNOSTICS cnt = ROW_COUNT;
    RAISE NOTICE 'Excluídos % itens de lote', cnt;

    -- 2. Excluir relacionamentos de procedimentos com atendimentos (atendimento_procedimento)
    -- Assumindo que existe uma tabela de junção conforme indicado pelas entidades
    DELETE FROM atendimento.atendimento_procedimento 
    WHERE atendimentos_idatendimento IN (
        SELECT a.idatendimento 
        FROM atendimento.atendimento a
        INNER JOIN animal.animal an ON a.animal = an.idanimal
        INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
        WHERE p.idproprietario = proprietario_id
    );
    
    GET DIAGNOSTICS cnt = ROW_COUNT;
    RAISE NOTICE 'Excluídos % relacionamentos atendimento-procedimento', cnt;

    -- 3. Excluir fotos dos animais (se existirem)
    DELETE FROM animal.foto_animal
    WHERE animal_id IN (
        SELECT an.idanimal
        FROM animal.animal an
        INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
        WHERE p.idproprietario = proprietario_id
    );
    
    GET DIAGNOSTICS cnt = ROW_COUNT;
    RAISE NOTICE 'Excluídas % fotos de animais', cnt;

    -- 4. Excluir atendimentos
    DELETE FROM atendimento.atendimento 
    WHERE animal IN (
        SELECT an.idanimal 
        FROM animal.animal an
        INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
        WHERE p.idproprietario = proprietario_id
    );
    
    GET DIAGNOSTICS cnt = ROW_COUNT;
    RAISE NOTICE 'Excluídos % atendimentos', cnt;

    -- 5. Excluir relacionamentos de animais com solicitações (solicitacao_animal)
    DELETE FROM atendimento.solicitacao_animal 
    WHERE animais_idanimal IN (
        SELECT an.idanimal 
        FROM animal.animal an
        INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
        WHERE p.idproprietario = proprietario_id
    );
    
    GET DIAGNOSTICS cnt = ROW_COUNT;
    RAISE NOTICE 'Excluídos % relacionamentos solicitacao-animal', cnt;

    -- 6. Excluir solicitações do proprietário
    DELETE FROM atendimento.solicitacao 
    WHERE proprietario IN (
        SELECT p.idproprietario 
        FROM animal.proprietario p
        WHERE p.idproprietario = proprietario_id
    );
    
    GET DIAGNOSTICS cnt = ROW_COUNT;
    RAISE NOTICE 'Excluídas % solicitações', cnt;

    RAISE NOTICE 'Exclusão concluída com sucesso para proprietário ID: %', proprietario_id;

EXCEPTION
    WHEN OTHERS THEN
        RAISE NOTICE 'ERRO durante a exclusão: %', SQLERRM;
        RAISE;
END $$;

-- Script opcional para verificar o que seria excluído (executar ANTES do script principal)
-- Descomente o bloco abaixo e execute para verificar:

/*
-- VERIFICAÇÃO - EXECUTE PRIMEIRO PARA VER O QUE SERÁ EXCLUÍDO:
DO $$
DECLARE
    proprietario_id INTEGER := :id_proprietario; -- ALTERAR ESTE VALOR
    cnt INTEGER;
BEGIN
    -- Verificar se o proprietário existe
    SELECT COUNT(*) INTO cnt FROM animal.proprietario WHERE idproprietario = proprietario_id;
    IF cnt = 0 THEN
        RAISE NOTICE 'AVISO: Proprietário com ID % não encontrado', proprietario_id;
        RETURN;
    END IF;

    RAISE NOTICE '=== VERIFICAÇÃO PARA PROPRIETÁRIO ID: % ===', proprietario_id;

    -- Verificar atendimentos que serão excluídos
    SELECT COUNT(*) INTO cnt
    FROM atendimento.atendimento a
    INNER JOIN animal.animal an ON a.animal = an.idanimal
    INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
    WHERE p.idproprietario = proprietario_id;
    RAISE NOTICE 'ATENDIMENTOS A SEREM EXCLUÍDOS: %', cnt;

    -- Verificar solicitações que serão excluídas
    SELECT COUNT(*) INTO cnt
    FROM atendimento.solicitacao s
    INNER JOIN animal.proprietario p ON s.proprietario = p.idproprietario
    WHERE p.idproprietario = proprietario_id;
    RAISE NOTICE 'SOLICITAÇÕES A SEREM EXCLUÍDAS: %', cnt;

    -- Verificar itens de lote que serão excluídos
    SELECT COUNT(*) INTO cnt
    FROM atendimento.item_lote_atendimento ila
    INNER JOIN atendimento.atendimento a ON ila.atendimento = a.idatendimento
    INNER JOIN animal.animal an ON a.animal = an.idanimal
    INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
    WHERE p.idproprietario = proprietario_id;
    RAISE NOTICE 'ITENS DE LOTE A SEREM EXCLUÍDOS: %', cnt;

    -- Verificar animais do proprietário (NÃO serão excluídos)
    SELECT COUNT(*) INTO cnt
    FROM animal.animal an
    INNER JOIN animal.proprietario p ON an.proprietario = p.idproprietario
    WHERE p.idproprietario = proprietario_id;
    RAISE NOTICE 'ANIMAIS DO PROPRIETÁRIO (NÃO EXCLUÍDOS): %', cnt;

    RAISE NOTICE '=== FIM DA VERIFICAÇÃO ===';
END $$;
*/

-- INSTRUÇÕES DE USO:
-- 1. Substituir :id_proprietario pelo ID real do proprietário (exemplo: WHERE p.idproprietario = 123)
-- 2. Executar primeiro o script de verificação (descomentando a seção VERIFICAÇÃO)
-- 3. Executar o script principal de exclusão
-- 
-- ESTRUTURA DE EXCLUSÃO (ordem das foreign keys):
-- 1. item_lote_atendimento (referencia atendimento)
-- 2. atendimento_procedimento (referencia atendimento)  
-- 3. foto_animal (referencia animal)
-- 4. atendimento (referencia animal e solicitacao)
-- 5. solicitacao_animal (referencia animal e solicitacao)
-- 6. solicitacao (referencia proprietario)
--
-- OS ANIMAIS NÃO SÃO EXCLUÍDOS - apenas seus atendimentos e solicitações
