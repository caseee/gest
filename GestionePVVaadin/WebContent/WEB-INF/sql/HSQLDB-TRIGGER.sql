CREATE TRIGGER MOVIMENTATION_SPECS_INS AFTER INSERT ON MOVIMENTATION_SPECS 
	REFERENCING NEW ROW AS newrow FOR EACH ROW
	CALL DOMOVFROMROW ( newrow.ID, 1 );
	
CREATE TRIGGER MOVIMENTATION_SPECS_DEL AFTER DELETE ON MOVIMENTATION_SPECS 
	REFERENCING OLD ROW AS oldrow FOR EACH ROW
	CALL DOMOVFROMROW ( oldrow.ID, -1 );
	  
CREATE TRIGGER MOVIMENTATION_SPECS_AF_UPD AFTER UPDATE ON MOVIMENTATION_SPECS 
  	REFERENCING OLD ROW AS oldrow NEW ROW AS newrow FOR EACH ROW
   	CALL DOUPDATE ( oldrow.id , newrow.ID );
   	
   	
   	
   	
   	