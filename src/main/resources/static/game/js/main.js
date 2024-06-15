'use strict';

let playerJoinForm = document.querySelector("#playerJoinForm");
let makeMoveForm = document.querySelector("#makeMoveForm");
let infoJoin = document.querySelector("#info");
let gameView = document.querySelector("#gameView");

let playerId = null;
let stompClient = null;

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
        infoJoin.textContent = "Welcome " + nickname;

        if (numberOfPlayers === 2) {
            playerJoinForm.classList.add('hidden');
            gameView.classList.remove('hidden');
        }

        console.log("list size -> " + numberOfPlayers);
        console.log("server message", message);
    }
}

function makeMove(event) {
    event.preventDefault();
    let moveType = event.target.querySelector("button[type=submit]:hover").value; //:hover - mouse cursor pointer
    console.log(moveType + " test");


    if (moveType && stompClient) {
        if (moveType === 'raise') {
            //TODO
            stompClient.send("/websocket/game.makeMove", {}, JSON.stringify(moveType));
        } else if (moveType === 'bet') {
            //TODO
            stompClient.send("/websocket/game.makeMove", {}, JSON.stringify(moveType));
        } else if (moveType === 'fold') {
            //TODO
            stompClient.send("/websocket/game.makeMove", {}, JSON.stringify(moveType));
        }
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
makeMoveForm.addEventListener('submit', makeMove, true);