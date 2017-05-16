
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import code.AttackCard;
import code.Card;
import code.CardStack;
import code.DefuseCard;
import code.DiscardDeck;
import code.FiveCardBundle;
import code.MainDeck;
import code.NormalCard;
import code.Player;
import code.PriorityManager;
import code.SkipCard;
import code.ThreeCardBundle;
import code.TurnManager;
import code.TwoCardBundle;
import exceptions.InvalidBundleException;

public class BundlesTest {
	
	@Before
	public void initialize() {
		TurnManager.tearDown();
		MainDeck.tearDown();
		DiscardDeck.tearDown();
		PriorityManager.tearDown();
		CardStack.tearDown();
	}
	
	@After
	public void tearDown() {
		TurnManager.tearDown();
		MainDeck.tearDown();
		DiscardDeck.tearDown();
		PriorityManager.tearDown();
		CardStack.tearDown();
	}

	@Test
	public void testCreateBundles() {

		ArrayList<Card> bundleTwo = new ArrayList<>(Arrays.asList(new NormalCard(), new NormalCard()));
		ArrayList<Card> bundleThree = new ArrayList<>(
		        Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard()));
		ArrayList<Card> bundleFive = new ArrayList<>(Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard(),
		        new NormalCard(), new NormalCard()));

		TwoCardBundle twoBundle = new TwoCardBundle(bundleTwo);
		ThreeCardBundle threeBundle = new ThreeCardBundle(bundleThree);
		FiveCardBundle fiveBundle = new FiveCardBundle(bundleFive);

		assertTrue(twoBundle instanceof TwoCardBundle);
		assertTrue(threeBundle instanceof ThreeCardBundle);
		assertTrue(fiveBundle instanceof FiveCardBundle);
	}

	@Test
	public void testTwoBundleIncorrectBundleSize() throws InvalidBundleException {

		ArrayList<Card> bundleWrong = new ArrayList<>(Arrays.asList(new NormalCard()));

		TwoCardBundle twoBundle = new TwoCardBundle(bundleWrong);

		assertTrue(!TwoCardBundle.isValidBundle(twoBundle.getCardsInBundle()));
	}

	@Test
	public void testThreeBundleIncorrectBundleSize() throws InvalidBundleException {

		ArrayList<Card> bundleWrong = new ArrayList<>(Arrays.asList(new NormalCard()));

		ThreeCardBundle threeBundle = new ThreeCardBundle(bundleWrong);

		assertTrue(!ThreeCardBundle.isValidBundle(threeBundle.getCardsInBundle()));
	}

	@Test
	public void testFiveBundleIncorrectBundleSize() throws InvalidBundleException {

		ArrayList<Card> bundleWrong = new ArrayList<>(Arrays.asList(new NormalCard()));

		FiveCardBundle fiveBundle = new FiveCardBundle(bundleWrong);

		assertTrue(!FiveCardBundle.isValidBundle(fiveBundle.getCardsInBundle()));
	}

	@Test
	public void testIncorrectBundleCards() throws InvalidBundleException {

		ArrayList<Card> bundleWrong = new ArrayList<>(Arrays.asList(new AttackCard(), new NormalCard()));

		TwoCardBundle twoBundle = new TwoCardBundle(bundleWrong);
		ThreeCardBundle threeBundle = new ThreeCardBundle(bundleWrong);
		FiveCardBundle fiveBundle = new FiveCardBundle(bundleWrong);

		assertTrue(!TwoCardBundle.isValidBundle(twoBundle.getCardsInBundle()));
		assertTrue(!ThreeCardBundle.isValidBundle(threeBundle.getCardsInBundle()));
		assertTrue(!FiveCardBundle.isValidBundle(fiveBundle.getCardsInBundle()));
	}

	@Test
	public void testTwoCardNullBundleCards() throws InvalidBundleException {
		TwoCardBundle twoBundle = new TwoCardBundle(null);

		assertTrue(!TwoCardBundle.isValidBundle(twoBundle.getCardsInBundle()));
	}

	@Test
	public void testThreeCardNullBundleCards() throws InvalidBundleException {
		ThreeCardBundle threeBundle = new ThreeCardBundle(null);

		assertTrue(!ThreeCardBundle.isValidBundle(threeBundle.getCardsInBundle()));
	}

	@Test
	public void testFiveCardNullBundleCards() throws InvalidBundleException {
		FiveCardBundle fiveBundle = new FiveCardBundle(null);

		assertTrue(!FiveCardBundle.isValidBundle(fiveBundle.getCardsInBundle()));
	}

	@Test
	public void testTwoCardClone() throws InvalidBundleException {
		ArrayList<Card> list = new ArrayList<>(Arrays.asList(new NormalCard(), new NormalCard()));

		TwoCardBundle twoBundle = new TwoCardBundle(list);

		TwoCardBundle clone = twoBundle.clone();

		assertTrue(TwoCardBundle.isValidBundle(twoBundle.getCardsInBundle()));
		assertTrue(TwoCardBundle.isValidBundle(clone.getCardsInBundle()));
		assertTrue(clone instanceof TwoCardBundle);

	}

	@Test
	public void testThreeCardClone() {
		ArrayList<Card> list = new ArrayList<>(Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard()));
		ThreeCardBundle threeBundle = new ThreeCardBundle(list);

		ThreeCardBundle clone = threeBundle.clone();

		assertTrue(ThreeCardBundle.isValidBundle(threeBundle.getCardsInBundle()));
		assertTrue(ThreeCardBundle.isValidBundle(clone.getCardsInBundle()));
		assertTrue(clone instanceof ThreeCardBundle);
	}

	@Test
	public void testFiveCardClone() {
		ArrayList<Card> list = new ArrayList<>(Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard(),
		        new NormalCard(), new NormalCard()));
		FiveCardBundle fiveBundle = new FiveCardBundle(list);

		FiveCardBundle clone = fiveBundle.clone();

		assertTrue(FiveCardBundle.isValidBundle(fiveBundle.getCardsInBundle()));
		assertTrue(FiveCardBundle.isValidBundle(clone.getCardsInBundle()));
		assertTrue(clone instanceof FiveCardBundle);
	}

	@Test
	public void testIsValidBundleNotNormalCards() {
		ArrayList<Card> twoList = new ArrayList<>(Arrays.asList(new NormalCard(), new AttackCard()));
		ArrayList<Card> threeList = new ArrayList<>(
		        Arrays.asList(new NormalCard(), new NormalCard(), new AttackCard()));
		ArrayList<Card> fiveList = new ArrayList<>(Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard(),
		        new NormalCard(), new AttackCard()));

		TwoCardBundle twoBundle = new TwoCardBundle(twoList);
		ThreeCardBundle threeBundle = new ThreeCardBundle(threeList);
		FiveCardBundle fiveBundle = new FiveCardBundle(fiveList);

		assertFalse(TwoCardBundle.isValidBundle(twoBundle.getCardsInBundle()));
		assertFalse(ThreeCardBundle.isValidBundle(threeBundle.getCardsInBundle()));
		assertFalse(FiveCardBundle.isValidBundle(fiveBundle.getCardsInBundle()));
	}

	@Test
	public void testIsValidBundleBadIcons() {
		NormalCard firstCard = new NormalCard();
		NormalCard secondCard = new NormalCard();
		NormalCard thirdCard = new NormalCard();
		NormalCard fourthCard = new NormalCard();
		NormalCard fifthCard = new NormalCard();

		firstCard.setIcon(1);
		secondCard.setIcon(2);
		thirdCard.setIcon(3);
		fourthCard.setIcon(4);
		fifthCard.setIcon(5);

		TwoCardBundle twoBundle = new TwoCardBundle(Arrays.asList(firstCard, secondCard));
		ThreeCardBundle threeBundle = new ThreeCardBundle(Arrays.asList(firstCard, secondCard, thirdCard));
		FiveCardBundle fiveBundle = new FiveCardBundle(
		        Arrays.asList(firstCard, secondCard, thirdCard, fourthCard, fifthCard));

		assertFalse(TwoCardBundle.isValidBundle(twoBundle.getCardsInBundle()));
		assertFalse(ThreeCardBundle.isValidBundle(threeBundle.getCardsInBundle()));
		assertFalse(FiveCardBundle.isValidBundle(fiveBundle.getCardsInBundle()));

	}

	@Test
	public void testTwoBundleCardAction() {
		Player player1 = new Player();
		Player player2 = new Player();
		TwoCardBundle twoBundle = new TwoCardBundle(Arrays.asList(new NormalCard(), new NormalCard()));
		twoBundle.setTargetCardIndex(0);

		player2.addDefuseCardToHand();
		twoBundle.cardAction(player1, player2);

		assertEquals(1, player1.getHand().size());
		assertEquals(0, player2.getHand().size());
		assertTrue(player1.getHand().get(0) instanceof DefuseCard);
	}

	@Test
	public void testThreeBundleCardAction() {
		Player player1 = new Player();
		Player player2 = new Player();
		ThreeCardBundle threeBundle = new ThreeCardBundle(
		        Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard()));
		threeBundle.setTargetCardClass(DefuseCard.class);

		player2.addDefuseCardToHand();
		threeBundle.cardAction(player1, player2);

		assertEquals(1, player1.getHand().size());
		assertEquals(0, player2.getHand().size());
		assertTrue(player1.getHand().get(0) instanceof DefuseCard);
	}

	@Test
	public void testThreeBundleCardActionEmptyHand() {
		Player player1 = new Player();
		Player player2 = new Player();
		ThreeCardBundle threeBundle = new ThreeCardBundle(
		        Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard()));
		threeBundle.setTargetCardClass(DefuseCard.class);

		threeBundle.cardAction(player1, player2);

		assertEquals(0, player1.getHand().size());
		assertEquals(0, player2.getHand().size());
	}

	@Test
	public void testThreeBundleCardActionWrongType() {
		Player player1 = new Player();
		Player player2 = new Player();
		ThreeCardBundle threeBundle = new ThreeCardBundle(
		        Arrays.asList(new NormalCard(), new NormalCard(), new NormalCard()));
		threeBundle.setTargetCardClass(SkipCard.class);

		player2.addDefuseCardToHand();
		threeBundle.cardAction(player1, player2);

		assertEquals(0, player1.getHand().size());
		assertEquals(1, player2.getHand().size());
		assertTrue(player2.getHand().get(0) instanceof DefuseCard);
	}

	@Test
	public void testFiveBundleCardAction() {
		Player player1 = new Player();
		FiveCardBundle fiveBundle = new FiveCardBundle(Arrays.asList(new NormalCard(), new NormalCard(),
		        new NormalCard(), new NormalCard(), new NormalCard()));
		fiveBundle.setDiscardDeckIndex(0);
		DiscardDeck deck = DiscardDeck.getInstance();
		deck.addCard(new AttackCard());
		
		fiveBundle.cardAction(player1, null);
		
		assertEquals(1, player1.getHand().size());
		assertEquals(0, deck.getCardCount());
		assertTrue(player1.getHand().get(0) instanceof AttackCard);
	}

}
