BEGIN;

CREATE TABLE IF NOT EXISTS public.users
(
    user_id bigserial NOT NULL,
    email varchar NOT NULL UNIQUE,
    password character varying NOT NULL,
    first_name character varying NOT NULL,
    last_name character varying NOT NULL,
    phone_number character varying NOT NULL,
    is_admin boolean DEFAULT false,
    CONSTRAINT users_pkey PRIMARY KEY (user_id)
    );

CREATE TABLE IF NOT EXISTS public.carports
(
    carport_id bigserial NOT NULL,
    amount_of_cars bigint NOT NULL DEFAULT 1,
    length bigint NOT NULL,
    width bigint NOT NULL,
    has_shed boolean NOT NULL DEFAULT false,
    shed_width bigint,
    shed_length bigint,
    has_gutter boolean NOT NULL DEFAULT false,
    user_id bigint NOT NULL,

    CONSTRAINT carport_pkey PRIMARY KEY (carport_id),
    CONSTRAINT fk_carport_user
    FOREIGN KEY (user_id)
    REFERENCES public.users(user_id)
    );

CREATE TABLE IF NOT EXISTS public.inquiries
(
    inquiry_id bigserial NOT NULL,
    status character varying NOT NULL CHECK (status IN ('pending', 'offered', 'accepted', 'rejected')),
    user_id bigint NOT NULL,
    carport_id bigint NOT NULL,
    date date NOT NULL,
    price numeric,
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
    part_price numeric,
    PRIMARY KEY (part_id)
    );
END;