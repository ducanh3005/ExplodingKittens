package tests;

import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.Test;

import code.Card;
import code.CardStack;
import code.NormalCard;

public class CardStackTest {

	@Test
	public void testCardstackConstructor() {
		CardStack cardStack = CardStack.getInstance();
		
		CardStack.tearDown();
	}
	
	@Test
	public void testGetStack() {
		CardStack cardStack = CardStack.getInstance();
		
		assertTrue(cardStack.getStack() instanceof Stack<?>);
	}
	
	@Test
	public void testPeekStack() {
		CardStack cardStack = CardStack.getInstance();
		Stack<Card> testingStack = new Stack<Card>();
		NormalCard testingCard = new NormalCard();
		
		testingStack.push(testingCard);
		
		cardStack.setStack(testingStack);
		
		assertTrue(cardStack.peek() instanceof NormalCard);
	}
}