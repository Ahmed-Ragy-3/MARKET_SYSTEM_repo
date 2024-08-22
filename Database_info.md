Database info
   Username(Schema): MARKET
   Password: marketdb
   Port: 1521
   Host: LOCALHOST
   Connect as: SYSDBA
   Service Name: ORCL
   Connect using Oracle client: OraDB21Home1


Tables:
Products
   ID (hidden except for admin) (PK)
   Name
   Price
   Rate
   Discount (if exists)
   Images
   Category ID (FK)
   Company responsible for the product
   Available number of product (hidden except for admin and if <= 10 products available)
   Description or specifications or Highlights 

Category
   ID
   NAME
   Number of Items

Users
   ID
   Username
   Password (encrypted or hashed)

Stores
   Item_ID
   quantity
   Arrival_Date
