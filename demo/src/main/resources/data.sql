INSERT INTO my_user (id, username, password, email)
VALUES (1, 'user1', 'password1', 'user@user'),
         (2, 'user2', 'password2', 'user@user'),
         (3, 'user3', 'password3', 'user@user');

INSERT INTO seller (id, is_approved)
VALUES (1, false);

INSERT INTO buyer (id)
VALUES (1);

INSERT INTO product (id, name, price, description, seller_id, discount, is_best_seller, is_in_stock, is_new_arrival, purchased, rating, stock_quantity)
VALUES (1, 'product1', 100, 'description1', 1, 0, false, false, false, false, 0, 0),
         (2, 'product2', 200, 'description2', 1, 0, false, false, false, false, 0, 0),
         (3, 'product3', 300, 'description3', 1, 0, false, false, false, false, 0, 0);

INSERT INTO review (id, rating, content, buyer_id, product_id, is_flagged)
VALUES (1, 5, 'content1', 1, 1, false),
         (2, 4, 'content2', 1, 2, false),
         (3, 3, 'content3', 1, 3, true);