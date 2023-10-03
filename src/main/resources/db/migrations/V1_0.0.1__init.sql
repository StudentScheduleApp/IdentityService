-- Table: public.app_tokens

-- DROP TABLE IF EXISTS public.app_tokens;

CREATE TABLE IF NOT EXISTS public.app_tokens
(
    id bigint NOT NULL,
    app_name character varying(255) COLLATE pg_catalog."default" NOT NULL,
    app_token character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT app_tokens_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.app_tokens
    OWNER to postgres;