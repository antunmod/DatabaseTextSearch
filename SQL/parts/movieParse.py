# A parser for given movie.sql file
lines = [line.rstrip('\n') for line in open('movie.sql')]

movieInserts = open('movieInserts.sql','w')
categoryInserts = open('categoryInserts.sql', 'w')
movieCategoryInserts = open('movieCategoryInserts.sql', 'w')

categoryDict = dict();
categoryCounter = 1
for line in lines:
	parts = line.split(",")
	categories = parts[2][2:-1]
	types = categories.split(";");
	for category in types:
		category = category.strip()
		if category not in categoryDict.keys():
			categoryDict[category] = categoryCounter;
			categoryCounter += 1;

for key, value in categoryDict.items():
	categoryInserts.write("INSERT INTO CATEGORY(id, name) VALUES (" + str(value) + ", " + "'" + key + "'" + ");\n");

for line in lines:
	parts = line.split(",")
	categories = parts[2][2:-1]
	types = categories.split(";");
	movieInserts.write(parts[0] + "," + parts[1] + "," + parts[3] + "," + parts[4] + "\n");
	for category in types:
		category = category.strip()
		movie_id = parts[0].split("(")[1];
		movieCategoryInserts.write("INSERT INTO MOVIE_CATEGORY VALUES (" + movie_id + ", " + str(categoryDict[category]) + ");\n")