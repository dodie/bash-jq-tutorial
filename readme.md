# How to Bash and jq: generate statistics for a REST API

This repository contains a dummy blog engine that publishes the following REST endpoints:

- `http://localhost:8080/posts?pageNumber=<num>`: List metadata for all posts in a paginated list: ID, title and tags.
- `http://localhost:8080/posts/<post_id>`: Get all details regarding a post, including its full text content.

The [calculate-bash-stats.sh](https://github.com/dodie/bash-jq-tutorial/blob/master/calculate-bash-stats.sh)
script scrapes data from these endpoints to calculate the percentage of bash related content.


# Usage

- Start blog engine: `mvn spring-boot:run`
- Run script: `./calculate-bash-stats.sh`

