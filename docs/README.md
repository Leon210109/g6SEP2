# SEP2

For this programm to run correctly you need 3 things in your library in Intellij: 1. gson.jar 2. openjfx-26.0.1 3. postgresql-42.7.11,
the correct gson, postgresql and openjfx are in the lib folder already, but for some unknown reason they tend to not want to cooperate from time to time. If that happens, you can go into file->project structure->libraries and if you click on the plus sign above the list of current libraries. You can remake the libraries here. If intellij prompts you with wether you want to replace the existing labrary click on yes, click apply and ok. Now it should work.

Now for a quick instruction on how to setup the system. 
After seting up the correct libraries, there is a folder in the project called database. In that folder you will find a sql file, copy all the queries in there and put them into a new console on your datagrip. The first querie will make a new database called roomrental. When that is made you will most likely have to open a new console in that specific database. Copy all the queries again and run them one by one from the querie that will make the Sep2 table down. When you have run all the queries, you should have 9 tables in the database, including some seeded data for a couple of them to play around with.

Once the database is setup go into the folder called Server, here you can find all the DAO's and most importantly the file databaseConnection. In this file is the postgres connection, you need to change the password in there to your own password. after that change there is a file called DatabaseDiagnostic.java. Running that file will test the database connection, and if the connection fails it tells you what wight be going wrong.

Thats all there is to it, if you have followed all these instructions the system should be ready to run. In the folder main you will find the Main.java file, running that should start the system.

For a User guide to the system you can watch this video: https://youtu.be/usp8Cza12N4
