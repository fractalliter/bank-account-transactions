CREATE TABLE IF NOT EXISTS accounts(
    id SERIAL NOT NULL ,
    customer_id VARCHAR(80) NOT NULL,
    country VARCHAR(120),
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS transactions(
    id SERIAL NOT NULL ,
    account_id INT NOT NULL ,
    amount NUMERIC NOT NULL DEFAULT 0,
    currency VARCHAR(12) NOT NULL ,
    direction VARCHAR(4) NOT NULL ,
    description TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TYPE valid_currencies AS ENUM ('EUR','SEK','GBP','USD');
CREATE TABLE IF NOT EXISTS balances(
    id SERIAL NOT NULL ,
    currency valid_currencies NOT NULL,
    amount NUMERIC NOT NULL DEFAULT 0,
    account_id INT NOT NULL ,
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)

);