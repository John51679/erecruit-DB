DROP PROCEDURE IF EXISTS getInterviewsAboutJob;
DELIMITER $
CREATE PROCEDURE getInterviewsAboutJob(IN jobId INT)
BEGIN
    DECLARE userName VARCHAR(12);
    DECLARE was_interviewed VARCHAR(12);
	DECLARE reason VARCHAR(100) DEFAULT "";
    DECLARE bad_condition INT DEFAULT 0;
    DECLARE not_found INT;
    
	DECLARE bcursor CURSOR FOR
	    SELECT cand_usrname FROM applies WHERE job_id = jobId;
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET not_found=1;
    
    
	OPEN bcursor;
    SET not_found=0;
WHILE not_found != 1 DO
		FETCH bcursor INTO userName;
        IF (SELECT NOT EXISTS (SELECT cand_username FROM interviews WHERE cand_username = userName AND interviews.job_id = jobId)) THEN
				SET reason = concat(reason, "Hasn't interviewed yet ");
                SET bad_condition = 1;
-- 				SELECT userName, reason;
            END IF;
            
			IF ((SELECT personality_score FROM interviews WHERE cand_username = userName AND interviews.job_id = jobId LIMIT 1) = 0 ) THEN
				SET reason = concat(reason, "Failed the interview. ");
                SET bad_condition = 1;
--                 SELECT userName, reason;	
            END IF;	
            
			IF ((SELECT personality_score FROM interviews WHERE cand_username = userName AND interviews.job_id = jobId LIMIT 1) = -1 
					OR (SELECT education_score FROM interviews WHERE cand_username = userName AND interviews.job_id = jobId LIMIT 1) = -1 
                    OR (SELECT experience_score FROM interviews WHERE cand_username = userName AND interviews.job_id = jobId LIMIT 1) = -1 ) THEN
				SET reason = concat(reason, "The interview has not been evaluated yet. ");
                SET bad_condition = 1;
--                 SELECT userName, reason;	
            END IF;	
            
            IF ((SELECT education_score FROM interviews WHERE cand_username = userName AND interviews.job_id = jobId LIMIT 1) = 0 ) THEN
				SET reason = concat(reason, "Inadequate education. ");
                SET bad_condition = 1;
--                 SELECT userName, reason;
			END IF;
            
			IF ((SELECT experience_score FROM interviews WHERE cand_username = userName AND interviews.job_id = jobId LIMIT 1) = 0 ) THEN
				SET reason = concat(reason, "No prior experience. ");
                SET bad_condition = 1;
--                 SELECT userName, reason;
			END IF;  
            
			IF (bad_condition = 1) THEN 
				SELECT userName, reason; 
			ELSE
				SELECT userName,
				   COUNT(*) AS 'Number_of_Interviews',
                   FORMAT((AVG(personality_score) + education_score + experience_score), 2) AS 'Total Score'
				FROM applies
				INNER JOIN interviews ON 
					(applies.job_id = jobId AND applies.job_id = interviews.job_id AND applies.cand_usrname = interviews.cand_username)
				WHERE cand_usrname = userName
				GROUP BY applies.cand_usrname
				ORDER BY AVG(personality_score) + education_score + experience_score DESC;
			END IF;
            
		SET reason = "";
        SET bad_condition = 0;
END WHILE;
CLOSE bcursor;
END$
DELIMITER ;
