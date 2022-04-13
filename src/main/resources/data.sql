#INSERT INTO oauth_client_details (client_id, client_secret, web_server_redirect_uri, scope, access_token_validity, refresh_token_validity, resource_ids, authorized_grant_types, additional_information) VALUES ('mobile', '{noop}123', 'http://localhost:8080/login', 'READ,WRITE,DOCUMENT,EMAIL', '3600', '10000', 'u_437287,d_498778,e_547634,a_548734', 'authorization_code,password,refresh_token,implicit', '{}');

INSERT INTO role (name)
VALUES ('ROLE_ADMIN'),
       ('ROLE_USER');

INSERT INTO auth_user (id, full_name, email, password, enabled, account_non_expired, credentials_non_expired,
                       account_non_locked)
VALUES (1, 'Hamza', 'hamza@gmail.com', '{noop}123', 1, 1, 1, 1);
INSERT INTO auth_user (id, full_name, email, password, enabled, account_non_expired, credentials_non_expired,
                       account_non_locked)
VALUES (2, 'Ahmad', 'ahmad@gmail.com', '{noop}123', 1, 1, 1, 1);

INSERT INTO role_user (ROLE_ID, USER_ID)
VALUES (1, 1) /* hamza-admin */,
       (2, 2) /* ahmad-authUser */;

INSERT INTO category(name)
    VALUE ('For Children'),
    ('For Teens'),
    ('9/11 and Daffodils'),
    ('Acts of Kindness');