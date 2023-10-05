# Films

#### SELECT by ID

```sql
SELECT *
FROM film
WHERE id = :id;
```

#### Find All

```sql
SELECT *
FROM film;
```

#### Find With Joined Data

```sql
SELECT *
FROM film
         JOIN age_group ON film.age_group_id = age_group.id
         JOIN film_copy ON film.id = film_copy.film_id
WHERE film.id = :id; -- Optional WHERE
```

#### Find With Joined Data and Filters

```sql
SELECT *
FROM film
         JOIN age_group ON film.age_group_id = age_group.id
         JOIN film_copy ON film.id = film_copy.film_id
WHERE film.name LIKE '%' || :name || '%';
```

#### Amount of Available Copies

```sql
SELECT COUNT(*)
FROM film_copy
WHERE film_id = :film_id
```

#### Count

```sql
SELECT COUNT(*)
FROM film;
```

#### Create

```sql
INSERT INTO film (name, length, age_group_id, price)
VALUES (:name, :length, :age_group_id, :price);
```

#### Update

```sql
UPDATE film
SET name         = :name,
    length       = :length,
    age_group_id = :age_group_id,
    price        = :price
WHERE id = : id;
```

#### DELETE by ID

```sql
DELETE
FROM film
WHERE id = :id;
```

