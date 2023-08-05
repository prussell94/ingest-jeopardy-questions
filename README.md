# Ingest-jeopardy

This microservice grabs the data from cluebase and puts it into a postgres database.
https://cluebase.readthedocs.io/en/latest/

Categories, clues, contestants, games and seasons are all retrieved.

To start postgres go into the src/test/resources/ package and run the docker-compose file:

```
docker-compose -f docker-compose-postgres.yml up -d
```

