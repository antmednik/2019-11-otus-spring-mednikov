#!/bin/sh

docker run --name my-pg -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres:12.1-alpine

# docker start my-pg