#!/bin/sh

docker run --name my-mongo -p 27017:27017 -d mongo

# docker start my-mongo