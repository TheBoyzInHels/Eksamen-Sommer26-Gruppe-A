CREATE DATABASE "CarportDB"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'en_US.utf8'
    LC_CTYPE = 'en_US.utf8'
    LOCALE_PROVIDER = 'libc'
    TABLESPACE = pg_default
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

BEGIN;

CREATE TABLE IF NOT EXISTS public.carports
(
    carport_id bigserial NOT NULL,
    amount_of_cars bigint NOT NULL DEFAULT 1,
    carport_length bigint NOT NULL,
    carport_width bigint NOT NULL,
    has_shed boolean DEFAULT false,
    shed_length bigint DEFAULT 0,
    shed_width bigint DEFAULT 0,
    has_gutter boolean DEFAULT false,
    user_id bigint NOT NULL,
    CONSTRAINT carports_pkey PRIMARY KEY (carport_id)
    );
CREATE TABLE IF NOT EXISTS public.inquiries
(
    inquiry_id bigserial NOT NULL,
    status varchar(20) NOT NULL CHECK (status IN ('Venter', 'Godkendt', 'Afvist', 'Betalt')),
    user_id bigint NOT NULL,
    carport_id bigint NOT NULL,
    date date NOT NULL,
    price numeric,
    CONSTRAINT inquiries_pkey PRIMARY KEY (inquiry_id)
    );

CREATE TABLE IF NOT EXISTS public.parts
(
    part_id bigserial NOT NULL,
    part_name character varying COLLATE pg_catalog."default" NOT NULL,
    part_description character varying COLLATE pg_catalog."default" NOT NULL,
    part_price numeric(10,2) NOT NULL,
    part_length integer NOT NULL,
    CONSTRAINT parts_pkey PRIMARY KEY (part_id)
    );
CREATE TABLE IF NOT EXISTS public.users
(
    user_id bigserial NOT NULL,
    email character varying COLLATE pg_catalog."default" NOT NULL,
    password character varying COLLATE pg_catalog."default" NOT NULL,
    first_name character varying COLLATE pg_catalog."default" NOT NULL,
    last_name character varying COLLATE pg_catalog."default" NOT NULL,
    phone_number character varying COLLATE pg_catalog."default" NOT NULL,
    is_admin boolean NOT NULL DEFAULT false,
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
    );

ALTER TABLE IF EXISTS public.carports
    ADD CONSTRAINT carport_fk_user_id FOREIGN KEY (user_id)
    REFERENCES public.users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.inquiries
    ADD CONSTRAINT inquiries_fk_carport_id FOREIGN KEY (carport_id)
    REFERENCES public.carports (carport_id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;


ALTER TABLE IF EXISTS public.inquiries
    ADD CONSTRAINT inquiries_fk_user_id FOREIGN KEY (user_id)
    REFERENCES public.users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION;

INSERT INTO parts (part_name, part_description, part_price, part_length) VALUES ('Spær','45X195MM',479.70, 600);
INSERT INTO parts (part_name, part_description, part_price, part_length) VALUES ('Remme','45X195MM',479.70, 600);
INSERT INTO parts (part_name, part_description, part_price, part_length) VALUES ('Remme','45X195MM',254.15, 480);
INSERT INTO parts (part_name, part_description, part_price, part_length) VALUES ('Stolpe','97X97 MM',221.85, 300);
INSERT INTO parts (part_name, part_description, part_price, part_length) VALUES ('Tagrende','ALUZINK 11 ALUZINK',649.95, 600);
INSERT INTO parts (part_name, part_description, part_price, part_length) VALUES ('Tagrende','PLASTMO TAGRENDE GRAFIT',649.00, 300);

ALTER TABLE IF EXISTS public.carports
    ADD COLUMN can_edit boolean DEFAULT true;

ALTER TABLE IF EXISTS public.carports
    ADD COLUMN customer_notes character varying DEFAULT 0;

END;