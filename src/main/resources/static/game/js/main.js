'use strict';

let playerJoinForm = document.querySelector("#playerJoinForm");
let makeMoveFormPlayer1 = document.querySelector("#makeMoveFormPlayer1");
let makeMoveFormPlayer2 = document.querySelector("#makeMoveFormPlayer2");
let infoJoin = document.querySelector("#info");
let gameView = document.querySelector("#gameView");
let bankDisplay = document.querySelector("#bank")

let playerId = null;
let stompClient = null;
let playerNumber = null;
let currentPlayer = null;

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
        let numberOfPlayers = message.numberOfPlayers;
        currentPlayer = message.currentPlayer;
        playerNumber = message.playerNumber;

        infoJoin.textContent = "Welcome " + nickname;
        console.log("current player --> " + currentPlayer + "  playerNumber -> " + playerNumber);

        if (numberOfPlayers === 2) {
            playerJoinForm.classList.add('hidden');
            gameView.classList.remove('hidden');
        }

        console.log("list size -> " + numberOfPlayers);
        console.log("server message", message);

        updateUI();

    }
}

function makeMove(event) {
    event.preventDefault();
    let moveType = event.target.querySelector("button[type=submit]:hover").value; //:hover - mouse point
    console.log(moveType + " test");

    if (moveType && stompClient && playerNumber) {

        let destination = '/websocket/game.makeMove.${playerNumber}';
        stompClient.send(destination, {}, JSON.stringify({moveType, playerId}));
    }
}


function updateUI() {
    if (currentPlayer === playerNumber) {
        if (playerNumber === 1) {
            makeMoveFormPlayer1.classList.remove('hidden');
            makeMoveFormPlayer2.classList.add('hidden');
        } else {
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


//TODO serve logic in controller line:78   game.makeMove.${playerNumber}