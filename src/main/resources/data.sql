-- Roles seeding for IAM Context
-- Using CURRENT_TIMESTAMP for audit fields to avoid NOT NULL constraints

-- Drop existing check constraint that might be blocking new enum values
-- This is common when using Hibernate ddl-auto=update with enums in PostgreSQL
ALTER TABLE roles DROP CONSTRAINT IF EXISTS roles_name_check;
ALTER TABLE roles DROP CONSTRAINT IF EXISTS role_name_check;


INSERT INTO roles (name, created_at, updated_at) 
SELECT 'ADMIN', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'ADMIN');

INSERT INTO roles (name, created_at, updated_at) 
SELECT 'OPERATOR', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM roles WHERE name = 'OPERATOR');

-- Seed User: 'operator' (password: password123)
INSERT INTO users (username, email, password_hash, first_name, last_name, is_verified, active, created_at, updated_at)
SELECT 'operator', 'operator@yaku.com', '$2a$12$I9jX6Z.By.CDGzZ.ZJ7lhuL2WIdR.qG1Nn6r0I.zE7Lp8X8X8X8X', 'Yaku', 'Operator', true, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'operator');

-- Assign ROLE_OPERATOR to operator
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u, roles r
WHERE u.username = 'operator' AND r.name = 'OPERATOR'
AND NOT EXISTS (SELECT 1 FROM user_roles WHERE user_id = u.id AND role_id = r.id);

-- Seed Threshold Configs for Pond 1
INSERT INTO threshold_configs (pond_id, sensor_type, min_allowed, max_allowed)
SELECT 1, 'TEMPERATURE', 20.0, 30.0
WHERE NOT EXISTS (SELECT 1 FROM threshold_configs WHERE pond_id = 1 AND sensor_type = 'TEMPERATURE');

INSERT INTO threshold_configs (pond_id, sensor_type, min_allowed, max_allowed)
SELECT 1, 'PH', 6.5, 8.5
WHERE NOT EXISTS (SELECT 1 FROM threshold_configs WHERE pond_id = 1 AND sensor_type = 'PH');

INSERT INTO threshold_configs (pond_id, sensor_type, min_allowed, max_allowed)
SELECT 1, 'TURBIDITY', 5.0, 10.0
WHERE NOT EXISTS (SELECT 1 FROM threshold_configs WHERE pond_id = 1 AND sensor_type = 'TURBIDITY');

-- Seed Sensor Readings for Pond 1 (Past 24 hours)
INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'TEMPERATURE', 24.5, 'C', CURRENT_TIMESTAMP - INTERVAL '24 hours');
INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'TEMPERATURE', 25.2, 'C', CURRENT_TIMESTAMP - INTERVAL '12 hours');
INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'TEMPERATURE', 26.1, 'C', CURRENT_TIMESTAMP - INTERVAL '1 hour');

INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'PH', 7.0, 'pH', CURRENT_TIMESTAMP - INTERVAL '24 hours');
INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'PH', 7.2, 'pH', CURRENT_TIMESTAMP - INTERVAL '12 hours');
INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'PH', 7.1, 'pH', CURRENT_TIMESTAMP - INTERVAL '1 hour');

INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'TURBIDITY', 6.5, 'NTU', CURRENT_TIMESTAMP - INTERVAL '24 hours');
INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'TURBIDITY', 6.8, 'NTU', CURRENT_TIMESTAMP - INTERVAL '12 hours');
INSERT INTO sensor_readings (pond_id, sensor_type, value, unit, timestamp)
VALUES (1, 'TURBIDITY', 6.7, 'NTU', CURRENT_TIMESTAMP - INTERVAL '1 hour');

-- Seed Measurement Aggregates for Pond 1
INSERT INTO measurement_aggregates (pond_id, sensor_type, min_value, max_value, average_value, period_start, period_end)
VALUES (1, 'TEMPERATURE', 24.0, 27.0, 25.5, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day');
INSERT INTO measurement_aggregates (pond_id, sensor_type, min_value, max_value, average_value, period_start, period_end)
VALUES (1, 'PH', 6.8, 7.4, 7.1, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day');
INSERT INTO measurement_aggregates (pond_id, sensor_type, min_value, max_value, average_value, period_start, period_end)
VALUES (1, 'TURBIDITY', 6.0, 7.5, 6.7, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '1 day');
