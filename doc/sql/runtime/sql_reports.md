# Reports

#### Amount of Distribution Items of a Film Copy at Date

```sql
SELECT COUNT(*)
FROM film_distribution_items items
         LEFT JOIN film_distribution distribution ON items.film_distribution_id = distribution.id
WHERE film_copy_id = :filmCopyId
  AND :date BETWEEN distribution.start_date AND distribution.end_date
```

#### List top N most distributed films

```sql
SELECT COUNT(*) AS filmAmount, film.name AS filmName
FROM film_distribution_items items
         JOIN film_copy copy
              on items.film_copy_id = copy.id
         JOIN film on copy.film_id = film.id
GROUP BY film_id, film.name
ORDER BY COUNT(*) DESC FETCH FIRST :amount ROWS ONLY;
```

#### List Revenue per Film

```sql
SELECT film.name                                                                                            AS filmName,
       CAST(SUM(film.price * CEIL((distribution.end_date - distribution.start_date) / 7)) AS DECIMAL(5, 2)) AS revenue
FROM film_distribution_items items
         JOIN film_distribution distribution ON distribution.id = items.film_distribution_id
         JOIN film_copy copy ON copy.id = items.film_copy_id
         JOIN film ON film.id = copy.film_id
GROUP BY film.name
ORDER BY revenue DESC;
```

#### Invoice for a Customer

```sql
SELECT film.name                                                                               AS filmName,
       film.price                                                                              AS pricePerWeek,
       distribution.start_date                                                                 AS startDate,
       distribution.end_date                                                                   AS endDate,
       CAST(SUM(film.price *
                ceil((distribution.end_date - distribution.start_date) / 7)) AS DECIMAL(5, 2)) AS priceTotal
FROM film_distribution_items items
         JOIN film_distribution distribution
              ON items.film_distribution_id = distribution.id AND distribution.id = :distributionId
         JOIN film_copy copy ON copy.ID = items.film_copy_id
         JOIN film ON film.ID = copy.film_id
GROUP BY film.NAME, film.price, distribution.start_date, distribution.end_date;
```

#### Distribution Report with Filter

```sql
SELECT film.name                                                                               AS filmName,
       COUNT(*)                                                                                AS amount,
       customer.id                                                                             AS customerId,
       film.price                                                                              AS pricePerWeek,
       distribution.start_date                                                                 AS startDate,
       distribution.end_date                                                                   AS endDate,
       CAST(SUM(film.price *
                CEIL((distribution.end_date - distribution.start_date) / 7)) AS DECIMAL(5, 2)) AS priceTotal
FROM film_distribution_items items
         JOIN film_distribution distribution ON items.film_distribution_id = distribution.id
         JOIN film_copy copy ON copy.id = items.film_copy_id
         JOIN film ON film.id = copy.film_id
         JOIN customer ON distribution.customer_id = customer.id
WHERE (:customerId IS NULL OR customer.id IN :customerId)
  AND (:filmId IS NULL OR film.id = :filmId)
  AND (:reportingDate IS NULL OR :reportingDate BETWEEN distribution.start_date AND distribution.end_date)
GROUP BY film.name, customer.id, film.price, distribution.start_date, distribution.end_date;
```