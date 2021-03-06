
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import code.Card;
import code.CardFactory;
import code.CardStack;
import code.DiscardDeck;
import code.Game;
import code.MainDeck;
import code.Player;
import code.PriorityManager;
import code.TurnManager;
import exceptions.InvalidBundleException;
import exceptions.InvalidNumberofPlayersException;
import exceptions.NoCardsToMoveException;

public class DefuseCardTest {

	CardFactory factory;
	CardStack stack;
	Game game;
	MainDeck mDeck;
	DiscardDeck dDeck;

	@Before
	public void initialize() throws InvalidNumberofPlayersException {
		TurnManager.tearDown();
		PriorityManager.tearDown();
		CardStack.tearDown();
		MainDeck.tearDown();
		DiscardDeck.tearDown();
		factory = new CardFactory();
		stack = CardStack.getInstance();
		game = new Game();
		mDeck = MainDeck.getInstance();
		dDeck = DiscardDeck.getInstance();
		game.start(3);
	}

	@After
	public void tearDown() {
		TurnManager.tearDown();
		PriorityManager.tearDown();
		CardStack.tearDown();
		MainDeck.tearDown();
		DiscardDeck.tearDown();
	}

	@Test
	public void testDefusePlayedWithKittenOnStack() {
		Card kittenCard = factory.createCard(CardFactory.EXPLODING_KITTEN_CARD);
		Card defuseCard = factory.createCard(CardFactory.DEFUSE_CARD);
		stack.addCard(kittenCard);
		stack.addCard(defuseCard);

		stack.resolveTopCard();

		assertEquals(0, stack.getStack().size());
	}

	@Test
	public void testDefusePlayedNoKitten() {
		Card defuseCard = factory.createCard(CardFactory.DEFUSE_CARD);
		Card skipCard = factory.createCard(CardFactory.SKIP_CARD);
		stack.addCard(skipCard);
		stack.addCard(defuseCard);

		stack.resolveTopCard();

		assertEquals(1, stack.getStack().size());
	}

	@Test
	public void testDefuseEmptyStack() {
		Card defuseCard = factory.createCard(CardFactory.DEFUSE_CARD);
		stack.addCard(defuseCard);

		stack.resolveTopCard();

		assertEquals(0, stack.getStack().size());
	}

	@Test
	public void testKittenPutBackInDeck() {
		Card defuseCard = factory.createCard(CardFactory.DEFUSE_CARD);
		Card kittenCard = factory.createCard(CardFactory.EXPLODING_KITTEN_CARD);
		stack.addCard(kittenCard);
		stack.addCard(defuseCard);
		int deckSize = mDeck.getCardCount();
		int numberOfKittens = countKittensInDeck();

		stack.resolveTopCard();

		assertEquals(deckSize + 1, mDeck.getCardCount());
		assertEquals(numberOfKittens + 1, countKittensInDeck());
	}

	private int countKittensInDeck() {
		int count = 0;
		for (Card card : mDeck.getCards()) {
			if (card.getID() == 5) {
				count++;
			}
		}

		return count;
	}

	@Test
	public void testDefusePutInDiscardDeckWhenKittenPlayed() {
		Card defuseCard = factory.createCard(CardFactory.DEFUSE_CARD);
		Card kittenCard = factory.createCard(CardFactory.EXPLODING_KITTEN_CARD);
		stack.addCard(kittenCard);
		stack.addCard(defuseCard);
		int discardDeckSize = dDeck.getCardCount();

		stack.resolveTopCard();

		assertEquals(discardDeckSize + 1, dDeck.getCardCount());
		assertTrue(dDeck.getCards().get(0).getID() == 2);
	}

	@Test
	public void testDefuseClone() {
		Card defuse = factory.createCard(CardFactory.DEFUSE_CARD);

		Card clone = defuse.clone();

		assertFalse(clone == null);
	}

	@Test
	public void testDefuseBackToHand() throws NoCardsToMoveException, InvalidBundleException {
		Player player1 = TurnManager.getInstance().getCurrentPlayer();

		int cardIndex = -1;
		for (int i = 0; i < player1.getHand().size(); i++) {
			if (player1.getHand().get(i).getID() == CardFactory.DEFUSE_CARD) {
				cardIndex = i;
			}
		}
		player1.getHandManager().selectCard(cardIndex);
		player1.getHandManager().moveSelectedToStack();
		int numCardsInHandBefore = player1.getHand().size();

		stack.resolveTopCard();

		assertEquals(numCardsInHandBefore + 1, player1.getHand().size());
	}

	@Test
	public void testDefuseBackToHandStackNotEmpty() throws NoCardsToMoveException, InvalidBundleException {
		Player player1 = TurnManager.getInstance().getCurrentPlayer();
		CardFactory factory = new CardFactory();

		int cardIndex = -1;

		for (int i = 0; i < player1.getHand().size(); i++) {
			if (player1.getHand().get(i).getID() == CardFactory.DEFUSE_CARD) {
				cardIndex = i;
			}
		}

		stack.addCard(factory.createCard(CardFactory.ATTACK_CARD));

		player1.getHandManager().selectCard(cardIndex);
		player1.getHandManager().moveSelectedToStack();

		int numCardsInHandBefore = player1.getHand().size();

		stack.resolveTopCard();

		assertEquals(numCardsInHandBefore + 1, player1.getHand().size());
		assertTrue(player1.getHand().get(player1.getHand().size() - 1).getID() == CardFactory.DEFUSE_CARD);
	}
}
