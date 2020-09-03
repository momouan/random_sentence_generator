package test;

//*
// Bullshit Generator using StanfordCoreNLP 
//*

import core.Pipeline;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.CoreDocument;
import edu.stanford.nlp.pipeline.CoreSentence;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;


public class Generator {

    // Computes ngrams of length n from text
    // Returns a HashMap with the ngrams as keys and their frequencies as values
    public static HashMap<String, Integer> ngrams(String text, int n) {
        ArrayList<String> words = new ArrayList<String>();
        for(String word : text.split(" ")) {
            words.add(word);
        }

        HashMap<String, Integer> map = new HashMap<String, Integer>();

        int c = words.size();
        for(int i = 0; i < c; i++) {
            if((i + n - 1) < c) {
                int stop = i + n;
                String ngramWords = words.get(i);

                for(int j = i + 1; j < stop; j++) {
                    ngramWords +=" "+ words.get(j);
                }
                map.merge(ngramWords, 1, Integer::sum);
            }
        }

        return map;
    }

    //Reads the content of a file and returns it as a string
    public static String readFile(String fileName)throws Exception
    {
        String data = "";
        try
        {
            data = new String(Files.readAllBytes(Paths.get(fileName)));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }

    //Returns a string containing the text lemmatized
    public static String lemmatize(String text){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabelList = coreDocument.tokens();
        String res = "";
        for (CoreLabel coreLabel : coreLabelList){
            res += coreLabel.lemma() + " ";
        }
        return res;
    }

    //Returns a string containing the POS of the text
    public static String POS(String text){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreLabel> coreLabelList = coreDocument.tokens();
        String res = "";
        int i = 0;
        for (CoreLabel coreLabel : coreLabelList){
            res += coreLabel.get(CoreAnnotations.PartOfSpeechAnnotation.class) + " ";
        }
        return res;
    }

    //Returns a list of String.sentences lemmatized
    public static List<String> sentence_lemmatization(String text){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();
        List<String> res = new ArrayList<String>();
        for (CoreSentence sentence : sentences){
            res.add(lemmatize(sentence.toString()));
        }
        return res;
    }

    //Returns a list containing the POS as String.sentences
    public static List<String> sentence_POS(String text){
        StanfordCoreNLP stanfordCoreNLP = Pipeline.getPipeline();
        CoreDocument coreDocument = new CoreDocument(text);
        stanfordCoreNLP.annotate(coreDocument);
        List<CoreSentence> sentences = coreDocument.sentences();
        List<String> res = new ArrayList<String>();
        for (CoreSentence sentence : sentences){
            res.add(POS(sentence.toString()));
        }
        return res;
    }


    //Lunching method
    public static void main(String []args){

        //Fixing the input word
        String word = "world";
        //Fixing the ngams length
        int ngrams = 3;
        //----------------------
        String text = "";
        StanfordCoreNLP stanfordCoreNLP;
        CoreDocument coreDocument;
    	System.out.println("Ngrams: ");
        try {
            //Opening the dictionnary , source file
            text = readFile("src\\main\\resources\\text_input.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //Creating a list of the file's sentences lemmatized
        List<String> sentence_lemma = sentence_lemmatization(text);
        //List<String> sentence_pos = sentence_POS(text);

        //Storing the content of the list in a String.txt
        text = "";
        for (String string : sentence_lemma){
            text = text + string.toString();
        }

        //Creating a hash map of ngrams of the String.text variable of length = ngrams
        HashMap<String, Integer> res = ngrams(text, ngrams);
        //Sorting the HashMap and Copying the result to the sorted variable
        Map<String, Integer> sorted = res
                .entrySet()
                .stream()
                .sorted(comparingByValue())
                .collect(
                        Collectors.toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,
                                LinkedHashMap::new));

        String result = "";
        String[] key;
        //String pos = "";
        int max;
        //Computing loop
        do {
            //We look for the ngram with the highest frequency
            max = 0;
            for(Map.Entry<String, Integer> entry : sorted.entrySet()){
                 key = entry.getKey().split(" ");
                if(key[0].equals(word) && entry.getValue() > max){
                    max = entry.getValue();
                }
            }
            if(max != 0){
                //We start our sentence with the input word
                result += word + " ";
                //Creating a list of possibilities
                List<String> possibilities = new ArrayList<String>();
                for(Map.Entry<String, Integer> entry : sorted.entrySet()){
                    key = entry.getKey().split(" ");
                    String possibility = "";
                    //We add the different possibilities to the list
                    if(key[0].equals(word) && entry.getValue() == max){
                        for(int i = 0; i < ngrams - 1; i++){
                            possibility += key[i] + "  ";
                        }
                        possibility += key[ngrams - 1] + " " + max;
                        possibilities.add(possibility);
                    }
                }
                //In case we have different routes with the same frequency
                //We generate one randomly
                if(possibilities.size() > 1){
                    Random random = new Random();
                         //pos = possibilities.get(random.nextInt(possibilities.size()));
                         //pos = POS(pos);
                         key = possibilities.get(random.nextInt(possibilities.size())).split(" ");
                }
                //One way possibility
                else {
                     //pos = possibilities.get(0);
                     //pos = POS(pos);
                     key = possibilities.get(0).split(" ");
                }
                //We add the second word to the sentence
                word = key[2];
            }
            //Ends if no path is found and if the next word is a full stop
            } while(!word.equals(".") && max != 0);
            //Prints the result to the screen
            System.out.println(result + ".");
        }
}