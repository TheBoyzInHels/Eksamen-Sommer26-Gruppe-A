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

CREATE TABLE public.carport
(
    carport_id bigserial NOT NULL,
    amount_of_cars bigint NOT NULL DEFAULT 1,
    length character varying NOT NULL,
    width character varying NOT NULL,
    has_shed boolean,
    shed_width character varying,
    shed_length character varying,
    has_gutter boolean,
    PRIMARY KEY (carport_id)
);
ALTER TABLE IF EXISTS public.carport
    ADD CONSTRAINT user_id FOREIGN KEY (user_id)
    REFERENCES public.users (user_id) MATCH SIMPLE
    ON UPDATE NO ACTION
       ON DELETE NO ACTION
    NOT VALID;

ALTER TABLE IF EXISTS public.carport
    OWNER to postgres;
END;