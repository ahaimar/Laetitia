
BEGIN:

    INSERT INTO users (
            user_id,
            first_name,
            last_name,
            email,
            phone,
            bio,
            reference_id,
            qr_code_image_uri,
            image_url,
            created_by,
            updated_by
        ) VALUES (
            'user_123',
            'John',
            'Doe',
            'john.doe@example.com',
            '1234567890',
            'This is a bio',
            'ref_001',
            NULL,
            NULL,
            1,  -- Assuming the admin user with ID 1 created this record
            1   -- Assuming the admin user with ID 1 updated this record
    );

    INSERT INTO roles (
        authority,
        name,
        reference_id,
        created_by,
        updated_by
    ) VALUES (
        'ROLE_ADMIN',
        'Administrator',
        'ref_role_001',
        1,  -- Assuming an admin user with ID 1 created this role
        1   -- Assuming the same admin user updated this role
    );

    INSERT INTO credentials (
        password,
        reference_id,
        user_id,
        created_by,
        updated_by
    ) VALUES (
        'hashed_password_here',  -- Replace with a properly hashed password
        'ref_cred_001',
        1,  -- Assuming this credential belongs to the user with ID 1
        1,  -- Assuming the admin user with ID 1 created this record
        1   -- Assuming the admin user with ID 1 updated this record
    );

    INSERT INTO confirmation (
        key,
        user_id,
        reference_id,
        create_at,
        update_id
    ) VALUES (
        'random_generated_key',  -- Replace with a securely generated key
        1,  -- Assuming this confirmation is for the user with ID 1
        'ref_conf_001',
        CURRENT_TIMESTAMP,
        1  -- Assuming the admin user with ID 1 updated this record
    );
END;
