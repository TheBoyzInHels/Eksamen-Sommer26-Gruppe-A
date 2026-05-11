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
END;