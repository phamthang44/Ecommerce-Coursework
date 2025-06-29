CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime NULL,
    updated_at    datetime     NOT NULL,
    created_by    VARCHAR(255) NOT NULL,
    updated_by    VARCHAR(255) NOT NULL,
    full_name     NVARCHAR(100)         NOT NULL,
    date_of_birth date NULL,
    gender        VARCHAR(255) NULL,
    phone         VARCHAR(20)  NOT NULL,
    email         VARCHAR(255) NOT NULL,
    username      VARCHAR(100) NOT NULL,
    password      VARCHAR(255) NOT NULL,
    `role`        VARCHAR(255) NULL,
    status        VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_email UNIQUE (email);

ALTER TABLE user
    ADD CONSTRAINT uc_user_phone UNIQUE (phone);

ALTER TABLE user
    ADD CONSTRAINT uc_user_username UNIQUE (username);