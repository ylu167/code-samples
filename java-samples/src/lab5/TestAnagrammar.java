package lab5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestAnagrammar {
	static Anagrammar ag;

	@BeforeAll
	public static void setupBeforeClass() {
		ag = new Anagrammar();
		ag.loadWords("words.txt");
	}
	
	@BeforeEach
	public void setupTest() {
		ag.isInDictionary = false;
		ag.hasAnagrams = false;
		
	}

	@Test
	public void testInDictionaryNoAnagram() {
		ag.findAnagrams("complex");
		assertEquals("Test in dictionary", true, ag.isInDictionary);
		assertEquals("Test has anagrams", false, ag.hasAnagrams);
	}
	
	@Test
	public void testInDictionaryHasAnagram() {
		ag.findAnagrams("free");
		assertEquals("Test in dictionary", true, ag.isInDictionary);
		assertEquals("Test has anagrams", true, ag.hasAnagrams);
	}

	@Test
	public void testNotInDictionaryHasAnagram() {
		ag.findAnagrams("abc");
		assertEquals("Test in dictionary", false, ag.isInDictionary);
		assertEquals("Test has anagrams", true, ag.hasAnagrams);
	}
	
	@Test
	public void testNotInDictionaryNoAnagram() {
		ag.findAnagrams("xyz");
		assertEquals("Test in dictionary", false, ag.isInDictionary);
		assertEquals("Test has anagrams", false, ag.hasAnagrams);
	}
	
	@Test
	public void testWordHasAnagrams() {
		ag.findAnagrams("rope");
		assertEquals(1, ag.anagramArray.length);
	}
	
	@Test
	public void testWordNoAnagrams() {
		ag.findAnagrams("xyz");
		assertEquals(0, ag.anagramArray.length);
	}
	
	@Test
	public void testWordHasAnagramsContent() {
		ag.findAnagrams("free");
		assertTrue(Arrays.asList(ag.anagramArray).contains("reef"));
		assertTrue(Arrays.asList(ag.anagramArray).contains("feer"));
	}
}
