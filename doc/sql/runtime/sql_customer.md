# Customers

#### SELECT by ID

```sql
SELECT *
FROM customer
WHERE id = :id;
```

#### Find All

```sql
SELECT *
FROM customer;
```

#### Find All With Filters

```sql
SELECT *
FROM customer
WHERE name LIKE '%' || :name || '%';


SELECT *
FROM customer
WHERE first_name LIKE '%' || :first_name || '%';


SELECT *
FROM customer
WHERE address LIKE '%' || :address || '%';


SELECT *
FROM customer
WHERE zip_code LIKE '%' || :zip_code || '%';


SELECT *
FROM customer
WHERE city LIKE '%' || :city || '%';


SELECT *
FROM customer
WHERE name LIKE '%' || :name || '%'
  AND first_name LIKE '%' || :first_name || '%'
  AND address LIKE '%' || :address || '%'
  AND zip_code LIKE '%' || :zip_code || '%'
  AND city LIKE '%' || :city || '%';
```

#### Count

```sql
SELECT COUNT(*)
FROM customer;
```

#### Create

```sql
INSERT INTO customer (name, first_name, date_of_birth, zip_code, city, address)
VALUES (:name, :first_name, :date_of_birth, :zip_code, :city, :address);
```

#### Update

```sql
UPDATE customer
SET name          = :name,
    first_name    = :first_name,
    date_of_birth = :date_of_birth,
    zip_code      = :zip_code,
    city          = :city,
    address       = :address
WHERE id = : id;
```

#### DELETE by ID

```sql
DELETE
FROM customer
WHERE id = :id;
```
