# DIL2ONE-mini-project
A Java-based demonstration project showcasing kg.tursunbek.data processing, database management, and template generation. This project was developed as a skill demonstration for the DIL2ONE initiative.
This application automates the ingestion of insurance customer kg.tursunbek.data into a local SQLite database and generates formatted reports using LaTeX.

Run the main application to choose between two primary actions: 
1) Generate Letters: Produces result1.tex containing letters for unsatisfied customers.
2) Generate Regional Table: Produces result2.tex containing regional kg.tursunbek.data from the source.

Used tools: SQLite JDBC API as well OpenCSV and Free Marker libraries. Closer look to the tools you can find under: \
SQLite JDBC: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc \
OpenCSV: https://opencsv.sourceforge.net/ \
Free Marker Template Engine: https://freemarker.apache.org/index.html 

Source for the database: https://www.kaggle.com/datasets/archanagajendra/insurance-customer-kg.tursunbek.data
