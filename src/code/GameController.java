
package code;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.ResourceBundle;

import exceptions.InvalidNumberofPlayersException;
import gui.CardComponent;
import gui.EKCardSelectionWindow;
import gui.EKDialogWindow;
import gui.EKPlayerSelectionWindow;
import gui.LanguageMenu;
import gui.MainWindow;
import gui.NumberofPlayersMenu;
import gui.PlayerNameEntryMenu;

public class GameController {

	public static void main(String[] args) {

		Game game = new GameLogger(new Game());

		Player player1 = new Player("One");
		Player player2 = new Player("Two");
		Player player3 = new Player("Three");
		Player player4 = new Player("Four");

		List<Player> test = new ArrayList<Player>();
		test.add(player1);
		test.add(player2);
		test.add(player3);
		test.add(player4);

		NumberofPlayersMenu menu = new NumberofPlayersMenu();

		LanguageMenu languageMenu = new LanguageMenu();

		// Opens the gui
		MainWindow window = new MainWindow();

		window.setSelectLanguageListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				window.setLocale(languageMenu.selectLanguage());
			}
		});

		window.setPlayGameListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int numPlayers = menu.selectNumberofPlayers(window.locale);

					game.start(numPlayers);

					List<String> playerNames = PlayerNameEntryMenu.getPlayerNames(window.locale, numPlayers);

					if (playerNames == null) {
						window.exitGame();
					} else {
						for (int i = 0; i < numPlayers; i++) {
							game.getPlayers().get(i).setName(playerNames.get(i));
						}

						window.openGameWindow();

						window.displayGameState(game);
						addCardListeners(game, window, window.getDisplayedCards());
					}
				} catch (InvalidNumberofPlayersException e1) {
					try {
						game.start(4);
					} catch (InvalidNumberofPlayersException ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		window.setNextTurnListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					game.getCurrentPlayer().getHandManager()
					        .addCards(game.getCurrentPlayer().getHandManager().getSelectedCards());
					window.clearSelected();
					game.nextTurn();
					window.displayGameState(game);
					addCardListeners(game, window, window.getDisplayedCards());
				} catch (Exception e) {
					window.endGame();
				}
			}

		});

		window.setPlaySelectedCardListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				List<CardComponent> components = window.getSelectedCards();
				List<Card> selectedCards = new ArrayList<Card>();
				for (CardComponent component : components) {
					selectedCards.add(component.getCard());
				}

				if (selectedCards.size() == 1 && selectedCards.get(0).getID() != CardFactory.NORMAL_CARD) {
					CardStack.getInstance().addCard(selectedCards.get(0));

					if (cardNeedsNoTargets(selectedCards.get(0))) {
						nopeChecker(game.getCurrentPlayer(), game, window);
						CardStack.getInstance().resolveTopCard();
						if (selectedCards.get(0).getID() == CardFactory.SCRY_CARD) {
							EKCardSelectionWindow cardSelect = new EKCardSelectionWindow(window.locale, null);

							List<Card> cardsScryed = new ArrayList<Card>();

							for (int i = 0; i < (MainDeck.getInstance().getCardCount() < 3
							        ? MainDeck.getInstance().getCardCount() : 3); i++) {
								cardsScryed.add(MainDeck.getInstance().getCards().get(i));
							}

							cardSelect.displayScryWindow(cardsScryed);
						}
						game.getCurrentPlayer().getHandManager().clearSelectedCards();
					} else if (cardNeedsPlayerTarget(selectedCards.get(0))) {
						List<Player> otherPlayers = new ArrayList<Player>();
						for (Player player : game.getPlayers()) {
							if (player != game.getActivePlayer()) {
								otherPlayers.add(player);
							}
						}
						window.hideHand();
						EKPlayerSelectionWindow playerSelectWindow = new EKPlayerSelectionWindow(game.getActivePlayer(),
						        otherPlayers, window.locale, "");
						Player targetedPlayer = playerSelectWindow.display();

						if (selectedCards.get(0).getID() == CardFactory.FAVOR_CARD) {
							notifyToPassTo(targetedPlayer, window);
							EKCardSelectionWindow cardSelector = new EKCardSelectionWindow(window.locale, "");
							Card selectedCard = cardSelector.displayFavorWindow(targetedPlayer);
							targetedPlayer.getHandManager().selectCard(targetedPlayer.getHand().indexOf(selectedCard));
						}

						nopeChecker(game.getCurrentPlayer(), game, window);

						window.unhideHand();

						game.getCurrentPlayer().getHandManager().clearSelectedCards();

						CardStack.getInstance().resolveTopCard(game.getCurrentPlayer(), targetedPlayer);
					}
					window.clearSelected();
					DiscardDeck.getInstance().addAll(selectedCards);
					window.displayGameState(game);
					addCardListeners(game, window, window.getDisplayedCards());
				} else { // Resolve bundle
					if (selectedCards.size() == 5) {
						EKCardSelectionWindow cardSelector = new EKCardSelectionWindow(window.locale, "");
						Card selectedCard = cardSelector.displayFiveCardBundleWindow();
						List<Card> cardsToAdd = new ArrayList<Card>();
						cardsToAdd.add(selectedCard);
						DiscardDeck.getInstance().removeCard(selectedCard.getClass());
						game.getActivePlayer().getHandManager().addCards(cardsToAdd);

						window.clearSelected();
						DiscardDeck.getInstance().addAll(selectedCards);
						window.displayGameState(game);
						addCardListeners(game, window, window.getDisplayedCards());
					} else if (selectedCards.size() == 3) {
						List<Player> otherPlayers = new ArrayList<Player>();
						for (Player player : game.getPlayers()) {
							if (player != game.getActivePlayer()) {
								otherPlayers.add(player);
							}
						}
						window.hideHand();
						EKPlayerSelectionWindow playerSelectWindow = new EKPlayerSelectionWindow(game.getActivePlayer(),
						        otherPlayers, window.locale, "");
						Player targetedPlayer = playerSelectWindow.display();

						notifyToPassTo(targetedPlayer, window);

						EKCardSelectionWindow cardSelector = new EKCardSelectionWindow(window.locale, "");
						String selectedCardName = cardSelector.displayThreeCardBundleWindow().toString();
						List<Card> cardsToAdd = new ArrayList<Card>();

						window.unhideHand();
						for (Card card : targetedPlayer.getHand()) {
							System.out.println(card.toString());
							if (card.toString().equals(selectedCardName)) {
								cardsToAdd.add(card);
								targetedPlayer.getHandManager().selectCard(targetedPlayer.getHand().indexOf(card));
								targetedPlayer.getHandManager().clearSelectedCards();
								break;
							}
						}
						game.getCurrentPlayer().getHandManager().clearSelectedCards();
						game.getCurrentPlayer().getHandManager().addCards(cardsToAdd);

						window.clearSelected();
						DiscardDeck.getInstance().addAll(selectedCards);
						window.displayGameState(game);
						addCardListeners(game, window, window.getDisplayedCards());
					} else if (selectedCards.size() == 2) {
						List<Player> otherPlayers = new ArrayList<Player>();
						for (Player player : game.getPlayers()) {
							if (player != game.getActivePlayer()) {
								otherPlayers.add(player);
							}
						}
						window.hideHand();
						EKPlayerSelectionWindow playerSelectWindow = new EKPlayerSelectionWindow(game.getActivePlayer(),
						        otherPlayers, window.locale, "");
						Player targetedPlayer = playerSelectWindow.display();

						List<Card> cardsToAdd = new ArrayList<Card>();

						int index = new Random().nextInt(targetedPlayer.getHand().size());
						Card selectedCard = targetedPlayer.getHand().get(index);

						targetedPlayer.getHandManager().selectCard(index);
						targetedPlayer.getHandManager().clearSelectedCards();

						cardsToAdd.add(selectedCard);

						// Check for nope from targeted player
						// EKDialogWindow.displayInfoMessage(title, toDisplay,
						// args);

						game.getCurrentPlayer().getHandManager().clearSelectedCards();
						game.getCurrentPlayer().getHandManager().addCards(cardsToAdd);

						window.clearSelected();
						DiscardDeck.getInstance().addAll(selectedCards);
						window.displayGameState(game);
						addCardListeners(game, window, window.getDisplayedCards());
					}
				}
			}

		});

		window.openStartWindow();
	}

	protected static boolean cardNeedsPlayerTarget(Card card) {
		return (card.getID() == CardFactory.ATTACK_CARD || card.getID() == CardFactory.FAVOR_CARD);
	}

	protected static boolean cardNeedsNoTargets(Card card) {
		return (card.getID() == CardFactory.EXPLODING_KITTEN_CARD || card.getID() == CardFactory.NOPE_CARD
		        || card.getID() == CardFactory.SHUFFLE_CARD || card.getID() == CardFactory.SKIP_CARD
		        || card.getID() == CardFactory.SCRY_CARD);
	}

	protected static void addCardListeners(Game game, MainWindow window, List<CardComponent> displayedCards) {
		for (CardComponent component : displayedCards) {
			component.addMouseListener(new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent arg0) {
					window.toggleSelected(component);
					if (game.getCurrentPlayer().getHand().contains(component.getCard())) {
						game.getCurrentPlayer().getHandManager()
						        .selectCard(game.getCurrentPlayer().getHand().indexOf(component.getCard()));
					} else {
						game.getCurrentPlayer().getHandManager()
						        .addCards(game.getCurrentPlayer().getHandManager().getSelectedCards());
						game.getCurrentPlayer().getHandManager().clearSelectedCards();
					}
				}

				@Override
				public void mouseEntered(MouseEvent arg0) {
					// Do nothing
				}

				@Override
				public void mouseExited(MouseEvent arg0) {
					// Do nothing

				}

				@Override
				public void mousePressed(MouseEvent arg0) {
					// Do nothing
				}

				@Override
				public void mouseReleased(MouseEvent arg0) {
					// Do nothing
				}

			});
		}
	}

	private static void nopeChecker(Player startingPlayer, Game game, MainWindow window) {
		List<Player> otherPlayers = new ArrayList<Player>();

		for (Player player : game.getPlayers()) {
			if (!player.equals(startingPlayer)) {
				otherPlayers.add(player);
			}
		}

		for (Player player : otherPlayers) {
			notifyToPassTo(player, window);
			boolean wantsToNope = EKDialogWindow.displayYesNo(
			        getStringFromBundle("NOPE_NOTIFICATION_TITLE", window.locale), "NOPE_NOTIFICATION_MESSAGE",
			        window.locale);

			Card nopeToPlay = getNopeInHand(player);

			if (wantsToNope && nopeToPlay != null) {
				CardStack.getInstance().addCard(nopeToPlay);
				player.getHandManager().selectCard(player.getHand().indexOf(nopeToPlay));
				player.getHandManager().clearSelectedCards();
				nopeChecker(player, game, window);
			}
		}
		notifyToPassTo(startingPlayer, window);
	}

	private static void notifyToPassTo(Player player, MainWindow window) {
		EKDialogWindow.displayInfoMessage(getStringFromBundle("PASS_NOTIFICATION_TITLE", window.locale),
		        getStringFromBundle("PASS_NOTIFICATION_MESSAGE", window.locale) + player.getName());
	}

	private static Card getNopeInHand(Player player) {
		for (Card card : player.getHand()) {
			if (card.getID() == CardFactory.NOPE_CARD) {
				return card;
			}
		}
		return null;
	}

	private static String getStringFromBundle(String key, Locale locale) {
		return ResourceBundle.getBundle("resources/resources", locale).getString(key);
	}
}