# Film Copies

#### SELECT by ID

```sql
SELECT *
FROM film_copy
WHERE id = :id;
```

#### Find All

```sql
SELECT *
FROM film_copy;
```

#### Find With Joined Data

```sql
SELECT *
FROM film_copy
         JOIN film ON film_copy.film_id = film.id
         JOIN film_distribution_items ON film_copy.id = film_distribution_items.film_copy_id
         JOIN film_distribution ON film_distribution_items.film_distribution_id = film_distribution.id
WHERE film_copy.id = :id; -- Optional WHERE
```

#### Find With Joined Data And Filters

```sql
SELECT *
FROM film_copy
         JOIN film ON film_copy.film_id = film.id
         JOIN film_distribution_items ON film_copy.id = film_distribution_items.film_copy_id
         JOIN film_distribution ON film_distribution_items.film_distribution_id = film_distribution.id
WHERE film.name LIKE '%' || :name || '%';
```

#### Available Film Copies

```sql
SELECT *
FROM film_copy
WHERE id NOT IN (SELECT film_copy_id FROM film_distribution_items);
```

#### Count

```sql
SELECT COUNT(*)
FROM film_copy;
```

#### Create

```sql
INSERT INTO film_copy (inventory_number, film_id)
VALUES (:inventory_number, :film_id);
```

#### Update

```sql
UPDATE film_copy
SET inventory_number = :inventory_number,
    film_id          = :film_id
WHERE id = :id;
```

#### DELETE by ID

```sql
DELETE
FROM film_copy
WHERE id = :id;
```
