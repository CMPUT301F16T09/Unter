var firebase = require('firebase');
var request = require('request');

var API_KEY = "AIzaSyDnmSHLfw4Qb1mdPUtjXZgya8UXhYbXnU8"; // Your Firebase Cloud Server API key

firebase.initializeApp({
  serviceAccount: "unter-8d7e0-firebase-adminsdk-xqu1e-15a1e06f7c.json",
  databaseURL: "https://unter-8d7e0.firebaseio.com/"
});
ref = firebase.database().ref();

function listenForNotificationRequests() {
  var requests = ref.child('notificationRequests');
  ref.on('child_added', function(requestSnapshot) {
    var request = requestSnapshot.val();
    sendNotificationToUser(
      request.username, 
      request.message,
      function() {
        request.ref().remove();
      }
    );
  }, function(error) {
    console.error(error);
  });
};

function sendNotificationToUser(user, message, onSuccess) {
  request({
    url: 'https://fcm.googleapis.com/fcm/send',
    method: 'POST',
    headers: {
      'Content-Type' :' application/json',
      'Authorization': 'key='+API_KEY
    },
    body: JSON.stringify({
      notification: {
        title: message
      },
      to : '/topics/user_'+user.getUsername()
    })
  }, function(error, response, body) {
    if (error) { console.error(error); }
    else if (response.statusCode >= 400) { 
      console.error('HTTP Error: '+response.statusCode+' - '+response.statusMessage); 
    }
    else {
      onSuccess();
    }
  });
}

// start listening
listenForNotificationRequests();