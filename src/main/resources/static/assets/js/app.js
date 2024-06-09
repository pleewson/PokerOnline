let stompClient = null;

function connect() {
    const socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/game', function (gameStatus) {
            showGameStatus(JSON.parse(gameStatus.body));
        });
    });
}

function joinGame(playerName) {
    stompClient.send("/app/join", {}, JSON.stringify({'playerName': playerName}));
}

function makeMove(playerName, move, amount) {
    stompClient.send("/app/move", {}, JSON.stringify({'playerName': playerName, 'move': move, 'amount': amount}));
}

function showGameStatus(gameStatus) {
    document.getElementById('currentBet').innerText = gameStatus.currentBet;
    const playerList = document.getElementById('playerList');
    playerList.innerHTML = '';
    gameStatus.playerList.forEach(player => {
        const li = document.createElement('li');
        li.innerText = `${player.email} - Coins: ${player.coins} - Active: ${player.isActive}`;
        playerList.appendChild(li);
    });
}

document.getElementById('moveForm').addEventListener('submit', function(event) {
    event.preventDefault();
    const formData = new FormData(event.target);
    const playerName = formData.get('playerName');
    const move = formData.get('move');
    let amount = 0;
    if (move === 'raise') {
        amount = 10;  // Example amount
    }
    makeMove(playerName, move, amount);
});

connect();
