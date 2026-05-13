BEGIN;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id bigserial NOT NULL,
    email character varying COLLATE pg_catalog."default",
    password character varying COLLATE pg_catalog."default",
    first_name character varying COLLATE pg_catalog."default",
    last_name character varying COLLATE pg_catalog."default",
    phone_number character varying COLLATE pg_catalog."default",
    is_admin boolean DEFAULT false,
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
    );

CREATE TABLE IF NOT EXISTS public.carports
(
    carport_id bigserial NOT NULL,
    amount_of_cars bigint NOT NULL DEFAULT 1,
    length bigint NOT NULL,
    width bigint NOT NULL,
    has_shed boolean,
    shed_width bigint,
    shed_length bigint,
    has_gutter boolean,
    user_id bigint NOT NULL,
    CONSTRAINT carport_pkey PRIMARY KEY (carport_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
    );

CREATE TABLE IF NOT EXISTS public.inquiries
(
    inquiry_id bigserial NOT NULL,
    status character varying NOT NULL,
    user_id bigint NOT NULL,
    carport_id bigint NOT NULL,
    date date NOT NULL,
    price bigint,
    PRIMARY KEY (inquiry_id),

    CONSTRAINT fk_inquiry_user
    FOREIGN KEY (user_id)
    REFERENCES public.users (user_id),

    CONSTRAINT fk_inquiry_carport
    FOREIGN KEY (carport_id)
    REFERENCES public.carports (carport_id)
    );

CREATE TABLE IF NOT EXISTS public.part
(
    part_id bigserial NOT NULL,
    part_name character varying NOT NULL,
    part_price bigint
);


END;