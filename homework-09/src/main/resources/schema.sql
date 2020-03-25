drop table if exists book_author;
drop table if exists book_genre;
drop table if exists book;
drop table if exists comment;
drop table if exists author;
drop table if exists genre;

create table book
(
  id    uuid primary key,
  title text not null
);

create table comment
(
  id uuid primary key,
  text text not null,
  book_id uuid not null references book(id)
);

create table author
(
  id   uuid primary key,
  name text not null
);

create table genre
(
  id   uuid primary key,
  name text not null
);

create table book_author
(
  book_id   uuid not null references book (id),
  author_id uuid not null references author (id),
  primary key (book_id, author_id)
);

create table book_genre
(
  book_id  uuid not null references book (id),
  genre_id uuid not null references genre (id),
  primary key (book_id, genre_id)
);