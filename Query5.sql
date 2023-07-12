SELECT 	 recruiter.username,
		(SELECT COUNT(*) FROM job WHERE recruiter.username = job.recruiter) AS `Announced jobs`,
		(SELECT COUNT(*) FROM interviews WHERE recruiter.username = interviews.recruiter_username) AS `Number of Interviews`, 
        FORMAT(AVG(job.salary), 0) `Average Salary` 
		FROM recruiter
		INNER JOIN job 
			ON recruiter.username = job.recruiter
		GROUP BY username
		HAVING `Announced jobs` >= 1
		ORDER BY AVG(job.salary) DESC;

