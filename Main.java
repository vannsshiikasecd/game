import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// Card class
class Card {
    private String suit;
    private String rank;
    private int value;

    public Card(String suit, String rank, int value) {
        this.suit = suit;
        this.rank = rank;
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public String getRank() {
        return rank;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}

// Deck class
class Deck {
    private ArrayList<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        int[] values = {2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10, 11};

        for (String suit : suits) {
            for (int i = 0; i < ranks.length; i++) {
                cards.add(new Card(suit, ranks[i], values[i]));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        return cards.remove(0);
    }
}

// Player class
class Player {
    protected ArrayList<Card> hand;
    protected int totalValue;

    public Player() {
        hand = new ArrayList<>();
        totalValue = 0;
    }

    public void addCard(Card card) {
        hand.add(card);
        totalValue += card.getValue();
        if (totalValue > 21) {
            for (Card c : hand) {
                if (c.getRank().equals("Ace") && totalValue > 21) {
                    totalValue -= 10; // Treat Ace as 1 instead of 11
                }
            }
        }
    }

    public int getTotalValue() {
        return totalValue;
    }

    public boolean isBusted() {
        return totalValue > 21;
    }

    public void displayHand() {
        for (Card card : hand) {
            System.out.println(card);
        }
        System.out.println("Total Value: " + totalValue);
    }
}

// Dealer class
class Dealer extends Player {
    public void showFirstCard() {
        if (!hand.isEmpty()) {
            System.out.println("Dealer shows: " + hand.get(0));
        }
    }

    public boolean shouldHit() {
        return totalValue < 17;
    }
}

// Game class
class Game {
    private Deck deck;
    private Player player;
    private Dealer dealer;

    public Game() {
        deck = new Deck();
        player = new Player();
        dealer = new Dealer();
    }

    public void play() {
        deck.shuffle();

        // Deal initial cards
        player.addCard(deck.deal());
        player.addCard(deck.deal());
        dealer.addCard(deck.deal());
        dealer.addCard(deck.deal());

        System.out.println("Player's Hand:");
        player.displayHand();

        dealer.showFirstCard();

        // Player's turn
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Do you want to hit or stand? (hit/stand)");
            String action = scanner.nextLine();
            if (action.equalsIgnoreCase("hit")) {
                player.addCard(deck.deal());
                System.out.println("Player's Hand:");
                player.displayHand();
                if (player.isBusted()) {
                    System.out.println("Player busts! Dealer wins.");
                    return;
                }
            } else if (action.equalsIgnoreCase("stand")) {
                break;
            } else {
                System.out.println("Invalid action. Please type 'hit' or 'stand'.");
            }
        }

        // Dealer's turn
        System.out.println("Dealer's Hand:");
        dealer.displayHand();
        while (dealer.shouldHit()) {
            System.out.println("Dealer hits.");
            dealer.addCard(deck.deal());
            System.out.println("Dealer's Hand:");
            dealer.displayHand();
            if (dealer.isBusted()) {
                System.out.println("Dealer busts! Player wins.");
                return;
            }
        }

        // Determine winner
        if (player.getTotalValue() > dealer.getTotalValue()) {
            System.out.println("Player wins!");
        } else if (player.getTotalValue() < dealer.getTotalValue()) {
            System.out.println("Dealer wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        Game game = new Game();
        game.play();
    }
}
