DELIMITER $$
DROP TRIGGER IF EXISTS log_insert_on_cand$$
CREATE TRIGGER log_insert_on_cand 
	AFTER INSERT ON candidate FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Insert", "candidate");
END$$

DROP TRIGGER IF EXISTS log_delete_on_cand$$
CREATE TRIGGER log_delete_on_cand 
	AFTER DELETE ON candidate FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Delete", "candidate");
END$$

DROP TRIGGER IF EXISTS log_update_on_cand$$
CREATE TRIGGER log_update_on_cand 
	AFTER UPDATE ON candidate FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Update", "candidate");
END$$

DROP TRIGGER IF EXISTS log_insert_on_recr$$
CREATE TRIGGER log_insert_on_recr
	AFTER INSERT ON recruiter FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Insert", "recruiter");
END$$

DROP TRIGGER IF EXISTS log_delete_on_recr$$
CREATE TRIGGER log_delete_on_recr 
	AFTER DELETE ON recruiter FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Delete", "recruiter");
END$$

DROP TRIGGER IF EXISTS log_update_on_recr$$
CREATE TRIGGER log_update_on_recr 
	AFTER UPDATE ON recruiter FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Update", "recruiter");
END$$

DROP TRIGGER IF EXISTS log_insert_on_user$$
CREATE TRIGGER log_insert_on_user
	AFTER INSERT ON user FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Insert", "user");
END$$

DROP TRIGGER IF EXISTS log_delete_on_user$$
CREATE TRIGGER log_delete_on_user
	AFTER DELETE ON user FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Delete", "user");
END$$

DROP TRIGGER IF EXISTS log_update_on_user$$
CREATE TRIGGER log_update_on_user
	AFTER UPDATE ON user FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Update", "user");
END$$

DROP TRIGGER IF EXISTS log_insert_on_etaireia$$
CREATE TRIGGER log_insert_on_etaireia
	AFTER INSERT ON etaireia FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Insert", "etaireia");
END$$

DROP TRIGGER IF EXISTS log_delete_on_etaireia$$
CREATE TRIGGER log_delete_on_etaireia
	AFTER DELETE ON etaireia FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Delete", "etaireia");
END$$

DROP TRIGGER IF EXISTS log_update_on_etaireia$$
CREATE TRIGGER log_update_on_etaireia
	AFTER UPDATE ON etaireia FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Update", "etaireia");
END$$

DROP TRIGGER IF EXISTS prevent_comp_from_changing_valuable_info$$
CREATE TRIGGER prevent_comp_from_changing_valuable_info 
	BEFORE UPDATE ON etaireia FOR EACH ROW
BEGIN
IF ( OLD.name != NEW.name ) THEN
  SIGNAL SQLSTATE VALUE '45000'
	SET MESSAGE_TEXT = 'Error: You can`t update a company`s name. Refreshing company`s name to it`s previous name...';
	SET NEW.name = OLD.name;
ELSEIF (OLD.AFM != NEW.AFM) THEN
  SIGNAL SQLSTATE VALUE '45000'
	  SET MESSAGE_TEXT = 'Error: You can`t update a company`s AFM. Refreshing company`s AFM to it`s previous value...';
	  SET NEW.AFM = OLD.AFM;
ELSEIF (OLD.DOY != NEW.DOY) THEN
  SIGNAL SQLSTATE VALUE '45000'
	  SET MESSAGE_TEXT = 'Error: You can`t update a company`s DOY. Refreshing company`s DOY to it`s previous state...';
	  SET NEW.DOY = OLD.DOY;
END IF;
END$$

DROP TRIGGER IF EXISTS log_insert_on_job$$
CREATE TRIGGER log_insert_on_job
	AFTER INSERT ON job FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Insert", "job");
END$$

DROP TRIGGER IF EXISTS log_delete_on_job$$
CREATE TRIGGER log_delete_on_job
	AFTER DELETE ON job FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Delete", "job");
END$$

DROP TRIGGER IF EXISTS log_update_on_job$$
CREATE TRIGGER log_update_on_job
	AFTER UPDATE ON job FOR EACH ROW
BEGIN
	INSERT INTO user_logs (username, success, action_type, table_name)
		VALUES (username, "True", "Update", "job");
END$$

DROP TRIGGER IF EXISTS prevent_application_after_deadline$$
CREATE TRIGGER prevent_application_after_deadline
	BEFORE DELETE ON applies FOR EACH ROW
BEGIN
	IF (( SELECT job.submission_date FROM job INNER JOIN applies ON job.id = applies.job_id ) < NOW())
	THEN
		SIGNAL SQLSTATE VALUE '45000'
			SET MESSAGE_TEXT = 'Error: After submission date applications can not be deleted....';
	END IF;
END$$

