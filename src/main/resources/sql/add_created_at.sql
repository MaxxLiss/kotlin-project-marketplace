ALTER TABLE "UserDetails"
    ADD COLUMN created_at timestamp NOT NULL DEFAULT '2024-10-10 00:01:03';

ALTER TABLE "UserDetails"
    ALTER COLUMN created_at
        DROP DEFAULT;

ALTER TABLE "User"
    ADD COLUMN created_at timestamp NOT NULL DEFAULT '2024-10-10 00:01:03';

ALTER TABLE "User"
    ALTER COLUMN created_at
        DROP DEFAULT;