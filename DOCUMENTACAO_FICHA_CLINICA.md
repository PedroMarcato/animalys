# Cadastro de Fichas Clínicas - Documentação

## Visão Geral
Esta funcionalidade permite o cadastro manual de fichas clínicas no sistema, facilitando a migração de dados de formulários em papel para o sistema digital.

## Como Acessar
1. Acesse a página "Proprietários" no menu de relatórios
2. Clique no botão "Nova Ficha Clínica" no canto superior direito

## Fluxo de Uso

### Etapa 1: Pesquisar Proprietário
- Digite o CPF do proprietário no formato XXX.XXX.XXX-XX
- Clique em "Pesquisar"
- O sistema exibirá os dados do proprietário encontrado

### Etapa 2: Selecionar Animal
- Escolha o animal na lista suspensa
- O sistema mostrará os dados completos do animal selecionado
- Informará se o animal possui microchip

### Etapa 3: Visualizar Histórico
- Lista todos os atendimentos anteriores do animal
- Mostra data, tipo, profissional e diagnóstico
- Contador total de atendimentos

### Etapa 4: Cadastrar Novo Atendimento
- Clique em "Novo Atendimento"
- Escolha o tipo: Consulta Clínica ou Castração
- Preencha os dados conforme o tipo selecionado

## Tipos de Atendimento

### Consulta Clínica
**Dados Gerais:**
- Data e hora do atendimento
- Profissional responsável
- Peso, escore corporal, FC, FR, temperatura
- Tratamento aplicado

**Abas Disponíveis:**
- Diagnóstico/Parecer (obrigatório)
- Medicamentos utilizados
- Atualização de microchip

### Castração
**Dados Gerais:**
- Data e hora do atendimento
- Profissional responsável

**Abas Disponíveis:**
- Diagnóstico/Parecer (obrigatório)
- Procedimentos realizados
- Medicamentos utilizados
- Atualização de microchip

## Funcionalidades Especiais

### Gerenciamento de Medicamentos
- Busca por nome do medicamento
- Seleção automática do lote mais próximo do vencimento
- Controle de estoque automático
- Validação de quantidade disponível

### Gerenciamento de Procedimentos
- Busca por nome do procedimento
- Adição múltipla de procedimentos
- Remoção com confirmação

### Atualização de Microchip
- Campo dedicado para inserir/atualizar número do microchip
- Visualização do status atual (com/sem microchip)

## Validações Implementadas

### Obrigatórias
- CPF do proprietário
- Seleção do animal
- Profissional responsável
- Diagnóstico/parecer

### Estoque
- Quantidade disponível vs. quantidade solicitada
- Validade dos medicamentos
- Atualização automática do estoque

### Integridade
- Criação automática de solicitação para manter consistência
- Atualização do status do animal (castrado quando aplicável)
- Histórico completo preservado

## Benefícios
- Interface intuitiva dividida em etapas
- Validações que previnem erros
- Integração completa com o sistema existente
- Histórico completo de atendimentos
- Controle automático de estoque
- Facilita a digitalização de fichas em papel

## Notas Técnicas
- Utiliza os mesmos repositórios e validações do sistema principal
- Mantém consistência de dados
- Interface responsiva compatível com diferentes dispositivos
- Mensagens informativas para guiar o usuário
