-- Adiciona campos de quantidade mensal para RetiradaRacao

ALTER TABLE estoque.retiradacao 
ADD quantidade1mes DECIMAL(10,3),
    quantidade2mes DECIMAL(10,3),
    quantidade3mes DECIMAL(10,3),
    quantidade4mes DECIMAL(10,3),
    quantidade5mes DECIMAL(10,3),
    quantidade6mes DECIMAL(10,3),
    quantidade7mes DECIMAL(10,3),
    quantidade8mes DECIMAL(10,3),
    quantidade9mes DECIMAL(10,3),
    quantidade10mes DECIMAL(10,3),
    quantidade11mes DECIMAL(10,3),
    quantidade12mes DECIMAL(10,3);
