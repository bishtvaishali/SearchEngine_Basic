
- The Program is written in java:
Version: Java8

- library: KrovetzStemmer-3.4
It can be downloaded from below link:
https://sourceforge.net/projects/lemur/files/lemur/KrovetzStemmer-3.4/KrovetzStemmer-3.4.tar.gz/download

- I created this project in Eclipse
-After downloading the library. Right click on the project>Build Path>Configure Build Path>Add external jars. 
Now browse the downloaded stemmer jar file. Click open.
-Your file will appear in the Referenced libraries folder.

RUN INSTRUCTIONS:
-Put the "documents.txt" file in the current directory. The program will automatically look for the file in the current directory.
Part1 will create the Inverted Index.
Part2 will intersect the posting lists of two terms in query.
Part 3:
-The 3 queries which are to be processed are in the main method of the program.
-In the beginning of the program, It will first invoke part1 to create Inverted Index. 
-Then it will write the Inverted index to a file "invertedIdx.txt" in your current directory.
- After this, it will load the inverted index from file "invertedIdx.txt" in memory i.e by storing in a map.
-Now the query will be evaluated by Part2 of the program. It will fetch posting lists from the map and intersect.
- Finally, the results will be stored in a file "results.txt" in the current directory. Also, the results are displayed in the console.

Format of file containing Inverted Index:
Term, Doc Freq, [Posting list]
Example: about,2,[2, 6]

 
