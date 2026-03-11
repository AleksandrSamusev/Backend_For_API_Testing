-- 1. Add the column (Allow NULL initially)
ALTER TABLE addresses ADD COLUMN IF NOT EXISTS address_type varchar(20);

-- 2. Migrate existing data (Assume current addresses are PRIMARY)
UPDATE addresses SET address_type = 'PRIMARY' WHERE address_type IS NULL;

-- 3. Enforce the constraint for all future data
ALTER TABLE addresses ALTER COLUMN address_type SET NOT NULL;