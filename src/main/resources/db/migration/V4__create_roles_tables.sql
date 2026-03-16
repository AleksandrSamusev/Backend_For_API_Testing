-- 🚀 1. UPGRADE USERS: Only add the column if it doesn't already exist
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name='users' AND column_name='password') THEN
            ALTER TABLE users ADD COLUMN password VARCHAR(255) NOT NULL DEFAULT 'temporary_password';
        END IF;
    END $$;

-- 🚀 2. CREATE ROLES: Safe creation
CREATE TABLE IF NOT EXISTS roles (
                                     id BIGSERIAL PRIMARY KEY,
                                     name VARCHAR(50) UNIQUE NOT NULL
);

-- 🚀 3. CREATE JOIN TABLE: Safe creation
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          role_id BIGINT NOT NULL,
                                          PRIMARY KEY (user_id, role_id),
                                          CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                                          CONSTRAINT fk_role FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- 🚀 4. SEED ROLES: Use ON CONFLICT to prevent "Duplicate Key" errors
INSERT INTO roles (name) VALUES ('ROLE_USER') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;