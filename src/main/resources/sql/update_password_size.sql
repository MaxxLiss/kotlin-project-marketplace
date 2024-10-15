ALTER TABLE "UserDetails"
    ALTER COLUMN password TYPE varchar(255) using password::varchar(255);
