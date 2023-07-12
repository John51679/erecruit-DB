SELECT 	candidate.username AS `Username`, 
		COUNT(*) AS `Number of Aplications`, 
        FORMAT(AVG(job.salary), 0) `Average Salary`
		FROM applies
			INNER JOIN candidate 
				ON candidate.username = applies.cand_usrname
			INNER JOIN job 
				ON job.id = applies.job_id 
		GROUP BY candidate.username
		HAVING AVG(job.salary) > 1800;