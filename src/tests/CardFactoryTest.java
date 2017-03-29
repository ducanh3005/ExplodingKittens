
package tests;

import static org.junit.Assert.*;

import java.awt.List;

import org.junit.Test;

import code.*;

public class CardFactoryTest {
	@Test
	public void testCardFactoryCreation() {
		@SuppressWarnings("unused")
		CardFactory cardFactory = new CardFactory();
	}

	@Test
	public void testCreateCardSingleNormal() {
		CardFactory cardFactory = new CardFactory();

		Card card = cardFactory.createCard(CardFactory.NORMAL_CARD);
		assertTrue(NormalCard.class.isInstance(card));
	}

	@Test
	public void testCreateCardMultipleNormal() {
		CardFactory cardFactory = new CardFactory();
		
		List<Card> cards = cardFactory.createCard(CardFactory.NORMAL_CARD, 10);
		
		for (Card card: cards) {
			assertTrue(NormalCard.class.isInstance(card));
		}
		assertEquals(10, cards.size());
	}
}
