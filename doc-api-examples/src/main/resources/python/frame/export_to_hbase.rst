Examples
--------

Overwrite/append scenarios (see below):

1. create a simple hbase table from csv
       load csv into a frame using existing frame api
       save the frame into hbase (it creates a table - lets call it table1)

2. overwrite existing table with new data
       do scenario 1 and create table1
       load the second csv into a frame
       save the frame into table1 (old data is gone)

3. append data to the existing table 1
       do scenario 1 and create table1
       load table1 into frame1
       load csv into frame2
       let frame1 = frame1 + frame2 (concatenate frame2 into frame1)
       save frame1 into base as table1 (overwrite with initial + appended data)


Vector scenarios (see below):

Vectors are not directly supported by HBase (which represents data as byte arrays) or the plugin.
While is true that a vector can be saved because of the byte array conversion for hbase, the following
is actually the recommended practice:

1. Convert the vector to csv (in python, outside ATK)
2. save the csv as string in the database (using ATK export_to_hbase)
3. read the cell as string (using ATK, read from hbase
4. convert the csv to vector (in python, outside ATK)


