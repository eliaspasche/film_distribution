# Film Distributions

#### SELECT by ID

```sql
SELECT *
FROM film_distribution
WHERE id = :id;
```

#### Find All

```sql
SELECT *
FROM film_distribution;
```

#### Find All

```sql
SELECT *
FROM film_distribution;
```

#### Find With Joined Data

```sql
SELECT *
FROM film_distribution
         JOIN customer ON film_distribution.customer_id = customer.id
         JOIN film_distribution_items ON film_distribution.id = film_distribution_items.film_distribution_id
         JOIN film_copy ON film_distribution_items.film_copy_id = film_copy.id
WHERE film_distribution.id = :id; -- Optional WHERE
```

#### Find All With Joined Data And Filters

```sql
SELECT *
FROM film_distribution
         JOIN customer ON film_distribution.customer_id = customer.id
         JOIN film_distribution_items ON film_distribution.id = film_distribution_items.film_distribution_id
         JOIN film_copy ON film_distribution_items.film_copy_id = film_copy.id
WHERE film_distribution.customer_id = :customer_id;


SELECT *
FROM film_distribution
         JOIN customer ON film_distribution.customer_id = customer.id
         JOIN film_distribution_items ON film_distribution.id = film_distribution_items.film_distribution_id
         JOIN film_copy ON film_distribution_items.film_copy_id = film_copy.id
WHERE film_copy.film_id = :film_id;


SELECT *
FROM film_distribution
         JOIN customer ON film_distribution.customer_id = customer.id
         JOIN film_distribution_items ON film_distribution.id = film_distribution_items.film_distribution_id
         JOIN film_copy ON film_distribution_items.film_copy_id = film_copy.id
WHERE :reporting_date BETWEEN film_distribution.start_date AND film_distribution.end_date;


SELECT *
FROM film_distribution
         JOIN customer ON film_distribution.customer_id = customer.id
         JOIN film_distribution_items ON film_distribution.id = film_distribution_items.film_distribution_id
         JOIN film_copy ON film_distribution_items.film_copy_id = film_copy.id
WHERE (:customer_id IS NULL OR film_distribution.customer_id = :customer_id)
  AND (:film_id IS NULL OR film_copy.film_id = :film_id)
  AND (:reporting_date IS NULL OR :reporting_date BETWEEN film_distribution.start_date AND film_distribution.end_date);


SELECT DISTINCT copy.id, copy.inventory_number, copy.film_id
FROM film_copy copy
         LEFT JOIN film_distribution_items items ON copy.id = items.film_copy_id
         LEFT JOIN film_distribution distribution ON items.film_distribution_id = distribution.id
WHERE distribution.start_date IS NULL
   OR distribution.end_date IS NULL
   OR ((:startDate NOT BETWEEN distribution.start_date AND distribution.end_date)
    AND (:endDate NOT BETWEEN distribution.start_date AND distribution.end_date))
    AND ((distribution.start_date NOT BETWEEN :startDate AND :endDate)
        AND (distribution.end_date NOT BETWEEN :startDate AND :endDate));
```

#### Count

```sql
SELECT COUNT(*)
FROM film_distribution;
```

#### Create

```sql
INSERT INTO film_distribution (customer_id, start_date, end_date)
VALUES (:customer_id, :start_date, :end_date);
```

#### Update

```sql
UPDATE film_distribution
SET customer_id = :customer_id,
    start_date  = :start_date,
    end_date    = :end_date
WHERE id = :id;
```

#### DELETE by ID

```sql
DELETE
FROM film_distribution
WHERE id = :id;
```
