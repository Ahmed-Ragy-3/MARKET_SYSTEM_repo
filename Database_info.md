Database info:
   Username(Schema): MARKET
   Password: marketdb
   Port: 1521
   Host: LOCALHOST
   Connect as: SYSDBA
   Service Name: ORCLPDB
   Connect using Oracle client: OraDB21Home1


Products Table:
   Name	      	   Type           Null?
   PRODUCT_ID		   NUMBER         NOT NULL
   PRODUCT_NAME	   VARCHAR2(500)  NOT NULL
   PRICE	            NUMBER(10,4)   NOT NULL
   RATE		         NUMBER(3,2)
   DISCOUNT		      NUMBER
   BRAND		         VARCHAR2(100)
   QUANTITY	         NUMBER(5)      NOT NULL
   IMAGE_URL		   VARCHAR2(1000)
   CATEGORY		      VARCHAR2(50)
   PRODUCTION_DATE	DATE
   IMAGES		      VARCHAR2(4000)
   FEATURES		      VARCHAR2(4000)
   DESCRIPTION		   CLOB

Category
   ID
   NAME
   Number of Items
   Image

Users Table:
   Name	   	      Type           Null?
   USER_ID		      NUMBER         NOT NULL
   USERNAME	         VARCHAR2(30)   NOT NULL
   PASSWORD	         VARCHAR2(100)  NOT NULL
   NEW_SUGGESTIONS   NUMBER(3)
   BALANCE		      NUMBER


Stores
   Item_ID
   quantity
   Arrival_Date

Suggestions Table:
   Name	   	   Type
   SENDER_ID	   NUMBER
   RECIEVER_ID	   NUMBER
   PRODUCT_ID	   NUMBER




Export command
expdp MARKET/marketdb@localhost:1521/ORCLPDB schemas=MARKET directory=db_dir dumpfile=MARKET.dmp logfile=MARKET_export.log

Import command
impdp MARKET/marketdb@localhost:1521/ORCLPDB schemas=MARKET directory=db_dir dumpfile=MARKET.dmp logfile=import.log