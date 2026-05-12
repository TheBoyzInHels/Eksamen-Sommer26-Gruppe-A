CREATE DATABASE "CarportDB"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    LOCALE_PROVIDER = 'libc'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

BEGIN;


CREATE TABLE IF NOT EXISTS public.users
(
    user_id bigserial,
    email character varying,
    password character varying,
    first_name character varying,
    last_name character varying,
    phone_number character varying,
    is_admin boolean DEFAULT false,
    PRIMARY KEY (user_id)
    );

CREATE TABLE public.carports
(
    carport_id bigserial NOT NULL,
    amount_of_cars bigint NOT NULL DEFAULT 1,
    carport_length character varying NOT NULL,
    carport_width bigint NOT NULL,
    has_shed boolean DEFAULT false,
    shed_length character varying,
    shed_width character varying,
    has_gutter boolean DEFAULT false,
    user_id bigint,
    PRIMARY KEY (carport_id),
    CONSTRAINT user_id FOREIGN KEY (user_id)
        REFERENCES public.users (user_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
);

ALTER TABLE IF EXISTS public.carports
    OWNER to postgres;

END;