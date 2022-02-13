CREATE DATABASE expenses_management;

CREATE TABLE category
(
    id SERIAL,
    name character varying,
    PRIMARY KEY (id)
);


ALTER TABLE IF EXISTS public.category
    OWNER to postgres;

CREATE TABLE public.expense
(
    id SERIAL,
    name character varying,
    created_at date,
    category integer,
    amount numeric,
    PRIMARY KEY (id));

ALTER TABLE IF EXISTS public.expense
    OWNER to postgres;

alter table public.expense 
add constraint FK_expense_category
foreign key (category)
references public.category (id); 