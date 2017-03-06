DROP TABLE IF EXISTS botInfo;
CREATE TABLE botInfo(uuid VARCHAR(30) NOT NULL, name VARCHAR(30) NOT NULL, description VARCHAR(300) NOT NULL, imageURL VARCHAR(4096) NOT NULL);

DROP TABLE IF EXISTS responses;
CREATE TABLE responses(uuid VARCHAR(30) NOT NULL, intent VARCHAR(255) NOT NULL, entity VARCHAR(255), response VARCHAR(5000) NOT NULL);

DROP TABLE IF EXISTS suggestions;
CREATE TABLE suggestions(uuid varchar(30) NOT NULL, text VARCHAR(1024) NOT NULL);

INSERT INTO BotInfo VALUES('1', 'Bot 1', 'Bot 1 description', 'bot1.png');
INSERT INTO BotInfo VALUES('2', 'Bot 2', 'Bot 2 description', 'bot2.png');
INSERT INTO BotInfo VALUES('5551', 'Bot 3', 'Bot 3 description', 'bot3.png');


INSERT INTO responses VALUES
       ('1', 'GetBasics', 'NONE', 'test response goes here'),
       ('2', 'GetBasics', 'e1', 'another test asdf'),
       ('2', 'GetBasics', 'e2', 'another test'),
       ('3', 'GetRandom', 'NONE', 'random 1'),
       ('3', 'GetRandom', 'NONE', 'random 2');

INSERT INTO suggestions VALUES
       ('1', 'suggestion');

COMMIT;
SHUTDOWN;
