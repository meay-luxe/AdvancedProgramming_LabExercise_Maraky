package com.example.poker_game;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

import java.util.List;

public class PokerController {

   @FXML private StackPane communityCard1, communityCard2, communityCard3,
            communityCard4, communityCard5;
    @FXML private Label communityLabel1, communityLabel2, communityLabel3,
            communityLabel4, communityLabel5;


    @FXML private StackPane computerCard1, computerCard2;
    @FXML private Label computerLabel1, computerLabel2;
    @FXML private Label computerHandLabel;


    @FXML private StackPane playerCard1, playerCard2;
    @FXML private Label playerLabel1, playerLabel2;
    @FXML private Label playerHandLabel;

    @FXML private Button dealButton, flopButton, turnButton,
            riverButton, showdownButton;


    @FXML private Label stageLabel, resultLabel;
    @FXML private Label playerScoreLabel, computerScoreLabel;


    private Game game;
    private int playerWins = 0;
    private int computerWins = 0;


    @FXML
    public void initialize() {
        game = new Game();
        clearTable();
    }


    @FXML
    private void onDeal() {
        game.deal();
        clearTable();


        Card p1 = game.getPlayer().getHoleCards().get(0);
        Card p2 = game.getPlayer().getHoleCards().get(1);
        showCard(playerCard1, playerLabel1, p1);
        showCard(playerCard2, playerLabel2, p2);


        computerLabel1.setText("?");
        computerLabel2.setText("?");
        computerCard1.getStyleClass().setAll("card-back");
        computerCard2.getStyleClass().setAll("card-back");


        stageLabel.setText("Cards dealt — click FLOP");
        resultLabel.setText("");
        computerHandLabel.setText("");
        playerHandLabel.setText("");


        dealButton.setDisable(true);
        flopButton.setDisable(false);
        turnButton.setDisable(true);
        riverButton.setDisable(true);
        showdownButton.setDisable(true);
    }

    @FXML
    private void onFlop() {
        game.flop();

        List<Card> community = game.getCommunityCards();
        showCard(communityCard1, communityLabel1, community.get(0));
        showCard(communityCard2, communityLabel2, community.get(1));
        showCard(communityCard3, communityLabel3, community.get(2));

        stageLabel.setText("Flop revealed — click TURN");

        flopButton.setDisable(true);
        turnButton.setDisable(false);
    }

    @FXML
    private void onTurn() {
        game.turn();

        List<Card> community = game.getCommunityCards();
        showCard(communityCard4, communityLabel4, community.get(3));

        stageLabel.setText("Turn revealed — click RIVER");

        turnButton.setDisable(true);
        riverButton.setDisable(false);
    }

    @FXML
    private void onRiver() {
        game.river();

        List<Card> community = game.getCommunityCards();
        showCard(communityCard5, communityLabel5, community.get(4));

        stageLabel.setText("River revealed — click SHOWDOWN");

        riverButton.setDisable(true);
        showdownButton.setDisable(false);
    }

    @FXML
    private void onShowdown() {

        Card c1 = game.getComputer().getHoleCards().get(0);
        Card c2 = game.getComputer().getHoleCards().get(1);
        showCard(computerCard1, computerLabel1, c1);
        showCard(computerCard2, computerLabel2, c2);


        HandResult playerResult = HandEvaluator.evaluate(
                game.getPlayer().getHoleCards(),
                game.getCommunityCards()
        );

        HandResult computerResult = HandEvaluator.evaluate(
                game.getComputer().getHoleCards(),
                game.getCommunityCards()
        );


        playerHandLabel.setText("Your hand: " + playerResult.getHandRank().getDisplay());
        computerHandLabel.setText("Computer: " + computerResult.getHandRank().getDisplay());

        determineWinner(playerResult, computerResult);


        stageLabel.setText("SHOWDOWN! Click DEAL for a new round.");
        showdownButton.setDisable(true);
        dealButton.setDisable(false);
    }

    private void determineWinner(HandResult playerResult, HandResult computerResult) {
        int comparison = playerResult.compareTo(computerResult);

        if (comparison > 0) {

            playerWins++;
            resultLabel.setText("🎉 YOU WIN! " + playerResult.getHandRank().getDisplay()
                    + " beats " + computerResult.getHandRank().getDisplay());
            resultLabel.getStyleClass().setAll("result-label", "result-win");

        } else if (comparison < 0) {

            computerWins++;
            resultLabel.setText("😞 COMPUTER WINS! " + computerResult.getHandRank().getDisplay()
                    + " beats " + playerResult.getHandRank().getDisplay());
            resultLabel.getStyleClass().setAll("result-label", "result-lose");

        } else {

            resultLabel.setText("🤝 IT'S A TIE! Both have " + playerResult.getHandRank().getDisplay());
            resultLabel.getStyleClass().setAll("result-label", "result-tie");
        }


        playerScoreLabel.setText("You: " + playerWins + " wins");
        computerScoreLabel.setText("Computer: " + computerWins + " wins");
    }


    private void showCard(StackPane cardPane, Label cardLabel, Card card) {
        cardLabel.setText(card.toString());


        cardPane.getStyleClass().removeAll("card-back", "card-slot",
                "player-card-slot", "card-red", "card-black");


        cardPane.getStyleClass().add("card-face");


        if (card.getSuit() == Suit.HEARTS || card.getSuit() == Suit.DIAMONDS) {
            cardLabel.getStyleClass().setAll("card-label-red");
        } else {
            cardLabel.getStyleClass().setAll("card-label-black");
        }
    }

    private void clearTable() {

        clearCard(communityCard1, communityLabel1, "card-slot");
        clearCard(communityCard2, communityLabel2, "card-slot");
        clearCard(communityCard3, communityLabel3, "card-slot");
        clearCard(communityCard4, communityLabel4, "card-slot");
        clearCard(communityCard5, communityLabel5, "card-slot");


        clearCard(playerCard1, playerLabel1, "player-card-slot");
        clearCard(playerCard2, playerLabel2, "player-card-slot");


        clearCard(computerCard1, computerLabel1, "card-back");
        clearCard(computerCard2, computerLabel2, "card-back");
        computerLabel1.setText("?");
        computerLabel2.setText("?");


        resultLabel.setText("");
        computerHandLabel.setText("");
        playerHandLabel.setText("");
    }

    private void clearCard(StackPane cardPane, Label cardLabel, String defaultStyle) {
        cardLabel.setText("");
        cardLabel.getStyleClass().setAll("card-label");
        cardPane.getStyleClass().setAll(defaultStyle);
    }
}