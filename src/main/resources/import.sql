-- Active extension to generate uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ===========================
-- RAW MATERIALS
-- ===========================

-- KG (large weighable solids)
INSERT INTO tb_raw_materials (id, code, name, stock_quantity, unit_type) VALUES
    (uuid_generate_v4(), 'RM001', 'Polietileno', 500, 'KG'),
    (uuid_generate_v4(), 'RM002', 'Polipropileno', 300, 'KG'),

-- G (small solids)
    (uuid_generate_v4(), 'RM003', 'Corante Azul', 400, 'G'),
    (uuid_generate_v4(), 'RM004', 'Corante Vermelho', 350, 'G'),

-- L (litres)
    (uuid_generate_v4(), 'RM005', 'Resina Líquida', 200, 'L'),
    (uuid_generate_v4(), 'RM006', 'Plastificante Líquido', 150, 'L'),

-- ML (millilitres)
    (uuid_generate_v4(), 'RM007', 'Catalisador', 500, 'ML'),
    (uuid_generate_v4(), 'RM008', 'Aditivo UV', 300, 'ML'),

-- UNIT (individual pieces)
    (uuid_generate_v4(), 'RM009', 'Tampa Plástica', 1000, 'UNIT'),
    (uuid_generate_v4(), 'RM010', 'Copo Descartável', 2000, 'UNIT'),

-- PACK (packages or sets)
    (uuid_generate_v4(), 'RM011', 'Kit de Montagem', 50, 'PACK'),
    (uuid_generate_v4(), 'RM012', 'Pacote de Tampas', 80, 'PACK'),

-- BOX (boxes)
    (uuid_generate_v4(), 'RM013', 'Caixa de Transporte Pequena', 30, 'BOX'),
    (uuid_generate_v4(), 'RM014', 'Caixa de Transporte Média', 20, 'BOX'),

-- ROLL (rolls, fabrics, tapes)
    (uuid_generate_v4(), 'RM015', 'Fita Adesiva', 10, 'ROLL'),
    (uuid_generate_v4(), 'RM016', 'Rolo de PVC', 15, 'ROLL'),

-- SHEET (sheets, plates)
    (uuid_generate_v4(), 'RM017', 'Chapa de Acrílico', 25, 'SHEET'),
    (uuid_generate_v4(), 'RM018', 'Placa de Policarbonato', 18, 'SHEET'),

-- M (Meter)
    (uuid_generate_v4(), 'RM019', 'Cabo de Nylon', 100, 'M'),
    (uuid_generate_v4(), 'RM020', 'Mangueira Plástica', 75, 'M');

-- ===========================
-- PRODUCTS
-- ===========================

INSERT INTO tb_products (id, code, name, price, description) VALUES
    (uuid_generate_v4(), 'P001', 'Caixa Plástica Pequena', 25.50, 'Caixa plástica para armazenamento de pequenos itens'),
    (uuid_generate_v4(), 'P002', 'Caixa Plástica Média', 40.00, 'Caixa plástica para transporte e armazenamento'),
    (uuid_generate_v4(), 'P003', 'Caixa Plástica Grande', 60.00, 'Caixa plástica de grande porte para transporte'),
    (uuid_generate_v4(), 'P004', 'Balde Plástico 10L', 35.00, 'Balde plástico resistente de 10 litros'),
    (uuid_generate_v4(), 'P005', 'Balde Plástico 20L', 50.00, 'Balde plástico resistente de 20 litros'),
    (uuid_generate_v4(), 'P006', 'Copo Descartável 200ml', 0.50, 'Copo plástico descartável para bebidas frias'),
    (uuid_generate_v4(), 'P007', 'Copo Descartável 300ml', 0.70, 'Copo plástico descartável para bebidas frias'),
    (uuid_generate_v4(), 'P008', 'Tampa de Copo 200ml', 0.20, 'Tampa plástica para copo de 200ml'),
    (uuid_generate_v4(), 'P009', 'Tampa de Copo 300ml', 0.25, 'Tampa plástica para copo de 300ml'),
    (uuid_generate_v4(), 'P010', 'Garrafa PET 500ml', 3.50, 'Garrafa PET para bebidas de 500ml');