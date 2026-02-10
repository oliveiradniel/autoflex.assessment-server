-- Active extension to generate uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===========================
-- RAW MATERIALS
-- ===========================

INSERT INTO
    tb_raw_materials (id, code, name, stock_quantity, unit_type)
VALUES
    (uuid_generate_v4(), 'RM-001', 'Aço Carbono',                812.75, 'KG'),
    (uuid_generate_v4(), 'RM-002', 'Alumínio',                   603.40, 'KG'),
    (uuid_generate_v4(), 'RM-003', 'Parafuso Sextavado',        1523, 'UNIT'),
    (uuid_generate_v4(), 'RM-004', 'Porca Sextavada',           1488, 'UNIT'),
    (uuid_generate_v4(), 'RM-005', 'Tinta Azul Industrial',      197.35, 'L'),
    (uuid_generate_v4(), 'RM-006', 'Tinta Branca Industrial',    148.90, 'L'),
    (uuid_generate_v4(), 'RM-007', 'Solvente Químico',           96.75, 'L'),
    (uuid_generate_v4(), 'RM-008', 'Cola Industrial',             96.55, 'KG'),
    (uuid_generate_v4(), 'RM-009', 'Chapa de MDF',               118, 'SHEET'),
    (uuid_generate_v4(), 'RM-010', 'Chapa de Aço Inox',           74, 'SHEET'),
    (uuid_generate_v4(), 'RM-011', 'Cabo Elétrico',              987.60, 'M'),
    (uuid_generate_v4(), 'RM-012', 'Mangueira Hidráulica',       433.25, 'M'),
    (uuid_generate_v4(), 'RM-013', 'Caixa de Papelão',           321, 'BOX'),
    (uuid_generate_v4(), 'RM-014', 'Borracha Vedante',            37.80, 'KG'),
    (uuid_generate_v4(), 'RM-015', 'Vidro Temperado',             58, 'SHEET'),
    (uuid_generate_v4(), 'RM-016', 'Plástico ABS',               284.90, 'KG'),
    (uuid_generate_v4(), 'RM-017', 'Etiqueta Adesiva',          2350, 'UNIT'),
    (uuid_generate_v4(), 'RM-018', 'Espuma Protetora',           126.45, 'KG'),
    (uuid_generate_v4(), 'RM-019', 'Resina Epóxi',               176.45, 'L'),
    (uuid_generate_v4(), 'RM-020', 'Pino Metálico',             1890, 'UNIT');


-- ===========================
-- PRODUCTS
-- ===========================

INSERT INTO
    tb_products (id, code, name, price, description)
VALUES
    (uuid_generate_v4(), 'P-001', 'Suporte Metálico Industrial',      185.70, 'Suporte reforçado para estruturas'),
    (uuid_generate_v4(), 'P-002', 'Painel Metálico Pintado',         240.50, 'Painel com acabamento em tinta industrial'),
    (uuid_generate_v4(), 'P-003', 'Gabinete Elétrico',               320.90, 'Gabinete para componentes elétricos'),
    (uuid_generate_v4(), 'P-004', 'Mesa Técnica MDF',                410.30, 'Mesa técnica para uso industrial'),
    (uuid_generate_v4(), 'P-005', 'Estrutura Hidráulica',            515.80, 'Estrutura para sistemas hidráulicos'),
    (uuid_generate_v4(), 'P-006', 'Kit Fixação Industrial',           95.40, 'Kit com parafusos e porcas'),
    (uuid_generate_v4(), 'P-007', 'Painel de Vidro Protetor',        278.65, 'Painel protetor com vidro temperado'),
    (uuid_generate_v4(), 'P-008', 'Caixa Organizadora ABS',          130.25, 'Caixa plástica organizadora'),
    (uuid_generate_v4(), 'P-009', 'Módulo de Vedação',               150.75, 'Módulo com borracha vedante'),
    (uuid_generate_v4(), 'P-010', 'Kit Embalagem Reforçada',          45.90, 'Kit para embalagem segura'),
    (uuid_generate_v4(), 'P-011', 'Placa Técnica Resinada',          360.40, 'Placa técnica com resina epóxi'),
    (uuid_generate_v4(), 'P-012', 'Base Estrutural Inox',            540.90, 'Base em aço inox'),
    (uuid_generate_v4(), 'P-013', 'Painel Sinalizador Pintado',      220.80, 'Painel sinalizador industrial'),
    (uuid_generate_v4(), 'P-014', 'Suporte Elétrico Cabeado',        305.60, 'Suporte com cabeamento elétrico'),
    (uuid_generate_v4(), 'P-015', 'Estrutura Modular MDF',           470.20, 'Estrutura modular em MDF'),
    (uuid_generate_v4(), 'P-016', 'Gabinete Vedado',                 390.10, 'Gabinete com vedação reforçada'),
    (uuid_generate_v4(), 'P-017', 'Módulo ABS Reforçado',            210.55, 'Módulo plástico ABS reforçado'),
    (uuid_generate_v4(), 'P-018', 'Base Metálica Soldada',           610.75, 'Base metálica de alta resistência'),
    (uuid_generate_v4(), 'P-019', 'Kit Montagem Técnica',             88.35, 'Kit de montagem geral'),
    (uuid_generate_v4(), 'P-020', 'Painel Técnico Completo',         725.90, 'Painel técnico industrial completo');

-- ===========================
-- ASSOCIATIONS
-- ===========================

INSERT INTO
    tb_product_materials (id, product_id, raw_material_id, quantity_needed)
SELECT
    uuid_generate_v4(),
    p.id,
    r.id,
    q.qty
FROM (VALUES
-- P-001
('P-001','RM-001', 22.90),
('P-001','RM-003', 18),
('P-001','RM-004', 18),

-- P-002
('P-002','RM-001', 15.40),
('P-002','RM-005', 2.35),

-- P-003
('P-003','RM-010', 3),
('P-003','RM-011', 12.40),

-- P-004
('P-004','RM-009', 2),
('P-004','RM-008', 1.25),

-- P-005
('P-005','RM-012', 9.75),
('P-005','RM-014', 0.85),

-- P-006
('P-006','RM-003', 45),
('P-006','RM-004', 45),

-- P-007
('P-007','RM-015', 1),
('P-007','RM-008', 0.75),

-- P-008
('P-008','RM-016', 4.60),
('P-008','RM-017', 12),

-- P-009
('P-009','RM-014', 1.10),
('P-009','RM-008', 0.65),

-- P-010
('P-010','RM-013', 1),
('P-010','RM-018', 0.95),

-- P-011
('P-011','RM-019', 2.35),
('P-011','RM-009', 1),

-- P-012
('P-012','RM-010', 5),
('P-012','RM-003', 22),

-- P-013
('P-013','RM-005', 1.85),
('P-013','RM-017', 8),

-- P-014
('P-014','RM-011', 16.80),
('P-014','RM-020', 14),

-- P-015
('P-015','RM-009', 3),
('P-015','RM-008', 2.15),

-- P-016
('P-016','RM-014', 1.40),
('P-016','RM-010', 2),

-- P-017
('P-017','RM-016', 3.25),
('P-017','RM-017', 10),

-- P-018
('P-018','RM-001', 28.70),
('P-018','RM-020', 32),

-- P-019
('P-019','RM-003', 25),
('P-019','RM-004', 25),

-- P-020
('P-020','RM-001', 18.60),
('P-020','RM-005', 3.45),
('P-020','RM-011', 20.90)
     ) AS q(prod, raw, qty)
         JOIN tb_products p ON p.code = q.prod
         JOIN tb_raw_materials r ON r.code = q.raw;