CREATE TABLE IF NOT EXISTS employee (
    id BIGSERIAL                PRIMARY KEY,
    first_name                   VARCHAR(50) NOT NULL,
    last_name                   VARCHAR(50) NOT NULL,
    birthdate                   DATE NOT NULL,
    department                  VARCHAR(50),
    employee                    VARCHAR(50),
    password                    VARCHAR(255) NOT NULL,
    salary                      DECIMAL(10,2) NOT NULL,
    email                       VARCHAR(50) NOT NULL,
    creation_date               TIMESTAMP WITHOUT TIME ZONE NOT NULL
);

CREATE TABLE IF NOT EXISTS role (
    id                         BIGSERIAL PRIMARY KEY,
    role                       VARCHAR(255) NOT NULL
);

INSERT INTO role(role) VALUES ('ROLE_ADMIN'), ('ROLE_USER');

CREATE TABLE IF NOT EXISTS employee2role (
    employee_id                BIGINT NOT NULL REFERENCES employee(id),
    role_id                    BIGINT NOT NULL REFERENCES role(id)
);