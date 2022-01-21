    FLUSH PRIVILEGES;
    ALTER USER "root"@"localhost" IDENTIFIED BY "password";
    Create database SMSdb;
    Use SMSdb;

    Create table smsData(
        id int AUTO_INCREMENT,
        phone VARCHAR(20),
        body VARCHAR(200),
        _timestamp TIMESTAMP,
        smsStatus TINYINT,
        Primary Key(id)
    );
