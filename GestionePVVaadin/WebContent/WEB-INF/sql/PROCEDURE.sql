DELIMITER //
CREATE PROCEDURE DOMOV (rowsite int, rowtypeid int, rowart int, rowq int, mul int) 
	BEGIN
		SELECT @EFFECT := QUANTITY_EFFECT FROM MOVIMENTATION_TYPES WHERE ID = rowtypeid;
	    IF NOT EXISTS (SELECT 1 FROM INVENTORY WHERE SITE = rowsite AND ARTICLE = rowart) THEN 
			INSERT INTO INVENTORY (SITE, ARTICLE) VALUES ( rowsite , rowart );
		END IF;
		UPDATE INVENTORY SET QUANTITY = QUANTITY + ( rowq * EFFECT * mul) WHERE SITE = rowsite AND ARTICLE = rowart;
		UPDATE ARTICLES SET QUANTITY = QUANTITY + ( rowq * EFFECT * mul) WHERE ID = rowart;
	END; //
DELIMITER ;
	

DELIMITER //
CREATE PROCEDURE DOUPDATE (oldrowsite int, oldroweffid int, oldrowart int, oldrowq int, newrowsite int, newroweffid int, newrowart int, newrowq int) 
	BEGIN 
		CALL DOMOV (oldrowsite, oldroweffid, oldrowart, oldrowq, -1 );
		CALL DOMOV (newrowsite, newroweffid, newrowart, newrowq, 1 );
	END; //
DELIMITER ;
