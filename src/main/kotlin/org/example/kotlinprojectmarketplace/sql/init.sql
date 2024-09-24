CREATE TABLE UserDetails(
  id serial PRIMARY KEY,
  login varchar(30) NOT NULL UNIQUE,
  password varchar(30) NOT NULL,
  seller boolean DEFAULT false,
  admin boolean DEFAULT false
);

CREATE TABLE "User"(
  id int PRIMARY KEY,
  email varchar(60) UNIQUE,
  phone varchar(30) UNIQUE,
  cash int NOT NULL DEFAULT 0,
  name varchar(30) NOT NULL,
  last_name varchar(30) NOT NULL,
  FOREIGN KEY(id) REFERENCES UserDetails(id) 
    ON DELETE CASCADE 
    ON UPDATE CASCADE
);

CREATE TABLE Product(
  id serial PRIMARY KEY,
  seller_id int NOT NULL,
  name varchar(60) NOT NULL,
  amount int NOT NULL DEFAULT 0,
  price int NOT NULL,
  UNIQUE(seller_id, name),
  FOREIGN KEY(seller_id) REFERENCES "User"(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE CartList(
  buyer_id int NOT NULL,
  product_id int NOT NULL,
  FOREIGN KEY(buyer_id) REFERENCES "User"(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY(product_id) REFERENCES Product(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE "Order"(
  id serial PRIMARY KEY,
  buyer_id int NOT NULL,
  FOREIGN KEY(buyer_id) REFERENCES "User"(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

CREATE TABLE OrderList(
  order_id int NOT NULL,
  product_id int NOT NULL,
  FOREIGN KEY(order_id) REFERENCES "Order"(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  FOREIGN KEY(product_id) REFERENCES Product(id)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);

ALTER TABLE Product
    RENAME name TO title;