CREATE TABLE interviews (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    job_id INT NULL NULL,
    cand_username VARCHAR(12) NOT NULL,
    recruiter_username VARCHAR(12) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    start_time TIME DEFAULT NULL,
    finish_time TIME DEFAULT NULL,
    duration TIME GENERATED ALWAYS AS (TIMEDIFF(finish_time, start_time)) VIRTUAL,
    personality_score INT DEFAULT "-1",
    education_score INT DEFAULT "-1",
    experience_score INT DEFAULT "-1",
    CONSTRAINT INTER_JOB FOREIGN KEY (job_id) REFERENCES job(id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT INTER_RECR FOREIGN KEY (recruiter_username) REFERENCES recruiter(username) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT INTER_CAND FOREIGN KEY (cand_username) REFERENCES candidate(username) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT CHK_PERS CHECK (personality_score <= 5 AND personality_score >= -1),
    CONSTRAINT CHK_EDU CHECK (education_score <= 5 AND education_score >= -1),
    CONSTRAINT CHK_EXP CHECK (experience_score <= 5 AND experience_score >= -1)
)engine=InnoDB;	

INSERT INTO interviews (job_id, cand_username, recruiter_username, created_at, 			 start_time,   finish_time, 	   personality_score , education_score, experience_score)
			VALUES	  (2	 , 'cleogeo'	, 'n_tri' 			, "2018-07-14 15:39:18", "11:39:18"	 , "12:09:45"   	 , "0"				 , "5" 			  , "3"),
					  (2	 , 'zazahir23'	, 'n_tri' 			, "2018-07-15 18:05:28", "13:09:48"	 , "14:01:25"   	 , "5"				 , "3" 			  , "2"),
					  (2	 , 'lionarF'	, 'n_tri' 			, "2018-07-15 14:34:32", "10:32:48"	 , "10:56:32"   	 , "4"			 	 , "4" 			  , "5"),
                      (2	 , 'lionarF'	, 'n_tri' 			, "2018-10-27 13:40:00", "10:40:03"	 , "11:20:04"   	 , "3"				 , "4" 			  , "5");
                      
CREATE TABLE sections (
	title VARCHAR(50) NOT NULL PRIMARY KEY,
    descr TEXT,
	belong_to  VARCHAR(50) NULL DEFAULT NULL,
    CONSTRAINT SEC_SEC FOREIGN KEY (belong_to) REFERENCES sections(title) ON DELETE CASCADE ON UPDATE CASCADE
)engine=InnoDB;	

INSERT INTO sections (title, descr, belong_to)
	VALUES ('Software Development', 'Software development is the process of conceiving, specifying, designing, programming, documenting, testing, and bug fixing involved in creating and maintaining applications, frameworks, or other software components.', NULL),
		   ('Web Development', 'Web development is the work involved in developing a web site for the Internet (World Wide Web) or an intranet (a private network).', NULL),
		   ('Application Development', 'Application development is the process of creating a computer program or a set of programs to perform the different tasks that a business requires.', 'Software Development'),
		   ('Data Science', 'Data science is an interdisciplinary field that uses scientific methods, processes, algorithms and systems to extract knowledge and insights from data in various forms, both structured and unstructured, similar to data mining.' , 'Software Development'),
		   ('Web design', 'Web design encompasses many different skills and disciplines in the production and maintenance of websites. The different areas of web design include web graphic design; interface design; authoring, including standardised code and proprietary software; user experience design; and search engine optimization. ', 'Web Development'),
		   ('User interface design', 'User interface design (UI) or user interface engineering is the design of user interfaces for machines and software, such as computers, home appliances, mobile devices, and other electronic devices, with the focus on maximizing usability and the user experience. ', 'Web design'),
		   ('Database Management and Architecture', 'This role is responsible for designing, deploying, and maintaining databases in support of high volume, complex data transactions for specific services or groups of services.', 'Data Science');

CREATE TABLE has_section (
	sec_title VARCHAR(50) NOT NULL,
    company_afm CHAR(9) NOT NULL,
    PRIMARY KEY (sec_title, company_afm),
	CONSTRAINT `HAS_SEC` FOREIGN KEY (sec_title) REFERENCES sections(title) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT `HAS_COM` FOREIGN KEY (company_afm) REFERENCES etaireia(AFM) ON DELETE CASCADE ON UPDATE CASCADE
)engine=InnoDB;	

INSERT INTO  has_section(sec_title, company_afm) 
	VALUES	('Software Development', '561234561'),
			('Data Science', '561234561'),
			('Database Management and Architecture', '561234561'),
			('Software Development', '023453344'),
			('User interface design', '023451232'),
			('Data Science', '123432211'),
			('Software Development', '18765549'),
			('Web design', '023451232'),
			('Web design', '23122345');
            
CREATE TABLE user_logs (
   id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
   username VARCHAR(12) NOT NULL DEFAULT 'admin',
   happened_at TIMESTAMP DEFAULT NOW(),
   action_type ENUM('Insert', 'Update', 'Delete') NOT NULL,
   success ENUM('True', 'False') NOT NULL,
   table_name VARCHAR(20) NOT NULL
)engine = InnoDB;

insert into user_logs (username, success, happened_at, action_type, table_name)
	VALUES  ('cleogeo', 'true', '2019-01-05 15:10:34','Insert', 'project'),
		    ('lionarF', 'false', '2019-02-02 21:14:44','Delete', 'applies'),
			('mnikol', 'true', '2019-01-07 19:17:54','Update', 'project'),
			('varcon82', 'true', '2017-04-06 7:59:10','Insert', 'job'),
			('n_tri', 'true', '2017-04-06 8:01:52','Insert', 'job'),
			('msmith', 'true', '2019-01-06 9:23:30','Update', 'job'),
			('varcon82', 'true', '2019-01-06 9:23:30','Update', 'job'),
			('Giankost', 'false', '2018-12-06 10:56:13','Delete', 'job'),
			('pavkie', 'true', '2018-11-06 13:43:03','Update', 'job'),
			('liagourma', 'true', '2018-11-26 22:43:03','Update', 'project');
            
CREATE TABLE admin_db (
username VARCHAR(12) NOT NULL,
PRIMARY KEY (username),
CONSTRAINT ADM_USER FOREIGN KEY (username) REFERENCES `user`(username) ON DELETE CASCADE ON UPDATE CASCADE
)engine=InnoDB;

insert into admin_db VALUES  ('vagos98'),('giannis98');