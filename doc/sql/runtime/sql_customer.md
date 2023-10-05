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
