INSERT INTO region(region_name)
VALUES ('Москва и Московская область'),
       ('Санкт-Петербург и Ленинградская область'),
       ('Севастопль и Крымская область');

INSERT INTO tariff(tariff_code, description, base_cost, region_id)
VALUES (6, '', 100.0, 1),
       (11, '', 0.0, 1),
       (3, '', 0.0, 1);

INSERT INTO tariff_call_option(call_type, minute_cost, minute_buffer, option_price, call_priority, tariff_code)
VALUES (1, 1.5, 0, 0, 0, 6),
       (2, 1.5, 0, 0, 0, 6),
       (1, 0.5, 300, 0, 1, 6),
       (2, 0, 300, 0, 1, 6);

INSERT INTO abonent(phone_number, balance, tariff_id)
VALUES (79146878167, 100.0, 6);