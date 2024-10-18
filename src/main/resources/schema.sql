-- schema.sql

CREATE TABLE SalesPoint (
                            ID INT PRIMARY KEY,
                            NAME VARCHAR(100),
                            LOCATION VARCHAR(255),
                            UNIQUE_CODE CHAR(6)
);

CREATE TABLE Voucher (
                         ID INT PRIMARY KEY,
                         NAME VARCHAR(100),
                         AMOUNT DECIMAL(10, 2),
                         SERVICE_PROVIDER VARCHAR(100)
);

CREATE TABLE Transaction (
                             ID INT PRIMARY KEY,
                             VOUCHER_ID INT,
                             SALESPOINT_ID INT,
                             SALE_DATE TIMESTAMP,
                             AMOUNT DECIMAL(10, 2),
                             IS_USED BOOLEAN,
                             USED_DATE TIMESTAMP,
                             ACTIVATION_CODE CHAR(36),
                             STATUS VARCHAR(20),
                             FOREIGN KEY (VOUCHER_ID) REFERENCES Voucher(ID),
                             FOREIGN KEY (SALESPOINT_ID) REFERENCES SalesPoint(ID)
);
