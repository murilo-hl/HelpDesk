-- HelpDesk: cria o banco "helpdesk" (se você tinha um banco de uma versão anterior, ele não é alterado).
CREATE DATABASE IF NOT EXISTS helpdesk CHARACTER SET utf8mb4;
USE helpdesk;

CREATE TABLE IF NOT EXISTS usuarios (
    id     INT AUTO_INCREMENT PRIMARY KEY,
    nome   VARCHAR(50) NOT NULL UNIQUE,      -- o nome é usado para entrar
    senha  VARCHAR(50) NOT NULL,             -- texto puro: só serve para trabalho escolar
    perfil VARCHAR(10) NOT NULL              -- USUARIO, TECNICO ou ADMIN
);

CREATE TABLE IF NOT EXISTS chamados (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    titulo        VARCHAR(100) NOT NULL,
    descricao     TEXT NOT NULL,
    prioridade    VARCHAR(10) NOT NULL DEFAULT 'MEDIA',      -- BAIXA, MEDIA, ALTA
    status        VARCHAR(15) NOT NULL DEFAULT 'ABERTO',     -- ABERTO, EM_ANDAMENTO, RESOLVIDO
    solucao       TEXT NULL,
    usuario_id    INT NOT NULL,                              -- quem abriu
    tecnico_id    INT NULL,                                  -- quem atende
    data_abertura DATETIME NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    FOREIGN KEY (tecnico_id) REFERENCES usuarios (id)
);

-- Administrador (login específico): nome "admin", senha "admin123"
INSERT INTO usuarios (nome, senha, perfil) VALUES
    ('admin',   'admin123', 'ADMIN'),
    ('tecnico', '1234',     'TECNICO'),
    ('maria',   '1234',     'USUARIO'),
    ('joao',    '1234',     'USUARIO');

INSERT INTO chamados (titulo, descricao, prioridade, status, usuario_id, tecnico_id, data_abertura) VALUES
    ('Computador não liga',   'Sem sinal de vídeo ao ligar.',          'ALTA',  'EM_ANDAMENTO', 3, 2,    NOW()),
    ('Impressora sem papel',  'Mostra erro, mas a bandeja está cheia.', 'MEDIA', 'ABERTO',       4, NULL, NOW());
