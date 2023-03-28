SHOW TABLES;

select * from  booking_fe1f9e3e;
drop table booking;
select * from users;
select * from bus_service; 

truncate table user_tickets_1;

select * from user_tickets_2;

DESCRIBE user_tickets_1;

CREATE TABLE bus_service (
    id INT AUTO_INCREMENT PRIMARY KEY,
    departure_location VARCHAR(255) NOT NULL,
    arrival_location VARCHAR(255) NOT NULL,
    departure_date DATE NOT NULL,
    departure_time TIME NOT NULL,
    arrival_date DATE NOT NULL,
    arrival_time TIME NOT NULL,
    price DECIMAL(10,2) NOT NULL
);

SELECT * FROM bus_service
WHERE departure_location = 'Manchester'
AND arrival_location = 'Liverpool';



SELECT * FROM bus_service ORDER BY departure_time ASC;

CREATE TABLE users (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    EMAIL VARCHAR(255) NOT NULL,
    PASSWORD VARCHAR(255) NOT NULL,
    ADDRESS VARCHAR(255) NOT NULL,
    PHONE_NUMBER VARCHAR(255) NOT NULL,
    FUNDS INT NOT NULL
);

DROP PROCEDURE fill_bus_service_northern_england;



DELIMITER //
CREATE PROCEDURE fill_bus_service_northern_england()
BEGIN
    DECLARE i INT DEFAULT 0;
    DECLARE locations TEXT;
    SET locations = 'Carlisle,Chester,Durham,Harrogate,Hull,Lancaster,Leeds,Liverpool,Manchester,Newcastle,Sheffield,York';

    WHILE i < 1000 DO
        INSERT INTO bus_service (
            departure_location,
            arrival_location,
            departure_date,
            departure_time,
            arrival_date,
            arrival_time,
            price
        )
        VALUES (
            SUBSTRING_INDEX(SUBSTRING_INDEX(locations, ',', 1 + FLOOR(RAND() * (LENGTH(locations) - LENGTH(REPLACE(locations, ',', '')) + 1))), ',', -1),
            SUBSTRING_INDEX(SUBSTRING_INDEX(locations, ',', 1 + FLOOR(RAND() * (LENGTH(locations) - LENGTH(REPLACE(locations, ',', '')) + 1))), ',', -1),
            CURRENT_DATE,
            SEC_TO_TIME(15 * 60 * FLOOR(RAND() * (60 * 24 / 15 - 4))),
            CURRENT_DATE + INTERVAL IF(TIME_TO_SEC(SEC_TO_TIME(15 * 60 * FLOOR(RAND() * (60 * 24 / 15 - 4)))) + (3 * 60 * 60) >= 86400, 1, 0) DAY,
            SEC_TO_TIME(MOD(TIME_TO_SEC(SEC_TO_TIME(15 * 60 * FLOOR(RAND() * (60 * 24 / 15 - 4)))) + (3 * 60 * 60), 86400)),
            10 + FLOOR(RAND() * 53) * 0.50
        );
        SET i = i + 1;
    END WHILE;
END //
DELIMITER ;

CALL fill_bus_service_northern_england();

CREATE TABLE bus_service_temp LIKE bus_service;

INSERT INTO bus_service_temp
SELECT DISTINCT
    id,
    departure_location,
    arrival_location,
    departure_date,
    departure_time,
    arrival_date,
    arrival_time,
    price
FROM
    bus_service;

TRUNCATE TABLE bus_service;
INSERT INTO bus_service SELECT * FROM bus_service_temp;
DROP TABLE bus_service_temp;
