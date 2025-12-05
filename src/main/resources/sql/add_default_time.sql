ALTER TABLE user_details
    ALTER COLUMN created_at
        SET DEFAULT NOW();

ALTER TABLE user_info
    ALTER COLUMN created_at
        SET DEFAULT NOW();