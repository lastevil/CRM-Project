angular.module('front').controller('chatController', function ($rootScope, $scope, $http, $localStorage, $location) {

var connectingElement = document.querySelector('.connecting');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var ulgroup = document.querySelector('#group');
var ulusers = document.querySelector('#users');
var btnSend = document.querySelector('#send');
var nameRecipiend = document.querySelector('#nameRecipiend');
var file = document.querySelector('#logo');
var btnUser = document.getElementById("btn-user");
var currentUserId = null;
var currentUserName = null;
var currentGroupId = 1; //  !=0 - группа, ==0 - user
var stompClient = null;
var username = '';
var senderId = 0;
var sessionId = "";
var context = "/chat/";
var countUsers = 0;
var nickName = '';
var userReg = '';

const contextPathChat = 'http://localhost:8701/chat/api/v1/';
const contextPathAuth = 'http://localhost:8701/auth/api/v1/';

$scope.userDetails = function(){
    $http.get(contextPathAuth+'users/'+ $localStorage.username)
       .then(function successCallback(response) {
          nickName = response.data.lastName + ' ' + response.data.firstName;
          userReg = {userName: $localStorage.username,
                     nickName: nickName};

          $scope.regUser(userReg);
       }, function errorCallback(response) {
          alert(response.data);
       });
}

$scope.regUser = function(userReg){
   $http.post(contextPathChat+'registration',JSON.stringify(userReg))
          .then(function successCallback(response) {
             $scope.connect(event);
          }, function errorCallback(response) {
             alert(response.data);
          });
}

$scope.connect = function(event) { // установить соединение с сервером
    if (stompClient == null) {
          var socket = new SockJS('http://localhost:8703/chat/ws');
          stompClient = Stomp.over(socket);
          stompClient.connect({}, $scope.onConnected, $scope.onError);
    }
}

$scope.onConnected = function() {
    //при успешном соединении сделать подписки и зарегистрировать юзера на сервере
    stompClient.subscribe('/topic/public', $scope.onMessageReceived);
    stompClient.subscribe('/user/topic/session', $scope.onMessageSession);
    stompClient.subscribe('/user/topic/history', $scope.onMessageHistory);
    stompClient.subscribe('/user/topic/list', $scope.onMessageList);
    stompClient.subscribe('/user/topic/activeusers', $scope.onMessageActiveusers);
    stompClient.send(context+"register", {},
        JSON.stringify({senderName: $localStorage.username, type: 'JOIN'})
    );
}

$scope.send = function(event) { //послать сообщение
    var messageContent = messageInput.value.trim();
    if(messageContent && stompClient ) {
        const message = {
            senderId: senderId,
            recipientId: currentUserId,
            senderName: username,
            recipientName: currentUserName,
            message: messageInput.value,
            groupId: currentGroupId,
            type: 'CHAT'
          };
          stompClient.send(context+"chat", {}, JSON.stringify(message));
        messageInput.value = '';
    }
}

$scope.loadGroup = function(event){ //запросить список всех групп, в которые входит юзер
   if(stompClient) {
      stompClient.send(context+"chatgroup", {}, JSON.stringify({senderId: senderId}));
   }
}

$scope.loadUsers = function(event){ //запросить список всех пользователей
   if(stompClient) {
      stompClient.send(context+"chatusers", {}, JSON.stringify({senderId: senderId}));
   }
}

$scope.onCreateChatLi = function(type, sendId, chatdate, senderName, recipientName, message){
   // вывести запись сообщения на экран
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
      messageElement.classList.add('userR');
      if(type == 'HISTORY'){
         var usernameText = document.createTextNode(dateHistory + '   ' + senderName);
         usernameElement.appendChild(usernameText);
      }else {
         var usernameText = document.createTextNode(senderName);
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

$scope.onMessageSession = function(payload) {
   //получить свою сессию и подписаться на нее
   var message = JSON.parse(payload.body);

   sessionId = message.session;
   if(sessionId != null){
      stompClient.subscribe('/queue/'+sessionId, $scope.onMessageReceived);
   }
   $scope.loadGroup();
   $scope.loadUsers();
   document.querySelector("#btn-load-users").disabled = false;
}

$scope.onMessageHistory = function(payload) {
   // получить историю выбранной группы или пользователя
   var message = JSON.parse(payload.body);
   if (message.senderName == username) {
      messageArea.innerHTML = '';
      if(message.groupId == null){
         for(let i = 0; i < message.chatRoom.length; i++){
            $scope.onCreateChatLi('HISTORY', message.chatRoom[i].senderId, message.chatRoom[i].chatdate,
                message.recipientName, message.recipientName, message.chatRoom[i].message);
         }
      }else {
         for(let i = 0; i < message.chatGroup.length; i++){
            $scope.onCreateChatLi('HISTORY', message.chatGroup[i].senderId, message.chatGroup[i].chatdate,
                message.chatGroup[i].senderName, message.chatGroup[i].recipientName, message.chatGroup[i].message);
         }
      }
   }
}

$scope.onMessageList = function(payload) {
   //получить список групп, в которые входит юзер
   //получить список всех пользователей
   var message = JSON.parse(payload.body);

   if('groups' in message) {
         $scope.ligroup = message.groups;
   }
   if('users' in message) {
         $scope.liusers = message.users;
   }

}

$scope.onMessageActiveusers = function(payload) { //получить пользователей online
   var message = JSON.parse(payload.body);
   var list_li = ulusers.querySelectorAll('li');
   for(let j=0; j<message.length; j++){
        for(let i=0; i<list_li.length; i++){
           if (message[j] == $scope.liusers[i].id){
              list_li[i].querySelector('.btn').style.background = 'green';
           }
           if ($scope.liusers[i].count == 0){
              list_li[i].querySelector('#badge').style.visibility = 'hidden';
           }
        }
   }
   list_li = ulgroup.querySelectorAll('li');
        for(let i=0; i<list_li.length; i++){
           if ($scope.ligroup[i].count == 0){
              list_li[i].querySelector('#badgeGroup').style.visibility = 'hidden';
           }
        }
}

$scope.onMessageReceived = function(payload) { //получить сообщение от сервера
    var message = JSON.parse(payload.body);
    if(message.type == 'JOIN') {
       if (username == '') {
          username = message.senderName;
          senderId = message.senderId;
          btnUser.value = message.senderName;
          stompClient.subscribe('/user/topic/chat', $scope.onMessageReceived);
          stompClient.send(context+"session", {}, JSON.stringify({senderId: senderId})
          );
       }else if(message.senderName != username) {
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
          if (message.senderName == $scope.liusers[i].id){
             list_li[i].querySelector('.btn').style.background = 'gray';
          }
       }

    } else if (message.type == 'CHAT') {
       if(message.groupId == null){
          if(message.senderId == senderId && message.recipientId == currentUserId ||
             message.senderId == currentUserId && message.recipientId == senderId) {
                $scope.onCreateChatLi('CHAT', message.senderId, message.chatDate,
                    message.senderName, message.recipientName, message.message);
          }else {
             var list_li = ulusers.querySelectorAll('li');
             for(let i=0; i<list_li.length; i++){
                if (message.senderId == $scope.liusers[i].id){
                      list_li[i].querySelector('#badge').style.visibility = 'visible';
                    $scope.liusers[i].count = $scope.liusers[i].count + 1;
                }
             }
          }
       }else{

          if(message.recipientId == senderId && message.groupId == currentGroupId ||
                       message.senderId == currentGroupId && message.recipientId == senderId) {
                $scope.onCreateChatLi('CHAT', message.senderId, message.chatDate,
                    message.senderName, message.recipientName, message.message);
          } else {
             var list_li = ulgroup.querySelectorAll('li');
             for(let i=0; i<list_li.length; i++){
                 if (message.groupId == $scope.ligroup[i].id){
                       list_li[i].querySelector('#badgeGroup').style.visibility = 'visible';
                    $scope.liusers[i].count = $scope.liusers[i].count + 1;
                 }
             }
          }
       }
    }
}

$scope.btnGroup = function(group){ // при выборе группы загрузить еe историю
   nameRecipiend.value = group.title;
   currentGroupId = group.id;
   currentUserId = null;
   currentUserName = null;
   const message = {
               senderId: senderId,
               groupId: currentGroupId,
               senderName: username,
      };
   stompClient.send(context+"history", {}, JSON.stringify(message));
   var list_li = ulgroup.querySelectorAll('li');
   for(let i=0; i<list_li.length; i++){
      if (group.id == $scope.ligroup[i].id){
         $scope.ligroup[i].count = 0;
         list_li[i].querySelector('#badgeGroup').style.visibility = 'hidden';
      }
   }
}

$scope.btnUsers = function(users){ //при выборе пользователя загрузить его историю
   nameRecipiend.value = users.nickName;
   currentGroupId = null;
   currentUserId = users.id;
   currentUserName = users.nickName;
   const message = {
            senderId: senderId,
            recipientId: currentUserId,
            senderName: username,
            recipientName: currentUserName,
   };
   stompClient.send(context+"history", {}, JSON.stringify(message));
   var list_li = ulusers.querySelectorAll('li');
   for(let i=0; i<list_li.length; i++){
      if (users.id == $scope.liusers[i].id){
         $scope.liusers[i].count = 0;
         list_li[i].querySelector('#badge').style.visibility = 'hidden';
      }
   }
}

$scope.btnLoadUsers = function(event){ //загрузить пользователей online
     stompClient.send(context+"activeusers", {}, JSON.stringify({}));//загрузить пользователей online
     document.querySelector("#btn-load-users").style.display = 'none';
     document.querySelector("#btn-load-users").style.visibility = 'hidden';
}

$scope.sendFile = function(){
   $("#logo").trigger('click');
}

$scope.disconnect = function() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
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
    connectingElement.text = 'Соединение не установлено.';
    connectingElement.style.color = 'red';
}

messageForm.addEventListener('submit', send, true)
$scope.userDetails();

});