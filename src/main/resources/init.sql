CREATE TABLE IF NOT EXISTS accounts
(
    id          SERIAL      NOT NULL,
    customer_id VARCHAR(80) NOT NULL,
    country     VARCHAR(120),
    create_at   TIMESTAMP   NOT NULL DEFAULT now(),
    PRIMARY KEY (id)
);

CREATE TYPE valid_currencies AS ENUM ('EUR','SEK','GBP','USD');
CREATE TYPE valid_directions AS ENUM ('IN','OUT');

CREATE TABLE IF NOT EXISTS transactions
(
    id          SERIAL           NOT NULL,
    account_id  INT              NOT NULL,
    amount      NUMERIC          NOT NULL DEFAULT 0,
    currency    valid_currencies NOT NULL,
    direction   valid_directions NOT NULL,
    description TEXT             NOT NULL,
    create_at   TIMESTAMP        NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)
);

CREATE TABLE IF NOT EXISTS balances
(
    id         SERIAL           NOT NULL,
    currency   valid_currencies NOT NULL,
    amount     NUMERIC          NOT NULL DEFAULT 0,
    account_id INT              NOT NULL,
    create_at  TIMESTAMP        NOT NULL DEFAULT now(),
    PRIMARY KEY (id),
    FOREIGN KEY (account_id) REFERENCES accounts (id)

);