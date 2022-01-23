# Bullshit-Generator-using-NLP
A Maven project implementing the Stanford Core NLP 3.9.2 dependencies (in the "pom.xml" file). <br>
The "src/main/resources/text_input.txt" file contains text that serves as training material for the model. <br>
The "src/main/java/core/Pipeline.java" file creates a singleton instance of a StanfordCoreNLP pipeline and specifies the needed properties (lemmatization, pos, ...). <br>
The "src/main/java/test/Generator.java" file first computes Ngrams of length n from text. Given a word as input, the program next, generates a sentence starting the input word.

