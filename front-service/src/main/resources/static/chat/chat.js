angular.module('front').controller('chatController', function ($rootScope, $scope, $http, $localStorage, $location) {

var usernamePage = document.querySelector('#username-page');
var usernameForm = document.querySelector('#usernameForm');
var connectingElement = document.querySelector('.connecting');
var chatPage = document.querySelector('#chat-page');
var chatPage1 = document.querySelector('#chat-page1');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var ulgroup = document.querySelector('#group');
var ulusers = document.querySelector('#users');
var btnSend = document.querySelector('#send');
var nameRecipiend = document.querySelector('#nameRecipiend');
var btnUser = document.getElementById("btn-user");
var currentUserId = null;
var currentUserName = null;
var currentGroupId = 1; //  !=0 - группа, ==0 - user
var stompClient = null;
var username = null;
var senderId = 0;
var sessionId = "";
var context = "/front/";


$scope.send = function(event) {
    var messageContent = messageInput.value.trim();

    if(messageContent && stompClient ) {
        const message = {
            senderId: senderId,
            recipientId: currentUserId,
            sender: username,
            recipientName: currentUserName,
            content: messageInput.value,
            groupId: currentGroupId,
            chatRoom: null,
            type: 'CHAT'
          };
          stompClient.send(context+"chat", {}, JSON.stringify(message));
        messageInput.value = '';
    }
}

$scope.loadGroup = function(event){
   if(stompClient) {
   var chatMessage = {
               senderId: senderId,
               type: 'LIST'
           };
      stompClient.send(context+"chatgroup", {}, JSON.stringify(chatMessage));
   }
}

$scope.loadUsers = function(event){

   if(stompClient) {
   var chatMessage = {
                  senderId: senderId,
                  type: 'LIST'
              };
      stompClient.send(context+"chatusers", {}, JSON.stringify(chatMessage));
   }
}

$scope.connect = function(event) {

    if(document.querySelector('#name').value.trim()) {
        var socket = new SockJS('http://localhost:8703/chat/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, $scope.onConnected, $scope.onError);
    }
}

$scope.onConnected = function() {

    stompClient.subscribe('/topic/public', $scope.onMessageReceived);
    stompClient.subscribe('/user/topic/session', $scope.onMessageReceived);
    stompClient.subscribe('/user/topic/history', $scope.onMessageReceived);
    stompClient.subscribe('/user/topic/list', $scope.onMessageReceived);
    stompClient.send(context+"register", {},
        JSON.stringify({sender: document.querySelector('#name').value.trim(), type: 'JOIN'})
    );

}

$scope.onCreateChatLi = function(type, sendId, chatdate, sender, recipientName, message){
   var messageElement = document.createElement('li');
   var minut = '';
   var dateHistory = '';
   for(let i=11; i<16; i++){
      minut = minut + chatdate[i];
   }
   for(let i=0; i<16; i++){
         dateHistory = dateHistory + chatdate[i];
      }

      messageElement.classList.add('chat-message');

   var usernameElement = document.createElement('span');

   if(sendId == senderId){
     messageElement.classList.add('userI');

   }else{
      messageElement.classList.add('user');

      if(type == 'HISTORY'){
         var usernameText = document.createTextNode(dateHistory + '   ' + sender);
         usernameElement.appendChild(usernameText);
      }else {
         var usernameText = document.createTextNode(sender);
         usernameElement.appendChild(usernameText);
      }
   }

   messageElement.appendChild(usernameElement);
   var textElement = document.createElement('p');
   if(type == 'HISTORY' && sendId == senderId){
      var messageText = document.createTextNode(message + '   ' + dateHistory);

   }else if(type == 'HISTORY' && sendId != senderId){
      var messageText = document.createTextNode(message);
   }else {
      var messageText = document.createTextNode(message+'  '+minut);
   }
   textElement.appendChild(messageText);
   messageElement.appendChild(textElement);
   messageArea.appendChild(messageElement);

   messageArea.scrollTop = messageArea.scrollHeight;

}

$scope.onMessageReceived = function(payload) {

    var message = JSON.parse(payload.body);


    if (message.type == 'SESSION'){
       sessionId = message.recipientName;

       if(sessionId != null){
          stompClient.subscribe('/queue/'+sessionId, $scope.onMessageReceived);
       }
       document.querySelector("#btn-load-users").disabled = false;

    }else if(message.type == 'LIST') {
       var buttElement = document.createElement('li');
       if(message.chatGroup != null){
          $scope.ligroup = message.chatGroup;
       }
       if(message.chatUsers != null){
          $scope.liusers = message.chatUsers;
       }

    }else if(message.type == 'NOTIFICATION') {

    } else if(message.type == 'JOIN') {
       if (username == null) {
          username = message.sender;
          senderId = message.senderId;
          $rootScope.user = message.sender;
          btnUser.innerText = message.sender;
          stompClient.subscribe('/user/topic/chat', $scope.onMessageReceived);
          $scope.loadGroup();
          $scope.loadUsers();
          stompClient.send(context+"session", {},
                      JSON.stringify({type: 'SESSION', senderId: senderId})
          );
          document.querySelector("#btnConnect").disabled = true;
          document.querySelector("#btnDisconnect").disabled = false;
       }else if(message.sender != username) {
          var list_li = ulusers.querySelectorAll('li');
          for(let i=0; i<list_li.length; i++){
              if (message.senderId == $scope.liusers[i].id){
                  list_li[i].querySelector('.btn').style.background = 'green';
              }
          }
       }

    } else if (message.type == 'LEAVE') {
       var list_li = ulusers.querySelectorAll('li');
       for(let i=0; i<list_li.length; i++){
          if (message.sender == $scope.liusers[i].id){
             list_li[i].querySelector('.btn').style.background = 'gray';
          }
       }

    } else if (message.type == 'HISTORY' && message.sender == username) {
        messageArea.innerHTML = '';
        if(message.groupId == null){
           for(let i = 0; i < message.chatRoom.length; i++){
             $scope.onCreateChatLi(message.type, message.chatRoom[i].senderId, message.chatRoom[i].chatdate,
                message.sender, message.recipientName, message.chatRoom[i].message);
           }
        }else {
           for(let i = 0; i < message.chatGroup.length; i++){
             $scope.onCreateChatLi(message.type, message.chatGroup[i].senderId, message.chatGroup[i].chatdate,
               message.chatGroup[i].senderName, message.chatGroup[i].recipientName, message.chatGroup[i].message);
           }
        }

    } else if (message.type == 'CHAT') {
       if(message.groupId == null){
          if(message.senderId == senderId && message.recipientId == currentUserId ||
             message.senderId == currentUserId && message.recipientId == senderId) {
                $scope.onCreateChatLi(message.type, message.chatRoom[0].senderId, message.chatRoom[0].chatdate,
                    message.sender, message.recipientName, message.chatRoom[0].message);
          }
       }else{
                $scope.onCreateChatLi(message.type, message.chatGroups[0].senderId, message.chatGroups[0].chatdate,
                    message.sender, message.recipientName, message.chatGroups[0].message);
       }

    }else if(message.type == 'ACTIVEUSERS') {
       var list_li = ulusers.querySelectorAll('li');
       for(let j=0; j<message.allIdUsers.length; j++){
          for(let i=0; i<list_li.length; i++){
             if (message.allIdUsers[j] == $scope.liusers[i].id){
                list_li[i].querySelector('.btn').style.background = 'green';
             }
             if ($scope.liusers[i].count == 0){
                list_li[i].querySelector('#badge').style.visibility = 'hidden';
             }
          }
       }
       document.querySelector("#btn-load-users").style.display = 'none';
       document.querySelector("#btn-load-users").style.visibility = 'hidden';

    }
}

$scope.btnGroup = function(group){
   nameRecipiend.value = group.title;
   currentGroupId = group.id;
   currentUserId = null;
   currentUserName = null;
   const message = {
               senderId: senderId,
               groupId: currentGroupId,
               sender: username,
               type: 'HISTORY'
      };
   stompClient.send(context+"history", {}, JSON.stringify(message));
}

$scope.btnUsers = function(users){
   nameRecipiend.value = users.nicName;
   currentGroupId = null;
   currentUserId = users.id;
   currentUserName = users.nicName;
   const message = {
            senderId: senderId,
            recipientId: currentUserId,
            sender: username,
            recipientName: currentUserName,
            type: 'HISTORY'
   };
   stompClient.send(context+"history", {}, JSON.stringify(message));
}

$scope.btnLoadUsers = function(event){
   stompClient.send(context+"activeusers", {},
       JSON.stringify({type: 'ACTIVEUSERS', senderId: senderId})
   );
   stompClient.send(context+"groupunread", {},
          JSON.stringify({type: 'GROUPUNREAD', senderId: senderId})
   );
}

$scope.sendFile = function(){
}

$scope.disconnect = function() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    document.querySelector("#btnConnect").disabled = false;
    document.querySelector("#btnDisconnect").disabled = true;
    document.querySelector('#name').value = '';
    currentUserId = null;
    currentUserName = null;
    currentGroupId = 1;
    stompClient = null;
    username = null;
    senderId = 0;
    sessionId = "";
}

$scope.onError = function(error) {
    connectingElement.textContent = 'Соединение не установлено.';
    connectingElement.style.color = 'red';
}

messageForm.addEventListener('submit', send, true)

});