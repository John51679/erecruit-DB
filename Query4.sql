SELECT 	etaireia.name AS `Company Name`, 
		job.position `Posotion`, 
        antikeim.title AS `Title`,
        belongs_to	AS `Child of`
		FROM requires
			INNER JOIN job 
				ON requires.job_id = job.id 
			INNER JOIN antikeim 
				ON requires.antikeim_title = antikeim.title
			INNER JOIN recruiter 
				ON job.recruiter = recruiter.username
			INNER JOIN etaireia 
				ON recruiter.firm = etaireia.AFM
		WHERE edra LIKE 'Patra%' AND title LIKE '%Program%';