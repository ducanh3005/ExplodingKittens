
package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import code.CardFactory;
import code.CardStack;
import code.DiscardDeck;
import code.Game;
import code.MainDeck;
import code.Player;
import code.PriorityManager;
import code.TurnManager;
import exceptions.InvalidNumberofPlayersException;
import exceptions.NoSuchPlayerException;

public class PriorityManagerTest {
	@Rule
	public final ExpectedException exception = ExpectedException.none();

	@Before
	public void initialize() {
		PriorityManager.tearDown();
		CardStack.tearDown();
		TurnManager.tearDown();
		MainDeck.tearDown();
		DiscardDeck.tearDown();
	}

	@After
	public void tearDown() {
		PriorityManager.tearDown();
		CardStack.tearDown();
		TurnManager.tearDown();
		MainDeck.tearDown();
		DiscardDeck.tearDown();
	}

	@Test
	public void testPriorityManagerInstanceGet() {
		@SuppressWarnings("unused")
		PriorityManager pm = PriorityManager.getInstance();
	}

	@Test
	public void testRemovePlayerFromEmptyPlayerList() {
		PriorityManager pm = PriorityManager.getInstance();

		int numPlayers = pm.getPlayerCount();
		pm.removePlayer(new Player("Player 1"));

		assertEquals(numPlayers, pm.getPlayerCount());
	}

	@Test
	public void testRemovePlayerNotInPlayerList() {
		PriorityManager pm = PriorityManager.getInstance();

		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));

		int numPlayers = pm.getPlayerCount();

		pm.removePlayer(new Player("Player 3"));

		assertEquals(numPlayers, pm.getPlayerCount());

		PriorityManager.tearDown();
	}

	@Test
	public void testRemoveActivePlayerFromPlayerList() throws NoSuchPlayerException {
		PriorityManager pm = PriorityManager.getInstance();

		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));

		pm.addPlayers(players);

		pm.removePlayer(pm.getActivePlayer());

		assertEquals(1, pm.getPlayerCount());
	}

	@Test
	public void testRemoveNonActivePlayerFromPlayerList() throws NoSuchPlayerException {
		PriorityManager pm = PriorityManager.getInstance();

		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));

		pm.addPlayers(players);

		pm.removePlayer(players.get(1));

		assertEquals("Player 1", pm.getActivePlayer().getName());
		assertEquals(1, pm.getPlayerCount());
	}

	@Test
	public void getActivePlayer() {
		PriorityManager pm = PriorityManager.getInstance();

		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));

		pm.addPlayers(players);

		assertEquals("Player 1", pm.getActivePlayer().getName());
	}

	@Test
	public void testNextPlayer() {
		PriorityManager pm = PriorityManager.getInstance();

		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));

		pm.addPlayers(players);

		assertEquals("Player 1", pm.getActivePlayer().getName());

		pm.nextPlayer();

		assertEquals("Player 2", pm.getActivePlayer().getName());

		pm.nextPlayer();

		assertEquals("Player 1", pm.getActivePlayer().getName());
	}

	@Test
	public void testResolveCard() {
		CardFactory cf = new CardFactory();
		PriorityManager pm = PriorityManager.getInstance();

		List<Player> players = new ArrayList<Player>();
		players.add(new Player("Player 1"));
		players.add(new Player("Player 2"));
		players.add(new Player("Player 3"));
		players.add(new Player("Player 4"));

		pm.addPlayers(players);
		CardStack.getInstance().addCard(cf.createCard(CardFactory.NORMAL_CARD));
		assertEquals(1, CardStack.getInstance().getStack().size());

		pm.resolveCard();

		assertEquals(4, pm.getCycleCount());
		assertEquals(0, CardStack.getInstance().getStack().size());
		assertTrue(pm.getActivePlayer().getName().equals("Player 1"));
	}

	@Test
	public void testGetAndSetCycleCount() {
		PriorityManager priorityManager = PriorityManager.getInstance();
		priorityManager.setCycleCount(3);
		assertEquals(3, priorityManager.getCycleCount());
	}

	@Test
	public void testCycleCountOne() {
		PriorityManager priorityManager = PriorityManager.getInstance();
		priorityManager.setCycleCount(1);
		assertEquals(1, priorityManager.getCycleCount());
	}

	@Test
	public void testCycleCountZero() {
		PriorityManager priorityManager = PriorityManager.getInstance();
		priorityManager.setCycleCount(0);
		assertEquals(0, priorityManager.getCycleCount());
	}

	@Test
	public void testCycleCount800() {
		PriorityManager priorityManager = PriorityManager.getInstance();
		priorityManager.setCycleCount(800);
		assertEquals(800, priorityManager.getCycleCount());
	}

	@Test
	public void testAddPlayerGameAlreadyStarted() throws InvalidNumberofPlayersException {
		Game game = new Game();
		game.start(3);
		List<Player> players = new ArrayList<Player>();
		players.add(new Player());

		PriorityManager.getInstance().addPlayers(players);

		assertEquals(4, PriorityManager.getInstance().getPlayerCount());
	}

}
