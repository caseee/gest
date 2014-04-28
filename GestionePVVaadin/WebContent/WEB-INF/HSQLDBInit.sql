# File contentente le query di inizializzazione database
CREATE TABLE USERS (ID IDENTITY, USERNAME VARCHAR(255) UNIQUE NOT NULL, PASS VARCHAR(255) NOT NULL, LEVEL INT NOT NULL); 
CREATE TABLE GROUPS (ID IDENTITY, FATHER_ID INTEGER REFERENCES GROUPS (ID), DESCRIPTION VARCHAR(256) );
CREATE TABLE ARTICLES (ID IDENTITY, GROUP_ID INTEGER REFERENCES GROUPS, NAME VARCHAR(255) NOT NULL, DESCRIPTION VARCHAR(255), PRICE INT);
CREATE TABLE MOVIMENTATION (ID IDENTITY, DESCRIPTION VARCHAR(255));
CREATE TABLE MOVIMENTATION_SPEC(ID IDENTITY, ID_HEAD INTEGER REFERENCES MOVIMENTATION (ID), ID_ART INTEGER REFERENCES MOVIMENTATION (ID), QUANTITY INTEGER, PRICE DECIMAL(10,2));
INSERT INTO USERS VALUES (NULL,'admin','70b3f062173dafebc53c2ef9bf20e17d9b0e63d2',99);
INSERT INTO GROUPS VALUES (NULL,NULL,'GENERALE');
INSERT INTO ARTICLES VALUES (NULL,IDENTITY(), 'ARTICOLO1','TEST1',10);
             