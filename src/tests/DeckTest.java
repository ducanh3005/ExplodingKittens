package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import code.Deck;
import code.Card;

public class DeckTest {

	@Test
	public void testDeckConstructor() {
		Deck deck = new Deck();
	}
	
	@Test
	public void testGetCards() {
		List<Card> cards = new ArrayList<Card>();
		Deck deck = new Deck(cards);
		
		assertEquals(cards, deck.getCards());
	}
	
	@Test
	public void testGetCardsWithNonEmpty() {
		List<Card> cards = new ArrayList<Card>();
		Card cardOne = new Card();
		cards.add(cardOne);
		Deck deck = new Deck(cards);
		
		assertEquals(cards, deck.getCards());
	}
	
	@Test
	public void testSetCards() {
		List<Card> cards = new ArrayList<Card>();
		Deck deck = new Deck();
		
		deck.setCards(cards);
		
		assertEquals(cards, deck.getCards());
	}
	
	@Test
	public void testSetCardsWithNonEmpty() {
		List<Card> cards = new ArrayList<Card>();
		Card cardOne = new Card();
		cards.add(cardOne);
		Deck deck = new Deck();
		
		deck.setCards(cards);
		
		assertEquals(cards, deck.getCards());
	}
	
	@Test
	public void testAddCardToZeroElements() {
		Deck deck = new Deck();
		Card firstCard = new Card();
		
		deck.addCard(firstCard, 1);
		
		assertEquals(1, deck.getCards().size());
		assertEquals(firstCard, deck.getCards().get(0));
	}

}
