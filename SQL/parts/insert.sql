insert into category(id, name) values
	(1, 'action'),
	(2, 'adventure'),
	(3, 'comedy'),
	(4, 'romance'),
	(5, 'sci-fi'),
	(6, 'fantasy'),
	(7, 'documentary');

insert into movie(id, title, summary, description) values
	(1, 'The Hangover', 'This is a movie about the consequences of a hangover', 
		'There are multiple main characters and things get out of hand when...'),
	(2, 'The Hangover 2', 'This is a movie about the consequences of a hangover', 
		'There are multiple main characters and things get out of hand when...'),
	(3, 'The Hangover 3', 'This is a movie about the consequences of a hangover', 
		'There are multiple main characters and things get out of hand when...'),
	(4, 'The Hangover 4', 'This is a movie about the consequences of a hangover', 
		'There are multiple main characters and things get out of hand when...'),
	(5, 'The Hangover 5', 'This is a movie about the consequences of a hangover', 
		'There are multiple main characters and things get out of hand when...'),
	(6, 'The Hangover 6', 'This is a movie about the consequences of a hangover', 
		'There are multiple main characters and things get out of hand when...');

insert into movie_category(movie_id, category_id) values
	(1, 1), (1, 2),
	(2, 1), (2, 2),
	(3, 1), (3, 2),
	(4, 1), (4, 2),
	(5, 1), (5, 2),
	(6, 1), (6, 2);