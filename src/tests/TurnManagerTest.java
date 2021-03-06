
package tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import code.Card;
import code.CardFactory;
import code.CardStack;
import code.DiscardDeck;
import code.Game;
import code.Log;
import code.MainDeck;
import code.Player;
import code.PlayerManager;
import code.PriorityManager;
import code.TurnManager;
import exceptions.InvalidBundleException;
import exceptions.InvalidNumberofPlayersException;
import exceptions.NoCardsToMoveException;

public class TurnManagerTest {

	@Before
	public void initialize() {
		TurnManager.tearDown();
		MainDeck.tearDown();
		DiscardDeck.tearDown();
		PriorityManager.tearDown();
		CardStack.tearDown();
		Log.tearDown();
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
	public void testConstruction() {
		@SuppressWarnings("unused")
		TurnManager manager = TurnManager.getInstance();
	}

	@Test
	public void testHandlesPlayerManager() {
		PlayerManager pmgr = EasyMock.mock(PlayerManager.class);
		List<Player> players = new ArrayList<>();
		players.add(new Player());
		EasyMock.expect(pmgr.getPlayers()).andReturn(players);
		EasyMock.replay(pmgr);
		TurnManager manager = TurnManager.getInstance();
		manager.setPlayerManager(pmgr);
		assertEquals(pmgr, manager.getPlayerManager());
		EasyMock.verify(pmgr);
	}

	@Test
	public void testHandlesPlayers() {
		ArrayList<Player> players = new ArrayList<>();
		Player mockPlayer1 = EasyMock.mock(Player.class);
		Player mockPlayer2 = EasyMock.mock(Player.class);
		Player mockPlayer3 = EasyMock.mock(Player.class);
		players.add(mockPlayer1);
		players.add(mockPlayer2);
		players.add(mockPlayer3);
		PlayerManager mockPM = EasyMock.mock(PlayerManager.class);
		TurnManager manager = TurnManager.getInstance();

		EasyMock.expect(mockPM.getPlayers()).andReturn(players);

		EasyMock.replay(mockPM, mockPlayer1, mockPlayer2, mockPlayer3);

		manager.setPlayerManager(mockPM);

		EasyMock.verify(mockPM, mockPlayer1, mockPlayer2, mockPlayer3);

		assertEquals(mockPlayer1, manager.getCurrentPlayer());
	}

	@Test
	public void testEndTurnWithoutDraw() {
		ArrayList<Player> players = new ArrayList<>();
		Player mockPlayer1 = EasyMock.mock(Player.class);
		Player mockPlayer2 = EasyMock.mock(Player.class);
		Player mockPlayer3 = EasyMock.mock(Player.class);
		players.add(mockPlayer1);
		players.add(mockPlayer2);
		players.add(mockPlayer3);
		PlayerManager mockPM = EasyMock.mock(PlayerManager.class);
		TurnManager manager = TurnManager.getInstance();

		EasyMock.expect(mockPM.getPlayers()).andReturn(players);

		EasyMock.replay(mockPM, mockPlayer1, mockPlayer2, mockPlayer3);

		manager.setPlayerManager(mockPM);
		assertEquals(mockPlayer1, manager.getCurrentPlayer());
		manager.endTurnWithoutDraw();
		assertEquals(mockPlayer2, manager.getCurrentPlayer());
		manager.endTurnWithoutDraw();
		assertEquals(mockPlayer3, manager.getCurrentPlayer());
		manager.endTurnWithoutDraw();
		assertEquals(mockPlayer1, manager.getCurrentPlayer());

		EasyMock.verify(mockPM, mockPlayer1, mockPlayer2, mockPlayer3);
	}

	@Test
	public void testEndTurnAndDraw() throws NoCardsToMoveException, InvalidBundleException {
		ArrayList<Player> players = new ArrayList<>();
		Card mockCard = EasyMock.mock(Card.class);
		Player mockPlayer1 = EasyMock.mock(Player.class);
		Player mockPlayer2 = EasyMock.mock(Player.class);
		Player mockPlayer3 = EasyMock.mock(Player.class);
		players.add(mockPlayer1);
		players.add(mockPlayer2);
		players.add(mockPlayer3);
		PlayerManager mockPM = EasyMock.mock(PlayerManager.class);
		TurnManager manager = TurnManager.getInstance();

		EasyMock.expect(mockPM.getPlayers()).andReturn(players);
		EasyMock.expect(mockPlayer1.drawCard()).andReturn(mockCard);
		EasyMock.expect(mockCard.getID()).andReturn(0);
		EasyMock.expect(mockPlayer2.drawCard()).andReturn(mockCard);
		EasyMock.expect(mockCard.getID()).andReturn(0);
		EasyMock.expect(mockPlayer3.drawCard()).andReturn(mockCard);
		EasyMock.expect(mockCard.getID()).andReturn(0);

		EasyMock.replay(mockPM, mockPlayer1, mockPlayer2, mockPlayer3, mockCard);

		manager.setPlayerManager(mockPM);
		assertEquals(mockPlayer1, manager.getCurrentPlayer());
		manager.endTurnAndDraw();
		assertEquals(mockPlayer2, manager.getCurrentPlayer());
		manager.endTurnAndDraw();
		assertEquals(mockPlayer3, manager.getCurrentPlayer());
		manager.endTurnAndDraw();
		assertEquals(mockPlayer1, manager.getCurrentPlayer());

		EasyMock.verify(mockPM, mockPlayer1, mockPlayer2, mockPlayer3, mockCard);
	}

	@Test
	public void testEndTurnAndDrawWithKittenOnTop()
	        throws InvalidNumberofPlayersException, NoCardsToMoveException, InvalidBundleException {
		CardFactory factory = new CardFactory();
		MainDeck mainDeck = MainDeck.getInstance();
		Game game = new Game();
		game.start(3);
		TurnManager turnManager = TurnManager.getInstance();

		mainDeck.insertCard(factory.createCard(CardFactory.EXPLODING_KITTEN_CARD), 0);
		for (Card card : turnManager.getCurrentPlayer().getHand()) {
			if (card.getID() == CardFactory.DEFUSE_CARD) {
				turnManager.getCurrentPlayer().getHandManager()
				        .selectCard((turnManager.getCurrentPlayer().getHand().indexOf(card)));
				turnManager.getCurrentPlayer().getHandManager().clearSelectedCards();
				break;
			}
		}
		turnManager.endTurnAndDraw();

		assertEquals(2, game.getPlayers().size());

	}

	@Test
	public void testDrawKittenWithDefuse()
	        throws InvalidNumberofPlayersException, NoCardsToMoveException, InvalidBundleException {
		CardFactory factory = new CardFactory();
		MainDeck mainDeck = MainDeck.getInstance();
		Game game = new Game();
		game.start(3);
		TurnManager turnManager = TurnManager.getInstance();
		mainDeck.insertCard(factory.createCard(CardFactory.EXPLODING_KITTEN_CARD), 0);
		int discardDeckSize = DiscardDeck.getInstance().getCardCount();
		Player player = turnManager.getCurrentPlayer();
		int currentHandSize = player.getHand().size();

		turnManager.endTurnAndDraw();

		assertEquals(3, game.getPlayers().size());
		assertEquals(discardDeckSize + 1, DiscardDeck.getInstance().getCardCount());
		assertEquals(currentHandSize - 1, player.getHand().size());
	}

	@Test
	public void testPlayerDoesNotRotateOnAttack()
	        throws InvalidNumberofPlayersException, NoCardsToMoveException, InvalidBundleException {
		CardFactory factory = new CardFactory();
		MainDeck mainDeck = MainDeck.getInstance();
		Game game = new Game();
		game.start(3);
		TurnManager turnManager = TurnManager.getInstance();
		Player player1 = game.getPlayers().get(0);

		mainDeck.insertCard(factory.createCard(CardFactory.NORMAL_CARD), 0);
		turnManager.addTurnForCurrentPlayer();
		turnManager.endTurnAndDraw();

		// Player 1's turn again.
		turnManager.endTurnWithoutDraw();
		// Player 2's turn.
		turnManager.endTurnWithoutDraw();
		// Player 3's turn.
		turnManager.endTurnWithoutDraw();
		// Player 1's turn.
		turnManager.endTurnWithoutDraw();

		assertEquals(player1, turnManager.getCurrentPlayer());
	}

	@Test
	public void testGameOver() throws InvalidNumberofPlayersException, NoCardsToMoveException, InvalidBundleException {
		CardFactory factory = new CardFactory();
		Game game = new Game();
		game.start(3);
		PriorityManager priorityManager = PriorityManager.getInstance();
		MainDeck mainDeck = MainDeck.getInstance();
		TurnManager turnManager = TurnManager.getInstance();
		Player player3 = game.getPlayers().get(2);
		mainDeck.insertCard(factory.createCard(CardFactory.NORMAL_CARD), 0);
		mainDeck.insertCard(factory.createCard(CardFactory.NORMAL_CARD), 0);
		mainDeck.insertCard(factory.createCard(CardFactory.NORMAL_CARD), 0);
		ByteArrayOutputStream os = new ByteArrayOutputStream(100);
		PrintStream capture = new PrintStream(os);
		System.setOut(capture);

		turnManager.makeCurrentPlayerLose();
		turnManager.endTurnAndDraw();
		turnManager.makeCurrentPlayerLose();
		turnManager.endTurnAndDraw();
		capture.flush();
		String result = os.toString();
		result = result.trim();

		assertEquals(1, priorityManager.getPlayerCount());
		assertEquals(player3, turnManager.getCurrentPlayer());
		assertEquals(0, turnManager.getTurnOrder().size());
		assertEquals("Game over!", result);
	}

	@Test
	public void testSetPlayerManagerPlayerAlreadyExists() throws InvalidNumberofPlayersException {
		Game game = new Game();
		game.start(3);
		TurnManager turnManager = TurnManager.getInstance();
		Player currentPlayer = turnManager.getCurrentPlayer();

		turnManager.setPlayerManager(new PlayerManager());

		assertEquals(currentPlayer, turnManager.getCurrentPlayer());
	}

	@Test
	public void testCardsClearedWhenKittenDrawn()
	        throws InvalidNumberofPlayersException, NoCardsToMoveException, InvalidBundleException {
		CardFactory factory = new CardFactory();
		Game game = new Game();
		game.start(3);
		Player player1 = game.getCurrentPlayer();
		MainDeck.getInstance().insertCard(factory.createCard(CardFactory.EXPLODING_KITTEN_CARD), 0);
		player1.getHand().add(factory.createCard(CardFactory.NORMAL_CARD));

		player1.getHandManager().selectCard(0);

		TurnManager.getInstance().endTurnAndDraw();

		assertEquals(0, player1.getHandManager().getSelectedCards().size());
	}

}
