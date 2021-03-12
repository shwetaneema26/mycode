package com.abc.wordcounter;
import com.abc.wordcounter.exception.InvalidWordException;
import com.abc.wordcounter.translator.EnglishTranslator;
import com.abc.wordcounter.impl.WordCounter;
import org.junit.*;
import org.junit.Test;
import java.util.Arrays;
import static org.junit.Assert.*;

public class WordCounterTest {
    private String[] inputArray;
    private WordCounter wordCounter;
    @Before
    public void setup() {
        inputArray = new String[]{"This", "Flower", "flow", "This", "Flower"};
        wordCounter = new WordCounter(new EnglishTranslator());
    }
    @Test
    public void testAddValidWord(){
        Arrays.stream(inputArray).forEach(word-> {
            try {
                wordCounter.addWord(word);
            } catch (InvalidWordException e) {
            }
        });
        assertEquals(2,wordCounter.getCount("This"));
        assertEquals(2,wordCounter.getCount("Flower"));
        assertEquals(1,wordCounter.getCount("flow"));
    }
    @Test(expected = InvalidWordException.class)
    public void testAddInValidWord() throws InvalidWordException {
        wordCounter.addWord("Sh12!*m");

    }
    @Test(expected = InvalidWordException.class)
    public void testAddNull() throws InvalidWordException {
        wordCounter.addWord(null);
    }
    @After
    public void tearDown() throws Exception {
        wordCounter = null;
        inputArray = null;
    }
}

