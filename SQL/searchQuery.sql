SELECT ts_headline(title, to_tsquery('english', 'Hangover | multiple')),
		ts_headline(description, to_tsquery('english', 'Hangover | multiple')),
		description,
		ts_rank(to_tsvector(title || description), to_tsquery('english', 'Hangover | multiple | random'))
		from movie
		where to_tsvector(title) @@ to_tsquery('english', 'Hangover') OR
		to_tsvector(description) @@ to_tsquery('english', 'random') -- OR
		-- description::TSVector @@ to_tsquery('english', 'multiple')

SELECT ts_headline(title, to_tsquery('english', '2 | Hangover & multiple')),
		ts_headline(description, to_tsquery('english', 'Hangover & multiple | main')),
		description,
		ts_rank(to_tsvector(title || description), to_tsquery('english', 'Hangover & The & multiple & random'))
		from movie
		where to_tsvector(title) @@ to_tsquery('english', 'Hangover') OR
		to_tsvector(description) @@ to_tsquery('english', 'random') -- OR
		-- description::TSVector @@ to_tsquery('english', 'multiple')

SELECT ts_headline(title, to_tsquery('english', 'Dance | (Legend & of & Tarzan) & (Lord & of)')),
		ts_headline(description, to_tsquery('english', 'Dance | (Legend & of & Tarzan) & (Lord & of)')),
		description,
		ts_rank(movieTSV, to_tsquery('english', 'Dance | (Legend & of & Tarzan) & (Lord & of)')) rank
		FROM movie JOIN movieTSV ON movie.id = movieTSV.id
		WHERE
		movieTSV @@ to_tsquery('english', 'Dance') OR
		movieTSV @@ to_tsquery('english', 'Legend & of & Tarzan') OR
		movieTSV @@ to_tsquery('english', 'Lord & of')
		ORDER BY rank DESC

SELECT * FROM crosstab('SELECT query, EXTRACT(DATE from executed), COUNT(*)')

SELECT query, executed::date dayExecuted,
			EXTRACT(HOUR from executed) hourExecuted, COUNT(*) FROM search
group by query, dayExecuted


select * from crosstab('SELECT query, executed::DATE dayExecuted, COUNT(*) FROM search
	group by query, dayExecuted',
	'select distinct executed::DATE from search')
AS pivotTable (query VARCHAR(40), s00 INT)
ORDER BY query

select * from crosstab('SELECT query, executed::DATE dayExecuted, COUNT(*) FROM search
		where executed::DATE > ''10-10-2018'' and executed::DATE < ''20-10-2018''
	group by query, dayExecuted',
	'select generate_series(0,1)')
AS pivotTable (query VARCHAR(40), s00 INT, s01 INT)
ORDER BY query


select * from crosstab('SELECT query,
			EXTRACT(HOUR from executed) hourExecuted, COUNT(*) FROM search
			where executed > ''10-10-2018'' and executed < ''20-10-2018''

			group by query, hourExecuted',
					  'select generate_series(0,23)')
AS pivotTable (query VARCHAR(40), s00 INT, s01 INT, s02 INT, s03 INT, s04 INT, s05 INT, s06 INT, s07 INT, s08 INT, s09 INT, s10 INT, s11 INT, s12 INT, s13 INT, s14 INT
			  , s15 INT, s16 INT, s17 INT, s18 INT, s19 INT, s20 INT, s21 INT, s22 INT, s23 INT)
ORDER BY query


