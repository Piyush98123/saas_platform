-- Seed data for SaaS Platform

-- Insert demo company
INSERT INTO companies (tenant_id, name, domain, subdomain, email, phone, address, city, state, zip_code, country, status, plan, created_by) VALUES
('demo', 'Demo Service Company', 'demoservice.com', 'demo', 'admin@demoservice.com', '+1-555-0123', '123 Business St', 'Business City', 'BC', '12345', 'USA', 'ACTIVE', 'PROFESSIONAL', 'system');

-- Insert super admin company (platform owner)
INSERT INTO companies (tenant_id, name, domain, subdomain, email, phone, address, city, state, zip_code, country, status, plan, created_by) VALUES
('platform', 'SaaS Platform', 'saasplatform.com', 'platform', 'admin@saasplatform.com', '+1-555-0000', '1 Platform Way', 'Platform City', 'PC', '00000', 'USA', 'ACTIVE', 'ENTERPRISE', 'system');

-- Get company IDs
DO $$
DECLARE
    demo_company_id BIGINT;
    platform_company_id BIGINT;
BEGIN
    SELECT id INTO demo_company_id FROM companies WHERE tenant_id = 'demo';
    SELECT id INTO platform_company_id FROM companies WHERE tenant_id = 'platform';
    
    -- Insert super admin user
    INSERT INTO users (tenant_id, email, password, first_name, last_name, status, email_verified, company_id, created_by) VALUES
    ('platform', 'superadmin@saasplatform.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Super', 'Admin', 'ACTIVE', true, platform_company_id, 'system');
    
    -- Insert demo company admin
    INSERT INTO users (tenant_id, email, password, first_name, last_name, status, email_verified, company_id, created_by) VALUES
    ('demo', 'admin@demoservice.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Demo', 'Admin', 'ACTIVE', true, demo_company_id, 'system');
    
    -- Insert demo staff member
    INSERT INTO users (tenant_id, email, password, first_name, last_name, status, email_verified, company_id, created_by) VALUES
    ('demo', 'staff@demoservice.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Demo', 'Staff', 'ACTIVE', true, demo_company_id, 'system');
    
    -- Get user IDs
    DECLARE
        super_admin_id BIGINT;
        demo_admin_id BIGINT;
        demo_staff_id BIGINT;
        super_admin_role_id BIGINT;
        company_admin_role_id BIGINT;
        staff_role_id BIGINT;
    BEGIN
        SELECT id INTO super_admin_id FROM users WHERE email = 'superadmin@saasplatform.com';
        SELECT id INTO demo_admin_id FROM users WHERE email = 'admin@demoservice.com';
        SELECT id INTO demo_staff_id FROM users WHERE email = 'staff@demoservice.com';
        
        SELECT id INTO super_admin_role_id FROM roles WHERE name = 'SUPER_ADMIN';
        SELECT id INTO company_admin_role_id FROM roles WHERE name = 'COMPANY_ADMIN';
        SELECT id INTO staff_role_id FROM roles WHERE name = 'STAFF';
        
        -- Assign roles
        INSERT INTO user_roles (user_id, role_id) VALUES
        (super_admin_id, super_admin_role_id),
        (demo_admin_id, company_admin_role_id),
        (demo_staff_id, staff_role_id);
    END;
    
    -- Insert demo customers
    INSERT INTO customers (tenant_id, first_name, last_name, email, phone, address, city, state, zip_code, country, company_id, created_by) VALUES
    ('demo', 'John', 'Doe', 'john.doe@example.com', '+1-555-0101', '456 Customer Ave', 'Customer City', 'CC', '54321', 'USA', demo_company_id, 'system'),
    ('demo', 'Jane', 'Smith', 'jane.smith@example.com', '+1-555-0102', '789 Client Blvd', 'Client City', 'CLC', '67890', 'USA', demo_company_id, 'system');
    
    -- Insert demo leads
    INSERT INTO leads (tenant_id, title, description, customer_name, customer_email, customer_phone, status, priority, estimated_value, source, company_id, created_by) VALUES
    ('demo', 'Car Service Request', 'Need oil change and brake inspection', 'John Doe', 'john.doe@example.com', '+1-555-0101', 'NEW', 'MEDIUM', 150.00, 'Website', demo_company_id, 'system'),
    ('demo', 'House Cleaning Quote', 'Regular house cleaning service', 'Jane Smith', 'jane.smith@example.com', '+1-555-0102', 'CONTACTED', 'HIGH', 200.00, 'Phone', demo_company_id, 'system');
    
    -- Insert demo quotes
    INSERT INTO quotes (tenant_id, quote_number, title, description, customer_id, subtotal, tax_rate, tax_amount, total, status, company_id, created_by) VALUES
    ('demo', 'Q-001', 'Car Service Package', 'Oil change, brake inspection, and tire rotation', 
     (SELECT id FROM customers WHERE email = 'john.doe@example.com' LIMIT 1), 150.00, 0.08, 12.00, 162.00, 'SENT', demo_company_id, 'system'),
    ('demo', 'Q-002', 'House Cleaning Service', 'Regular house cleaning with deep cleaning options', 
     (SELECT id FROM customers WHERE email = 'jane.smith@example.com' LIMIT 1), 200.00, 0.08, 16.00, 216.00, 'DRAFT', demo_company_id, 'system');
    
    -- Insert demo quote items
    INSERT INTO quote_items (tenant_id, description, quantity, unit_price, total, quote_id, company_id, created_by) VALUES
    ('demo', 'Oil Change', 1, 35.00, 35.00, (SELECT id FROM quotes WHERE quote_number = 'Q-001' LIMIT 1), demo_company_id, 'system'),
    ('demo', 'Brake Inspection', 1, 50.00, 50.00, (SELECT id FROM quotes WHERE quote_number = 'Q-001' LIMIT 1), demo_company_id, 'system'),
    ('demo', 'Tire Rotation', 1, 25.00, 25.00, (SELECT id FROM quotes WHERE quote_number = 'Q-001' LIMIT 1), demo_company_id, 'system'),
    ('demo', 'Labor', 1, 40.00, 40.00, (SELECT id FROM quotes WHERE quote_number = 'Q-001' LIMIT 1), demo_company_id, 'system'),
    ('demo', 'House Cleaning - Basic', 1, 150.00, 150.00, (SELECT id FROM quotes WHERE quote_number = 'Q-002' LIMIT 1), demo_company_id, 'system'),
    ('demo', 'Deep Cleaning Add-on', 1, 50.00, 50.00, (SELECT id FROM quotes WHERE quote_number = 'Q-002' LIMIT 1), demo_company_id, 'system');
    
    -- Insert demo bookings
    INSERT INTO bookings (tenant_id, booking_number, title, description, customer_id, scheduled_date, estimated_duration_minutes, status, total_amount, company_id, created_by) VALUES
    ('demo', 'B-001', 'Car Service Appointment', 'Oil change and brake inspection', 
     (SELECT id FROM customers WHERE email = 'john.doe@example.com' LIMIT 1), 
     CURRENT_TIMESTAMP + INTERVAL '2 days', 120, 'CONFIRMED', 162.00, demo_company_id, 'system'),
    ('demo', 'B-002', 'House Cleaning Service', 'Regular house cleaning', 
     (SELECT id FROM customers WHERE email = 'jane.smith@example.com' LIMIT 1), 
     CURRENT_TIMESTAMP + INTERVAL '3 days', 180, 'SCHEDULED', 216.00, demo_company_id, 'system');
END $$;

