-- Database Creation
CREATE SEQUENCE prod_seq
START WITH 10 INCREMENT BY 1
NOCACHE;

CREATE SEQUENCE users_seq
START WITH 100 INCREMENT BY 1
NOCACHE;

CREATE TABLE Products (
   PRODUCT_ID       NUMBER          PRIMARY KEY,
   NAME             VARCHAR2(30)    NOT NULL,
   PRICE            NUMBER(10, 4)   NOT NULL,
   RATE             NUMBER(1)       DEFAULT 3,
   DISCOUNT         NUMBER(3),
   --Image(s)
   CATEGORY_ID      NUMBER(10),
   COMPANY          VARCHAR2(30),
   QUANTITY         NUMBER(5)       NOT NULL,
   DESCRIPTION      VARCHAR2(1000),
);

CREATE TABLE Users (
   USER_ID          NUMBER          PRIMARY KEY,
   USERNAME         VARCHAR2(30)    NOT NULL,
   PASSWORD         VARCHAR2(100)   UNIQUE NOT NULL,
   NEW_SUGGESTIONS  NUMBER(3)       DEFAULT 0
);

CREATE TABLE Stores (
   PRODUCT_ID   NUMBER,
   QUANTITY     NUMBER  NOT NULL,
   ARRIVAL_DATE DATE    DEFAULT SYSDATE,

   CONSTRAINT fk_product2 FOREIGN KEY(PRODUCT_ID) REFERENCES Products(PRODUCT_ID)
);

CREATE TABLE Suggestions (
   SENDER_ID    NUMBER NOT NULL,
   RECIEVER_ID  NUMBER NOT NULL,
   PRODUCT_ID   NUMBER NOT NULL,

   CONSTRAINT fk_sender FOREIGN KEY(SENDER_ID)      REFERENCES Users(USER_ID),
   CONSTRAINT fk_reciever FOREIGN KEY(RECIEVER_ID)  REFERENCES Users(USER_ID),
   CONSTRAINT fk_product FOREIGN KEY(PRODUCT_ID)    REFERENCES Products(PRODUCT_ID)
);

-- Create triggers
CREATE OR REPLACE TRIGGER trg_prod_id
BEFORE INSERT ON Products
FOR EACH ROW
BEGIN
   IF :NEW.PRODUCT_ID IS NULL THEN
      SELECT prod_seq.NEXTVAL INTO :NEW.PRODUCT_ID FROM dual;
   END IF;
END;

CREATE OR REPLACE TRIGGER trg_users_id
BEFORE INSERT ON USERS
FOR EACH ROW
BEGIN
   IF :NEW.USER_ID IS NULL THEN
      SELECT users_seq.NEXTVAL INTO :NEW.USER_ID FROM dual;
   END IF;
END;

-----------------------------------------------------------------------------------------------------------
select * from Products;

DROP TABLE CATEGORIES CASCADE CONSTRAINTS;
DROP TRIGGER trg_category_id;
DROP SEQUENCE CAT_SEQ;

ALTER TABLE PRODUCTS
ADD IMAGE_URL VARCHAR2(1000)

UPDATE USERS
SET BALANCE = 0;

CREATE INDEX username_idx
ON USERS (USERNAME);

DROP INDEX idx_category;
CREATE INDEX idx_category ON PRODUCTS(CATEGORY);

create view categories_view as 
SELECT DISTINCT CATEGORY FROM PRODUCTS;

create view home_view as 
SELECT * FROM PRODUCTS where RATE > 4.8;

SELECT * FROM PRODUCTS WHERE PRODUCT_ID = 601 OR PRODUCT_ID = 975;

DROP VIEW CATEGORIES_VIEW;
COMMIT;

ROLLBACK;