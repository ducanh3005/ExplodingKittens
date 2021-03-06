
package code;

public class NopeCard extends Card implements Cloneable {
	public NopeCard() {
		this.cardID = 1;
	}

	public NopeCard(String path) {
		this();
		this.imagePath = path;
	}

	@Override
	public void cardAction(Player p1, Player p2) {
		CardStack.getInstance().counterTopCard();
	}

	@Override
	public NopeCard clone() {
		return new NopeCard();
	}

}
