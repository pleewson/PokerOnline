'use strict';

var playerJoinForm = document.querySelector("#playerJoinForm");
var makeMoveForm = document.querySelector("#makeMoveForm");

var playerId = null;
var stompClient = null;


function connect(event) {
    playerId = document.querySelector('#playerId');

    if (playerId) {

        playerJoinForm.classList.add('hidden');
        makeMoveForm.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);

    }

    event.preventDefault();
}


function onConnected() {
    //subscribe to the game topic
    stompClient.subscribe('/topic/game', onMessageReceived)
    //sending player object to server
    stompClient.send("/game.addPlayer", {}, JSON.stringify(playerId));
}


function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);
    console.log("server message", message)
}

function onError(error) {
    console.error("ws error -------------------", error);
}



playerJoinForm.addEventListener('submit', connect, true);