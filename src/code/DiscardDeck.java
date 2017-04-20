package code;

import java.util.List;

import exceptions.EmptyDiscardDeckException;

public class DiscardDeck {

	private static DiscardDeck discardDeck;
	
	Deck deck;

	public static DiscardDeck getInstance() {
		if (discardDeck == null) {
			discardDeck = new DiscardDeck();
		}
		return discardDeck;
	}
	
	private DiscardDeck() {
		deck = new Deck();
	}

	public static void tearDown() {
		discardDeck = null;
	}

	public List<Card> getCards() {
		// TODO Auto-generated method stub
		return discardDeck.deck.getCards();
	}

	public int getCardCount() {
		// TODO Auto-generated method stub
		return discardDeck.deck.getCards().size();
	}

	public Card removeCard(Class<?> cardType) {
		Card retCard = null;
		if (discardDeck.getCardCount() == 0) {
			throw new EmptyDiscardDeckException("Discard Deck is empty");
		}
		for (Card card : discardDeck.getCards()) {
			if (card.getClass().equals(cardType)) {
				retCard = card;
				discardDeck.deck.removeCard(retCard);
				return retCard;
			}
		}
		
		return retCard;
	}

}
