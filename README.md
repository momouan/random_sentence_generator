# Bullshit-Generator-using-NLP
Generate a sentence given a random word using StanfordCoreNLP for Java
A Maven project implementing the Stanford Core NLP 3.9.2 dependencies (in the "pom.xml" file).
The "src/main/resources/text_input.txt" file contains text that serves as training material for the model.
The "src/main/java/core/Pipeline.java" file creates a singleton instance of a StanfordCoreNLP pipeline and specifies the needed properties (lemmatization, pos, ...).
The "src/main/java/test/Generator.java" file first computes Ngrams of length n from text. Given a word as input, the program next, generates a sentence starting the input word.
