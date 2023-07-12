SELECT CONCAT_WS(" ", user.first_name, user.surname) AS 'Recruiter Name', 
	   job.id AS `Job Number`, 
       job.salary AS `Salary`, 
       etaireia.name AS `Company Name`, 
       COUNT(*) `Number of applications`
       FROM job
			INNER JOIN user 
				ON job.recruiter = user.username
			INNER JOIN applies 
				ON applies.job_id = job.id 
			INNER JOIN recruiter 
				ON job.recruiter = recruiter.username
			INNER JOIN etaireia 
				ON recruiter.firm = etaireia.AFM
	   WHERE job.salary > 1900 
       GROUP BY id;
       
SELECT id FROM interviews WHERE recruiter_username = "cleogeo";