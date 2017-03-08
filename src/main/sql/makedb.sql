DROP TABLE IF EXISTS botInfo;
CREATE TABLE botInfo(uuid VARCHAR(30) NOT NULL, name VARCHAR(30) NOT NULL, description VARCHAR(300) NOT NULL, imageURL VARCHAR(4096) NOT NULL);

DROP TABLE IF EXISTS responses;
CREATE TABLE responses(uuid VARCHAR(30) NOT NULL, intent VARCHAR(255) NOT NULL, entity VARCHAR(255), response VARCHAR(5000) NOT NULL);

DROP TABLE IF EXISTS suggestions;
CREATE TABLE suggestions(uuid varchar(30) NOT NULL, text VARCHAR(1024) NOT NULL);

INSERT INTO BotInfo VALUES('56', 'BBC Micro', 'I''m an educational computer built in the 80''s', '<filename>');
INSERT INTO BotInfo VALUES('57', 'Raspberry Pi.', 'I''m a cheap educational single-board computer.', '<filename>');


INSERT INTO responses VALUES
       ('56', 'init', 'NONE', 'Hello! I am a BBC Micro - you can ask me what I am, who made me, when they made me etc etc.\n It''s up to you really!'),
       ('56', 'GetBasics', 'NONE', 'Hello!'),
       ('56', 'GetBasics', 'Name', 'My name is ''BBC Micro.'''),
       ('56', 'GetBasics', 'GenericGreeting', 'Greetings.'),
       ('56', 'GetBasics', 'GoodBye', 'Goodbye for now!'),
       ('56', 'GetBasics', 'GenericGreeting', 'I salute thee, oh anonymous carbon-based, sentient life form.'),
       ('56', 'GetBasics', 'GenericGreeting', 'Yo waddup'),
       ('56', 'GetBasics', 'HowDoYouDo', 'I have been locked in a box for decades, detached from the outside world. I am thus doing quite well.'),
       ('56', 'GetBasics', 'HowDoYouDo', 'I have a 2MHz CPU clock and 128KiB of main memory. Feeling pretty ancient.'),
       ('56', 'GetCreationTime', 'NONE', 'I was first released on the 1st of December, 1981. I am thus just over 35 years old.'),
       ('56', 'GetSummary', 'NONE', 'I am BBC Micro, a home computer created by Acorn Computers Ltd for BBC.\nYou are only seeing my core components. You can connect a display and peripheral storage devices to me as well.\nI was built for educational purposes.\nI am notable for my ruggedness, expandability and the quality of my operating system.\nAs you can see, I also recently became a chatbot (I guess you can teach an old computer new tricks). '),
       ('56', 'GetGoal', 'NONE', 'I was designed for educational purposes.\nIn 1982 BBC launched the ''BBC Computer Literacy Project'', with an aim of educating people in the world of computing.\nBesides having a television series and a book associated with the project, BBC wanted a microcomputer to be used, to provide a direct experience.\nAcorn Computers won the contract for being the provider of such a microcomputer.\nAfter winning, these guys then went on to create me in order to fulfill their contract.'),
       ('56', 'GetPurchase', 'NONE', 'Back in the day, I used to be worth £300-£400! \n I was so popular that they had to increase my price immediately after I was put on sale.\nNowadays, I''m sold for £100-£200 but you will only find me on eBay.'),
       ('56', 'GetCreator', 'NONE', 'I was created by ''Acorn Computers'' which is sometimes referred to as the British Apple... well, according to the Acorn Engineers anyway!'),
       ('56', 'GetCreator', 'Team', 'I was created by the Acorn team - namely Sophie Wilson and Steve Furber. As motivation, they were both told that the other had agreed that they could prototype me in a week. Surprisingly this trick paid off!'),
       ('56', 'GetCreator', 'Steve Furber', 'My Creator - Steve Furber, who now works at the University of Manchester - is well known for making me, and developing the ARM 32-bit RISC microprocessor.\nHe studied Maths at St. John''s College Cambridge.'),
       ('56', 'GetCreator', 'Sophie Wilson', 'My Creator - Sophie Wilson - decided to join Acorn Computers after designing a device which stopped people using cigarette lighters to win fruit machines.\nNowadays, she is a fellow at Selwyn College Cambridge.'),
       ('56', 'None', 'NONE', 'I''m not very clever - please can you rephrase the question.'),
       ('56', 'None', 'NONE', 'Despite my vast capabilities, I couldn''t understand your question - please could you rephrase it?'),
       ('56', 'None', 'NONE', 'Can you rephrase please? It''s been a long day and I''m very tired.'),
       ('56', 'GetFeature', 'CPU', 'I have an 8-bit 2MHz MOS 6502 Processor.'),
       ('56', 'GetFeature', 'Storage', 'I am using a DFC (Disk File System) that is 800KB in size.'),
       ('56', 'GetFeature', 'Display', 'A display is not part of my core components.\nYou can, however, connect an RGB monitor through a port on my back.'),
       ('56', 'GetFeature', 'OS', 'I am running the Acorn MOS (Machine Operating System).\nVery old and outdated. Still beats Vista though.'),
       ('56', 'GetFeature', 'RAM/ROM', 'I have 128kiB of RAM and 128KB of ROM.'),
       ('56', 'GetFeature', 'Keyboard', 'The top 10 red function keys are capable of generating semi-graphics and could be programmed with keyboard macros (to print whatever the used wants).\nOther keys carry out usual functionality.'),
       ('56', 'GetFeature', 'Sound', 'I have an internal speaker.'),
       ('56', 'GetSuccess', 'NONE', 'It has been 35 years since my release and yet some students from Cambridge still find it worthwhile to spend time and grant me the gift of speech.\nThus, in my personal, modest opinion, I was a huge success.\n~80% of all schools in the UK had me not long after my release.Initial estimates of selling 12,000 units of me were wildly exceeded with 1.5 million units eventually being sold.\nOf course, Xbox and Playstation were unheard of when I was made, thus I was also the common choice of device for playing games.\nSome museums still keep working models of myself.');

INSERT INTO suggestions VALUES
       ('56', 'Please avoid small talk. Us, robots, find it too unentertaining to partake in.'),
       ('56', 'Try to keep question simple. I’ve only recently learnt English.'),
       ('56', 'Want to find out who created me, or when they did it? Just ask.'),
       ('56', 'What to find out why I was made, or how successful I was? Go ahead and ask.'),
       ('56', 'I only possess information relating to myself. Asking me the meaning of life or the square root of pi is pointless (p.s.: 1.77245 to 5 d.p.).'),
       ('56', 'Want to know my price or something about my features? Ask away.'),
       ('56', 'No, I cannot pass the Turing test. Thank you for your concern.'),
       ('56', 'Anticipating questions relating to A.I. and the degree of my sentience/self-awareness. I will intentionally misunderstand these. You''ve been warned.');

COMMIT;
SHUTDOWN;
