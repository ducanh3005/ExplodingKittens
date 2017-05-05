
package code;

import java.util.ArrayList;
import java.util.List;

public class ScryCard extends Card {

	public List<Card> cardsToReveal = new ArrayList<>();// for testing purposes,
	                                                    // might remove after
	                                                    // GUI integration

	public ScryCard() {
		this.cardID = 7;
	}

	@Override
	public void cardAction(Player p1, Player p2) {
		int numberOfCardsToScry = 3;
		cardsToReveal = new ArrayList<>();// top card of deck is at
		                                  // index 0.
		MainDeck mainDeck = MainDeck.getInstance();
		List<Card> deckOrder = mainDeck.getCards();
		if (deckOrder.size() < numberOfCardsToScry)
			numberOfCardsToScry = deckOrder.size();
		for (int i = 0; i < numberOfCardsToScry; i++)
			cardsToReveal.add(deckOrder.get(i));
		// do something with cardsToReveal later, once GUI has been integrated.
	}

	@Override
	public ScryCard clone() {
		return new ScryCard();
	}
}
