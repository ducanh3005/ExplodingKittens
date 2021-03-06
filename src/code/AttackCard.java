
package code;

public class AttackCard extends Card implements Cloneable {
	public AttackCard() {
		this.cardID = 3;
	}

	public AttackCard(String path) {
		this();
		this.imagePath = path;
	}

	@Override
	public void cardAction(Player p1, Player p2) {
		TurnManager.getInstance().endTurnWithoutDrawForAttacks();
		TurnManager.getInstance().addTurnForCurrentPlayer();
	}

	@Override
	public Card clone() {
		return new AttackCard();
	}
}
