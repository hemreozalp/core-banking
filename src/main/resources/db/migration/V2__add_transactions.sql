CREATE TABLE transactions (
    id UUID PRIMARY KEY,
    source_account_id BIGINT,
    target_account_id BIGINT,
    amount NUMERIC(15,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_source_account FOREIGN KEY (source_account_id) REFERENCES accounts(id),
    CONSTRAINT fk_target_account FOREIGN KEY (target_account_id) REFERENCES accounts(id)
);