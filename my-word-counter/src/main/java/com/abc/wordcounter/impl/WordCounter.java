package com.abc.wordcounter.impl;

import com.abc.wordcounter.exception.InvalidWordException;
import com.abc.wordcounter.translator.EnglishTranslator;
import com.abc.wordcounter.translator.Translator;
import java.util.concurrent.*;
import java.util.concurrent.atomic.LongAdder;
/**
 * *This class maintains frequency of input words in any language(if a word is given in a language
 other than english it
 * translates it to english first and then store its frequency) in a threadsafe way.
 *
 * @author shwetaNeema
 */
public class WordCounter {
    private final ConcurrentMap<String, LongAdder> map;
    private final Translator translator;
    public WordCounter(final Translator translator){
        map = new ConcurrentHashMap<>();
        this.translator = translator;
    }
    /**
     * Method to store frequency of input word or throw UnsupportedWordException if input word is non
     alphabatic.
     *
     * @param word
     * @throws InvalidWordException
     */
    public void addWord(final String word) throws InvalidWordException {
        if (word != null && word.chars().allMatch(Character::isLetter)) {
            map.computeIfAbsent(translator.translate(word), k -> new LongAdder()).increment();
        } else {
            throw new InvalidWordException("Only supported words are alphanumeric " + word);
        }
    }
    /**
     * Method to get frequency of a given word added to wordCounter.
     *
     * @param word
     * @return
     */
    public long getCount(final String word) {
        if (word != null) {
            return map.getOrDefault(word,new LongAdder()).longValue();
        }
        return 0;
    }
    public static void main(String[] args) {

        String[] inputArray = {"This", "this", "Flower", "T7!", "flow", "This", "T7!", "Flower", "T7!"};
        WordCounter counter = new WordCounter(new EnglishTranslator());
        ExecutorService executor = Executors.newFixedThreadPool(3);

        Runnable addRunnable = ()->{
            for(String s:inputArray){
                System.out.println("adding "+s);
                try {
                    counter.addWord(s);
                } catch (InvalidWordException e) {
                    e.printStackTrace();
                }
                System.out.println("Done adding");
            }
        };

        Runnable getCountRunnable = ()->{
            System.out.println("getting count");

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(counter.getCount("This"));
        };

        executor.submit(addRunnable);
        executor.submit(addRunnable);
        executor.submit(getCountRunnable);

        executor.shutdown();
        try {
            executor.awaitTermination(1000, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.out.println("Thread Pool interrupted!");
            System.exit(1);
        }
    }
}