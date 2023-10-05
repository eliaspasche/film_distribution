# Age Groups

#### SELECT by ID

```sql
SELECT *
FROM age_group
WHERE id = :id;
```

#### Find All

```sql
SELECT *
FROM age_group;
```

#### Find With Joined Data

```sql
SELECT *
FROM age_group
         JOIN film ON age_group.id = film.age_group_id
WHERE age_group.id = :id; -- Optional WHERE
```

#### Count

```sql
SELECT COUNT(*)
FROM age_group;
```

#### Create

```sql
INSERT INTO age_group (name, minimum_age)
VALUES (:name, :minimum_age);
```

#### Update

```sql
UPDATE age_group
SET name        = :name,
    minimum_age = :minimum_age
WHERE id = :id;
```

#### DELETE by ID

```sql
DELETE
FROM age_group
WHERE id = :id;
```