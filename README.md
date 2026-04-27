# DIL2ONE-mini-project
A Java-based demonstration project showcasing CSV paring, database management and template generation. This project was developed as a skill demonstration for the DIL2ONE initiative.

The project:<br>
Reads input data from a CSV file<br>
Uses the data to build or parameterize an SQL query<br>
Executes the query<br>
Passes the query result to a template engine for processing<br>
Produces rendered output based on the template

The project has one prewritten template and a sql query with to it run. The user will be asked, if he wants to process hiw own data or 
the prewritten one.

The project has minor restrictions: <br>
The CSV file must have in its column names the data type to be passed to a SQL databasse. <br>
The names of the columns and variables names in the template must be matched. <br>
The name of the data model must be named as "root".

Used tools: SQLite JDBC API as well OpenCSV and Free Marker libraries. Closer look to the tools you can find under: \
SQLite JDBC: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc \
OpenCSV: https://opencsv.sourceforge.net/ \
Free Marker Template Engine: https://freemarker.apache.org/index.html 

Source for the database: https://www.kaggle.com/datasets/archanagajendra/insurance-customer
