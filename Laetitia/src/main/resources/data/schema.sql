BEGIN:

--    CREATE TABLE IF NOT EXISTS oauth2_registered_client (
--        id varchar(100) NOT NULL,
--        client_id varchar(100) NOT NULL,
--        client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
--        client_secret varchar(200) DEFAULT NULL,
--        client_secret_expires_at timestamp DEFAULT NULL,
--        client_name varchar(200) NOT NULL,
--        client_authentication_methods varchar(1000) NOT NULL,
--        authorization_grant_types varchar(1000) NOT NULL,
--        redirect_uris varchar(1000) DEFAULT NULL,
--        post_logout_redirect_uris varchar(1000) DEFAULT NULL,
--        scopes varchar(1000) NOT NULL,
--        client_settings varchar(2000) NOT NULL,
--        token_settings varchar(2000) NOT NULL,
--        PRIMARY KEY (id)
--    );

    CREATE TABLE IF NOT EXISTS users (
        id SERIAL PRIMARY KEY,
        user_id VARCHAR(255) NOT NULL UNIQUE,
        first_name VARCHAR(255) NOT NULL,
        last_name VARCHAR(255) NOT NULL,
        email VARCHAR(255) UNIQUE NOT NULL,
        phone VARCHAR(255),
        bio VARCHAR(255),
        reference_id VARCHAR(255),
        qr_code_image_uri TEXT,
        image_url VARCHAR(255),
        last_login TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        login_attempts INTEGER DEFAULT 0,
        mfa BOOLEAN NOT NULL DEFAULT FALSE,
        enabled BOOLEAN NOT NULL DEFAULT FALSE,
        account_non_expired BOOLEAN NOT NULL DEFAULT FALSE,
        account_non_locked BOOLEAN NOT NULL DEFAULT FALSE,
        created_by BIGINT NOT NULL,
        updated_by BIGINT NOT NULL,
        created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_users_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
        CONSTRAINT fk_users_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE
    );

    CREATE TABLE IF NOT EXISTS confirmation (
        id SERIAL PRIMARY KEY,
        key VARCHAR(255),
        user_id BIGINT NOT NULL UNIQUE,
        reference_id VARCHAR(255) NOT NULL,
        created_by BIGINT NOT NULL,
        updated_by BIGINT NOT NULL,
        created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_confirmation_user_id FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
        CONSTRAINT fk_confirmation_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT
    );

    CREATE TABLE IF NOT EXISTS credentials (
        id SERIAL PRIMARY KEY,
        password VARCHAR(255) NOT NULL,
        reference_id VARCHAR(255),
        user_id BIGINT NOT NULL,
        created_by BIGINT NOT NULL,
        updated_by BIGINT NOT NULL,
        created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_credentials_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE CASCADE,
        CONSTRAINT fk_credentials_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT
    );

    CREATE TABLE IF NOT EXISTS documents (
        id SERIAL PRIMARY KEY,
        document_id BIGINT NOT NULL UNIQUE,
        extension VARCHAR(10),
        formatted_size VARCHAR(255),
        icon VARCHAR(255),
        size_type BIGINT NOT NULL,
        uri VARCHAR(255),
        description VARCHAR(255),
        reference_id VARCHAR(255),
        created_by BIGINT NOT NULL,
        updated_by BIGINT NOT NULL,
        created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_documents_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT,
        CONSTRAINT fk_documents_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT
    );

    CREATE TABLE IF NOT EXISTS roles (
        id SERIAL PRIMARY KEY,
        authority VARCHAR(255) NOT NULL,
        name VARCHAR(255),
        reference_id VARCHAR(255),
        created_by BIGINT NOT NULL,
        updated_by BIGINT NOT NULL,
        created_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        updated_at TIMESTAMP(6) WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
        CONSTRAINT fk_roles_created_by FOREIGN KEY (created_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT,
        CONSTRAINT fk_roles_updated_by FOREIGN KEY (updated_by) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT
    );

    CREATE TABLE IF NOT EXISTS user_roles (
        id SERIAL PRIMARY KEY,
        user_id BIGINT NOT NULL,
        role_id BIGINT NOT NULL,
        CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE CASCADE ON DELETE RESTRICT,
        CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id) ON UPDATE CASCADE ON DELETE RESTRICT
    );

    CREATE INDEX IF NOT EXISTS index_users_email ON users (email);
    CREATE INDEX IF NOT EXISTS index_users_user_id ON users (user_id);
    CREATE INDEX IF NOT EXISTS index_confirmation_user_id ON confirmation (user_id);
    CREATE INDEX IF NOT EXISTS index_credentials_user_id ON credentials (user_id);
    CREATE INDEX IF NOT EXISTS index_user_roles_user_id ON user_roles (user_id);

END;























