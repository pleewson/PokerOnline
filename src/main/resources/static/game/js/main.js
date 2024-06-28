'use strict';

let playerJoinForm = document.querySelector("#playerJoinForm");
let makeMoveFormPlayer1 = document.querySelector("#makeMoveFormPlayer1");
let makeMoveFormPlayer2 = document.querySelector("#makeMoveFormPlayer2");
let infoJoin = document.querySelector("#info");
let gameView = document.querySelector("#gameView");
let bankDisplay = document.querySelector("#bank");
let player1Coins = document.querySelector("#player1Coins");
let player2Coins = document.querySelector("#player2Coins");
let player1CurrentBet = document.querySelector("#player1CurrentBet");
let player2CurrentBet = document.querySelector("#player2CurrentBet");

let communityCard1 = document.querySelector("#communityCard1");
let communityCard2 = document.querySelector("#communityCard2");
let communityCard3 = document.querySelector("#communityCard3");
let communityCard4 = document.querySelector("#communityCard4");
let communityCard5 = document.querySelector("#communityCard5");

let playerId = null;
let stompClient = null;
let currentPlayer = null;
let isGameStarted = false;

function connect(event) {
    playerId = document.querySelector('#playerId').value;

    if (playerId) {
        let socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}

function onConnected() {
    //subscribe to the game topic
    stompClient.subscribe('/topic/game', onMessageReceived)
    //sending player object to server
    stompClient.send("/websocket/game.addPlayer", {}, JSON.stringify(playerId));
}

function onError(error) {
    console.error("ws error -------------------", error);
}

function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);

    if (message.type === "disconnect") {
        console.log("user has disconnected");
        onDisconnected();
    }

    if(message.type === "finish") {
        window.location.href = message.url;  //redirect players to scoreboards
    }

        let nickname = message.nickname;
        currentPlayer = message.currentPlayer;
        isGameStarted = message.gameStarted;

        if (!sessionStorage.getItem('playerNumber')) {
            sessionStorage.setItem('playerNumber', message.playerNumber);
            console.log("sessionStorage  ADDED NEW NUMBER " + message.playerNumber);
        } else {
            console.log("sessionStorage NOT ADDED NEW NUMBER");
        }

        infoJoin.textContent = "Welcome " + nickname;
        console.log("current player --> " + currentPlayer + "  playerNumber -> " + sessionStorage.getItem('playerNumber'));

        if (isGameStarted === true) {
            playerJoinForm.classList.add('hidden');
            gameView.classList.remove('hidden');
        }

        if (message.currentBet !== undefined) {
            bankDisplay.textContent = message.currentBet;
        }


//CARDS VIEW
        if (message.player1Cards !== undefined || message.player2Cards !== undefined) {
            let playerNumber = parseInt(sessionStorage.getItem('playerNumber'), 10);
            let playerCards;

            if (playerNumber === 1) {
                playerCards = message.player1Cards;
            }

            if (playerNumber === 2) {
                playerCards = message.player2Cards;
            }

            let card1 = playerCards[0];
            let card2 = playerCards[1];

            let card1Image = "/images/cards/" + card1 + ".png";
            let card2Image = "/images/cards/" + card2 + ".png";

            console.log("-- CARD LOG-- playernumber " + playerNumber + " card1img -> " + card1Image + " card2img -> " + card2Image)

            document.getElementById("player" + playerNumber + "Card1").setAttribute("src", card1Image);
            document.getElementById("player" + playerNumber + "Card2").setAttribute("src", card2Image);
        }

        //PLAYERS STSATS
        let playersStats = message.playersStats;
        playersStats.forEach(player => {
            updatePlayerStats(player.playerNumber, player.coins, player.currentBet);
        })

        //COMMUNITY CARDS
        let numCommunityCards = message.numCommunityCards;
        let communityCards = message.communityCards;
        console.log("numCommunityCards " + numCommunityCards + " communityCards ->>" + communityCards)

        if (numCommunityCards === 0 || numCommunityCards === null) {
            communityCard1.setAttribute("src", "/images/cards/BACK.png");
            communityCard2.setAttribute("src", "/images/cards/BACK.png");
            communityCard3.setAttribute("src", "/images/cards/BACK.png");
            communityCard4.setAttribute("src", "/images/cards/BACK.png");
            communityCard5.setAttribute("src", "/images/cards/BACK.png");
        } else if (numCommunityCards === 3) {
            communityCard1.setAttribute("src", "/images/cards/" + communityCards[0] + ".png");
            communityCard2.setAttribute("src", "/images/cards/" + communityCards[1] + ".png");
            communityCard3.setAttribute("src", "/images/cards/" + communityCards[2] + ".png");
            communityCard4.setAttribute("src", "/images/cards/BACK.png");
            communityCard5.setAttribute("src", "/images/cards/BACK.png");
        } else if (numCommunityCards === 4) {
            communityCard4.setAttribute("src", "/images/cards/" + communityCards[3] + ".png");
        } else if (numCommunityCards === 5) {
            communityCard5.setAttribute("src", "/images/cards/" + communityCards[4] + ".png");
        }

        console.log("server message", message);

        updateUI();


}


function makeMove(event) {
    event.preventDefault();
    let moveType = event.target.querySelector("button[type=submit]:hover").value; //:hover - mouse point
    console.log(moveType + " test");

    let playerNumber = parseInt(sessionStorage.getItem('playerNumber'), 10);

    let betAmount = null;
    if (moveType === "bet") {
        betAmount = parseInt(document.getElementById("betAmountPlayer" + playerNumber).value, 10);
    }

    if (moveType && stompClient && currentPlayer === playerNumber) {

        let moveRequest = {
            playerNumber: playerNumber,
            moveType: moveType,
            betAmount: betAmount
        };

        stompClient.send("/websocket/game.makeMove", {}, JSON.stringify(moveRequest));
    }
}


function updateUI() {
    let playerNumber = parseInt(sessionStorage.getItem('playerNumber'), 10);
    console.log("loggggggg -> UPADTE UI METHOD: currentPlayer: " + currentPlayer + ", playerNumber: " + playerNumber);

    if (currentPlayer === playerNumber) {
        if (playerNumber === 1) {
            makeMoveFormPlayer1.classList.remove('hidden');
            makeMoveFormPlayer2.classList.add('hidden');
        } else if (playerNumber === 2) {
            makeMoveFormPlayer2.classList.remove('hidden');
            makeMoveFormPlayer1.classList.add('hidden');
        }
    } else {
        makeMoveFormPlayer1.classList.add("hidden");
        makeMoveFormPlayer2.classList.add("hidden");
    }
}

function updatePlayerStats(playerNumber, coins, currentBet) {
    if (playerNumber === 1) {
        player1Coins.textContent = coins;
        player1CurrentBet.textContent = currentBet;
    } else if (playerNumber === 2) {
        player2Coins.textContent = coins;
        player2CurrentBet.textContent = currentBet;
    }
}


window.addEventListener("beforeunload", () => {
    if (stompClient) {
        stompClient.disconnect(() => {
            sessionStorage.
            onDisconnected("Rozłączono z serwerem WebSocket");
        });
    }

});


function onDisconnected() {
    playerJoinForm.classList.remove('hidden');
    gameView.classList.add('hidden');

    currentPlayer = null;
    isGameStarted = false;

    infoJoin.textContent = "Second player has disconnected." +
        " REFRESH SITE BEFORE STARTING A NEW GAME:)";

}

playerJoinForm.addEventListener('submit', connect, true);
makeMoveFormPlayer1.addEventListener('submit', makeMove, true);
makeMoveFormPlayer2.addEventListener('submit', makeMove, true);
