CREATE TABLE users (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),

    -- BaseEntity fields
                       created_at TIMESTAMP,
                       created_by VARCHAR(255),
                       updated_at TIMESTAMP,
                       updated_by VARCHAR(255),
                       deleted BOOLEAN DEFAULT FALSE,

    -- User fields
                       email VARCHAR(255) UNIQUE NOT NULL,
                       date_of_birth TIMESTAMP,
                       gender VARCHAR(50),
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       phone_number VARCHAR(20) NOT NULL,
                       user_name VARCHAR(100) UNIQUE,
                       password VARCHAR(255),
                       role VARCHAR(50),
                       refresh_token TEXT
);
