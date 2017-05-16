
package code;

import java.util.Stack;

public class DefuseCard extends Card implements Cloneable {

	CardStack stack;
	TurnManager tm;
	MainDeck mDeck;
	CardFactory factory;
	DiscardDeck dDeck;

	public DefuseCard() {
		this.cardID = 2;
	}

	public DefuseCard(String path) {
		this();
		this.imagePath = path;
	}

	@Override
	public void cardAction(Player p1, Player p2) {
		stack = CardStack.getInstance();
		tm = TurnManager.getInstance();
		mDeck = MainDeck.getInstance();
		factory = new CardFactory();
		dDeck = DiscardDeck.getInstance();

		if (stack.getStack().isEmpty()) {
			tm.getCurrentPlayer().addDefuseCardToHand();
			return;
		}

		if (stack.getStack().elementAt(0) instanceof ExplodingKittenCard) {
			stack.setStack(new Stack<Card>());

			// Here is where the user will need to decide where to put the
			// Exploding kitten card. For now, it will be placed onto the
			// bottom of the deck.
			mDeck.insertCard(factory.createCard(CardFactory.EXPLODING_KITTEN_CARD), mDeck.getCardCount() - 1);
			dDeck.addCard(factory.createCard(CardFactory.DEFUSE_CARD));
		} else {
			tm.getCurrentPlayer().addDefuseCardToHand();
		}
	}

	@Override
	public DefuseCard clone() {
		return new DefuseCard();
	}
}
