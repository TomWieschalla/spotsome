-- user
insert into user (user_id, email) values (100, 'alf@melmac.org');
insert into user (user_id, email) values (200, 'chill@beuth-hochschule.de');

-- location
-- ---------------------------------------
-- Rock am Ring
insert into location (location_id, latitude, longitude) values (1, 50.365278, 7.3125);
-- Hurricance
insert into location (location_id, latitude, longitude) values (2, 53.1697267, 9.4870781);
-- Beuth Haus A
insert into location (location_id, latitude, longitude) values (3, 52.54330, 13.35074);
-- Beuth Mensa
insert into location (location_id, latitude, longitude) values (4, 52.54462, 13.35500);

-- spot
insert into spot (spot_id, name, radius, location_id) values (1, 'Rock am Ring', 500, 1);
insert into spot (spot_id, name, radius, location_id) values (2, 'Hurricane', 1000, 2);
insert into spot (spot_id, name, radius, location_id) values (3, 'Hurricane2', 1000, 2);

insert into spot (spot_id, name, radius, location_id) values (4, 'Beuth Haus A', 500, 3);
insert into spot (spot_id, name, radius, location_id) values (5, 'Beuth Mensa', 200, 4);

-- message
--insert into message (message_id, message_text, media_wrapper_id, user_id) values (500, 'null problemo', null, 100);
--insert into message (message_id, message_text, media_wrapper_id, user_id) values (501, 'Da sind Haare im Thunfisch. Also mir schmeckts!', null, 100);
--insert into message (message_id, message_text, media_wrapper_id, user_id) values (502, 'Wer hat Hunger?', null, 200);
--insert into message (message_id, message_text, media_wrapper_id, user_id) values (503, 'Heute gibt es nur Müll!', null, 200);
--
--
--insert into spot_message (spot_id, message_id) values (1, 500);
--insert into spot_message (spot_id, message_id) values (1, 501);
--insert into spot_message (spot_id, message_id) values (5, 502);
--insert into spot_message (spot_id, message_id) values (5, 503);