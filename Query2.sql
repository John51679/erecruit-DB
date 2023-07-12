SELECT  candidate.username AS `Candidate Username`, 
		candidate.certificates AS `Certificates`, 
		COUNT(*) AS `Number of degrees`,
        FORMAT(AVG(grade), 2) `Average Grade`
		FROM has_degree
			INNER JOIN candidate 
				ON has_degree.cand_usrname = candidate.username
		GROUP BY username
		HAVING COUNT(*) > 1;
