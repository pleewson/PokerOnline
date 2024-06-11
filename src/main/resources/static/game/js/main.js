'use strict';

var playerJoinForm = document.querySelector("#playerJoinForm");
var makeMoveForm = document.querySelector("#makeMoveForm");

var player  = null;

// var stompClient = null;

function connect(event) {
    player = document.querySelector('#player');

    if (player) {
        playerJoinForm.classList.add('hidden');
        makeMoveForm.classList.remove('hidden');
    }

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);

    stompClient.connect({}, onConneted, onError)
}


function onConnected() {
    //subscribe to the game topic
    stompClient.subscribe('/topic/game', onMessageRecived)
    //sending player object to server
    stompClient.send("/game.addPlayer", {}, JSON.stringify(player));
}


function onMessageRecived() {
    //TODO
}

// playerJoin.addEventListener('submit',connect,true)