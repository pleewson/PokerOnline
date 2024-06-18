'use strict';

let playerJoinForm = document.querySelector("#playerJoinForm");
let makeMoveFormPlayer1 = document.querySelector("#makeMoveFormPlayer1");
let makeMoveFormPlayer2 = document.querySelector("#makeMoveFormPlayer2");
let infoJoin = document.querySelector("#info");
let gameView = document.querySelector("#gameView");
let bankDisplay = document.querySelector("#bank")

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
        console.log("user has disconnected 123123123123123123123123 ");
        onDisconnected();
    } else {
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

        console.log("server message", message);

        updateUI();

    }
}

function makeMove(event) {
    event.preventDefault();
    let moveType = event.target.querySelector("button[type=submit]:hover").value; //:hover - mouse point
    console.log(moveType + " test");

    let playerNumber = parseInt(sessionStorage.getItem('playerNumber'), 10);

    if (moveType && stompClient && currentPlayer === playerNumber) {

        let moveRequest = {
            playerNumber: playerNumber,
            moveType: moveType
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


window.addEventListener("beforeunload", () => {
    if (stompClient) {
        stompClient.disconnect(() => {
            onDisconnected("Rozłączono z serwerem WebSocket");
        });
    }
});

function onDisconnected() {
    playerJoinForm.classList.remove('hidden');
    gameView.classList.add('hidden');

    infoJoin.textContent = "Second player has disconnected ";
}

playerJoinForm.addEventListener('submit', connect, true);
makeMoveFormPlayer1.addEventListener('submit', makeMove, true);
makeMoveFormPlayer2.addEventListener('submit', makeMove, true);


//TODO serve logic in controller