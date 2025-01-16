INSERT INTO users (
    id,
    user_id,
    first_name,
    last_name,
    mail,
    login_attempts,
    last_login,
    phone,
    bio,
    image_url,
    account_not_expired,
    account_locked,
    enabled,
    mfa,
    qr_code_secret,
    qr_image_url,
    role_id
) VALUES (
    0,
    'user123',
    'John',
    'Doe',
    'john.doe@example.com',
    0,
    '2024-12-31 10:00:00',
    NULL,  -- phone (nullable)
    NULL,  -- bio (nullable)
    NULL,  -- imageUrl (nullable)
    true,
    false,
    true,
    false,
    'secret123',
    'http://example.com/qrimage.jpg',
    USER
);
