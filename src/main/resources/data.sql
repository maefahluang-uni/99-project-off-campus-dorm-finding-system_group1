

-- Insert sample data
INSERT INTO LANDLORD (email, firstName, lastName, telephone) VALUES
('landlord1@example.com', 'John', 'Doe', '123-456-7890'),
('landlord2@example.com', 'Jane', 'Smith', '987-654-3210');

INSERT INTO RATING (id,oneCount, twoCount, threeCount, fourCount, fiveCount, zeroCount) VALUES
(1,0, 0, 3, 0, 0, 0),
(2,0,2,3,4,0,0),
(3,0, 0, 0, 0, 5, 0),
(4,0,0,0,0,0,0),
(5,0,0,0,0,0,0),
(6,0,0,0,0,0,0),
(7,0,0,0,0,0,0),
(8,0,0,0,0,0,0),
(9,0,0,0,0,0,0);

-- Insert sample data into DORMITORY table
INSERT INTO DORMITORY (
    id,rating_id, name, city, province, price, gender, fullyBooked, description, rules, Amenties,
    img1, img2, img3, img4, email, landlord_email,lat,lng
) VALUES
(1,1, '3K NangLae', 'Chiang Rai', 'Mueang Chiang Rai', 3000, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', '3K1.JPG', '3K2.JPG', '3K3.JPG', '3K4.JPG', '3KNangLae@gmail.com', 'landlord1@example.com',19.90885594661509, 99.855986253231),

 (3,3, 'Regent Mansion', 'Chiang Rai', 'Mueang Chiang Rai', 4500, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'RegentMansion1.jpg', 'RegentMansion2.jpg', 'RegentMansion3.jpg', 'RegentMansion4.jpg', 'regentmansion@gmail.com.com', 'landlord1@example.com',20.04309266241252, 99.88063398206963),

 (2,2, 'Beyond', 'Chiang Rai', 'Mueang Chiang Rai',4500, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'Beyond1.JPG', 'Beyond2.JPG', 'Beyond3.JPG', 'Beyond4.JPG', 'beyond@gmail.com', 'landlord1@example.com',20.046637552020567, 99.88055756857642),

 (4,4, 'Casa', 'Bangkok', 'Bangkok', 4000, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'Casa1.jpg', 'Casa2.jpg', 'Casa3.jpg', 'Casa4.jpg', 'casa@gmail.com', 'landlord1@example.com', 13.731070368371455, 100.56310517468275),

 (5,5, 'Chatthanan', 'Salaya', 'Salaya', 2700, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'Chatthanan1.JPG', 'Chatthanan2.JPG', 'Chatthanan3.JPG', 'Chatthanan4.JPG', 'chatthanan@gmail.com', 'landlord2@example.com', 13.791758703078125, 100.32844930538447),

 (6,6, 'Home and Hill', 'Chiang Mai', 'Chiang Mai', 4000, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'HomeAndChill1.JPG', 'HomeAndChill2.jpg', 'HomeAndChill3.jpg', 'HomeandChill4.jpg', 'homeandhill@gmail.com', 'landlord1@example.com', 18.79177087959815, 98.9506369108173),

 (7,7, 'Phuket Apartment', 'Phuket', 'Phuket', 3000, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'Natthicha1.JPG', 'Natthicha2.JPG', 'Natthicha3.jpg', 'Natthicha4.JPG', 'phuketapartment@gmail.com', 'landlord2@example.com',7.88776502753229, 98.3773034517855),

 (8,8, 'STK Resort', 'Chiang Rai', 'Mueang Chiang Rai', 3000, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'STK1.JPG', 'STK2.JPG', 'STK3.JPG', 'STK4.JPG', 'stkdormitory@gmail.com', 'landlord1@example.com', 20.04467581829367, 99.87976215317447),
 
 (9,9, 'Phuthong', 'Chiang Rai', 'Mueang Chiang Rai', 2800, 'Unisex', false, 'Welcome to our vibrant and inviting dormitory, where your college experience truly comes to life! Nestled in the heart of campus, our dormitory is more than just a place to rest your head. ', 'Smoking is strictly prohibited within the dormitory premises to maintain a healthy living environment. ', 'Discover modern living at our dormitory, where high-speed Wi-Fi, spacious study lounges, and well-equipped kitchens await. ', 'PhuThong1.JPG', 'PhuThong2.JPG', 'PhuThong3.JPG', 'PhuThong4.JPG', 'phuthongdormitory@gmail.com', 'landlord2@example.com', 20.044730086284975, 99.87848786744873);


-- Insert sample data into RATING table
INSERT INTO Tenant (email, firstName, lastName, gender, phone, password)
VALUES
    ('admin@comfynest.com', 'admin', 'admin', 'Male', '+999999999', '$2a$10$E7mzNVJGhWXTQa4MctswUOfH4nItgL1gYOCYd0cGFJyQdVWFMW6he'); -- Password is "adminmfu"

