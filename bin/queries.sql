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

CREATE TABLE HISTORY (
   USER_ID        NUMBER NOT NULL,
   PRODUCT_ID     NUMBER NOT NULL,
   PURCHASE_DATE  DATE  DEFAULT SYSDATE
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
DESC SUGGESTIONS;

CREATE INDEX username_idx
ON USERS (USERNAME);

CREATE OR REPLACE view HOME_VIEW as
SELECT PRODUCT_ID, PRODUCT_NAME, IMAGE_URL, PRICE, DISCOUNT, CATEGORY FROM PRODUCTS WHERE RATE > 4.8;

CREATE view frame_view as
SELECT PRODUCT_ID, PRODUCT_NAME, IMAGE_URL, PRICE, DISCOUNT, CATEGORY FROM PRODUCTS;


CREATE OR REPLACE VIEW TEMP_VIEW AS
SELECT PRODUCT_ID, PRODUCT_NAME, PRICE, RATE, DISCOUNT, BRAND, IMAGE_URL FROM PRODUCTS 
WHERE CATEGORY = 'Automotive';

SELECT * FROM TEMP_VIEW;

SELECT * FROM USERS;

CREATE OR REPLACE TYPE num_arr AS TABLE OF NUMBER;
/

ALTER TABLE SUGGESTIONS
ADD IS_SEEN NUMBER(1) DEFAULT 0;

ALTER TABLE USERS
DROP COLUMN NEW_SUGGESTIONS;


COMMIT;

ROLLBACK;