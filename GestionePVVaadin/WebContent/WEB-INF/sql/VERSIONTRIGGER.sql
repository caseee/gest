CREATE TRIGGER SITES_VERSION BEFORE UPDATE ON SITES
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER USERS_VERSION BEFORE UPDATE ON USERS
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER COLORS_VERSION BEFORE UPDATE ON COLORS
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER SEASONS_VERSION BEFORE UPDATE ON SEASONS
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER SIZES_VERSION BEFORE UPDATE ON SIZES
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER CATEGORIES_VERSION BEFORE UPDATE ON CATEGORIES
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER ARTICLES_VERSION BEFORE UPDATE ON ARTICLES 
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER REGISTRY_VERSION BEFORE UPDATE ON REGISTRY
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER MOVIMENTATION_TYPES_VERSION BEFORE UPDATE ON MOVIMENTATION_TYPES
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER MOVIMENTATIONS_VERSION BEFORE UPDATE ON MOVIMENTATIONS
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER MOVIMENTATION_SPECS_VERSION BEFORE UPDATE ON MOVIMENTATION_SPECS
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;
	
CREATE TRIGGER INVENTORY_VERSION BEFORE UPDATE ON INVENTORY
	FOR EACH ROW
	SET NEW.VERSIONID = OLD.VERSIONID+1;

CREATE TRIGGER ARTICLES_VERSION_CREAT BEFORE INSERT ON ARTICLES 
	FOR EACH ROW
	SET NEW.VERSIONID = 0;
	
CREATE TRIGGER MOVIMENTATIONS_VERSION_CREAT BEFORE INSERT ON MOVIMENTATIONS 
	FOR EACH ROW
	SET NEW.VERSIONID = 0;
	
	


