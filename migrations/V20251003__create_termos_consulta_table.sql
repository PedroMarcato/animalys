-- Criação da tabela para armazenar os termos de consulta assinados
CREATE TABLE animal.termos_consulta (
    id SERIAL PRIMARY KEY,
    proprietario_id INTEGER NOT NULL REFERENCES animal.proprietario(id_proprietario),
    animal_id INTEGER NOT NULL REFERENCES animal.animal(id_animal),
    nome_proprietario VARCHAR(200) NOT NULL,
    rg_proprietario VARCHAR(20) NOT NULL,
    cpf_proprietario VARCHAR(14) NOT NULL,
    endereco_proprietario VARCHAR(500) NOT NULL,
    nome_animal VARCHAR(100) NOT NULL,
    ficha_controle VARCHAR(50) NOT NULL,
    especie_animal VARCHAR(50) NOT NULL,
    sexo_animal VARCHAR(10) NOT NULL,
    cor_animal VARCHAR(50),
    data_assinatura DATE NOT NULL,
    data_cadastro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    observacoes TEXT,
    UNIQUE (proprietario_id, animal_id)
);

-- Índices para melhorar performance
CREATE INDEX idx_termos_consulta_proprietario ON animal.termos_consulta(proprietario_id);
CREATE INDEX idx_termos_consulta_animal ON animal.termos_consulta(animal_id);
CREATE INDEX idx_termos_consulta_data_assinatura ON animal.termos_consulta(data_assinatura);
CREATE INDEX idx_termos_consulta_data_cadastro ON animal.termos_consulta(data_cadastro);