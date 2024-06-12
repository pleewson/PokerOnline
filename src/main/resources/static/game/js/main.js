'use strict';

let playerJoinForm = document.querySelector("#playerJoinForm");
let makeMoveForm = document.querySelector("#makeMoveForm");

let playerId = null;
let stompClient = null;


function connect(event) {
    playerId = document.querySelector('#playerId').value;

    if (playerId) {

        playerJoinForm.classList.add('hidden');
        makeMoveForm.classList.remove('hidden');

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


function onMessageReceived(payload) {
    let message = JSON.parse(payload.body);
    console.log("server message", message)
}

function onError(error) {
    console.error("ws error -------------------", error);
}



playerJoinForm.addEventListener('submit', connect, true);