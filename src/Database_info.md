Database info
   Username(Schema): MARKET
   Password: marketdb
   Port: 1521
   Host: LOCALHOST
   Connect as: SYSDBA
   Service Name: ORCLPDB
   Connect using Oracle client: OraDB21Home1


Tables:
Products
   ID (hidden except for admin) (PK)
   Name
   Price
   Rate
   Discount (if exists)
   Image(s)
   Category ID (FK)
   Company responsible for the product
   Available number of product (hidden except for admin and if <= 10 products available)
   Description or specifications or Highlights 

Category
   ID
   NAME
   Number of Items
   Image

Users
   ID
   Username
   Password (encrypted or hashed)
   NEW_SUGGESTIONS

Stores
   Item_ID
   quantity
   Arrival_Date

Suggestion
   SENDER_ID
   RECIEVER_ID
   ITEM_ID