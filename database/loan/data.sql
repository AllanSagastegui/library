CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS loans (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    book_id UUID NOT NULL,
    loan_date TIMESTAMP NOT NULL,
    estimated_return_date TIMESTAMP NOT NULL,
    date_of_real_return TIMESTAMP,
    status VARCHAR(20) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_loans_user_id ON loans(user_id);

CREATE INDEX IF NOT EXISTS idx_loans_status ON loans(status);