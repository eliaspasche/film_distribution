# Users

#### SELECT by ID

```sql
SELECT *
FROM application_user
WHERE id = :id;
```

#### Find All

```sql
SELECT *
FROM application_user;
```

#### Count

```sql
SELECT COUNT(*)
FROM application_user;
```

#### Create

```sql
INSERT INTO application_user (username, name, hashed_password)
VALUES (:username, :name, :hashed_password);
```

#### Update

```sql
UPDATE application_user
SET username        = :username,
    name            = :name,
    hashed_password = :hashed_password
WHERE id = :id;
```

#### DELETE by ID

```sql
DELETE
FROM application_user
WHERE id = :id;
```

