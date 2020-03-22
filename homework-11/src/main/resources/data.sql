insert
into book (id,
                  title)
values ('b926dbdd-ae2f-4a0b-8ad7-64f9c28afe9d',
        'book-one'),
       ('3c074773-0868-457e-8341-54987389ba6b',
        'book-two');

insert
into author (id,
                    "name")
values ('2706f01e-177c-4362-85ce-a84c99df99ab',
        'author-one'),
       ('078c51df-e9ea-45cb-b51d-8d298ab9b5b6',
        'author-two'),
       ('4310b23a-8e19-4fc9-b129-922d4f6c3284',
        'author-three');

insert
into genre (id,
                   "name")
values ('5571c3da-d92a-4a4c-9126-efd5fb71ca48',
        'genre-one'),
       ('08c4ed28-d902-4ce8-ad98-f7f3b79c3df1',
        'genre-two');

INSERT INTO book_author
  (book_id, author_id)
VALUES ('b926dbdd-ae2f-4a0b-8ad7-64f9c28afe9d', '2706f01e-177c-4362-85ce-a84c99df99ab')
     , ('b926dbdd-ae2f-4a0b-8ad7-64f9c28afe9d', '078c51df-e9ea-45cb-b51d-8d298ab9b5b6')
     , ('3c074773-0868-457e-8341-54987389ba6b', '078c51df-e9ea-45cb-b51d-8d298ab9b5b6');

INSERT INTO book_genre
  (book_id, genre_id)
values ('b926dbdd-ae2f-4a0b-8ad7-64f9c28afe9d', '5571c3da-d92a-4a4c-9126-efd5fb71ca48')
     , ('3c074773-0868-457e-8341-54987389ba6b', '5571c3da-d92a-4a4c-9126-efd5fb71ca48')
     , ('3c074773-0868-457e-8341-54987389ba6b', '08c4ed28-d902-4ce8-ad98-f7f3b79c3df1');

