<%@page import="javax.sql.DataSource"%>
<%@page import="java.sql.SQLData"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">

<!-- jQuery library -->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

<!-- Popper JS -->
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>

<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

<!-- Latest compiled JavaScript -->
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>

<style type="text/css">
body {
	overflow: hidden;
}

.nav {
	background-color: blueviolet;
	position: relative;
	height: 100vh;
}

.row {
	margin: 0px !important;
}

.col-md-3, .col-md-9 {
	padding: 0px !important;
}

.container-fluid {
	margin: 0;
	padding: 0px;
}

.container {
	margin-left: 0px;
	margin-right: 0px;
}

ul {
	list-style-type: none;
}

.top {
	background-color: #00003d;
	height: 10vh;
}

.center-section {
	margin-top: 20px;
	position: relative;
	top: 0;
	overflow-y: scroll;
}

.center-section::-webkit-scrollbar {
	width: 0px;
}

.bottom {
	background-color: #efefef;
	height: 70px;
}

p {
	color: white;
	padding: 10px 10px;
}

input[type=text], textarea {
	width: 100%;
	padding: 12px 20px;
	margin: 8px 0;
	display: block;
	border-radius: 10px;
	border: 1px solid #ccc;
	box-sizing: border-box;
	resize: none;
}

.send {
	padding: 5px 15px;
	margin: 8px 0;
	background-color: green;
	color: white;
	display: block;
	border-radius: 5px;
	border: 1px solid #ccc;
	box-sizing: border-box;
	display: block;
}

.one {
	width: 300px;
	height: 50px;
	background-color: blue;
}

.two {
	width: 200px;
	height: 50px;
	background-color: pink;
}

.page-footer {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
}

.main-section {
	height: 90vh;
	position: relative;
}

#message {
	width: 100%;
}

.links {
    font-size: 22px;
    text-decoration: none;
    color: #f6f6f6;
    letter-spacing: 0.5px;
    padding: 15px;
    margin: 10px;
    cursor: pointer;
}

/* .links:hover{
    background-color: rgba(70,50,136,0.7);
    border-radius: 2px;
    font-size: 24px;
} */
</style>
<script src="https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.3.1.min.js"></script>
</head>
<body>
	<!-- 	<form>
		<input type="text" id="send-txt"> <input type="hidden"
			id="session-id"> <input type="button" value="Send Message"
			onClick="sendMessage();">
	</form>
	<textarea id="messageTextArea" rows="10" cols="50"></textarea> -->

	<div class="container-fluid">
		<div class="row">
			<div class="col-md-3 nav">
				<ul class="online-list">
				</ul>
			</div>
			<div class="col-md-9 Container">
				<div class="container-fluid top">
					<div class="row">
						<p id="name">
							<%
								if (request.getCookies() != null) {
									out.print(request.getCookies()[0].getValue().toString());
									System.out.println(request.getCookies().length);
								}
							%>
						</p>
					</div>
				</div>
				<div class="container main-section">
					<div class="row center-section"></div>
					<footer class="page-footer font-small blue row">
						<input type="hidden" id="session-id">
						<div class="col-md-10">
							<textarea rows="1" name="message" id="message"></textarea>
						</div>
						<div class="col-md-2">
							<button class="send" onclick="sendMessage();"
								style="font-size: 25px">
								Send <i class="fa fa-send-o"></i>
							</button>
						</div>
					</footer>
				</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
	obj = {
			'sender_id':null,
			'receiver_id':null,
			'msg':null
	};
	function getCookie(cname) {
			var name = cname + "=";
			var decodedCookie = decodeURIComponent(document.cookie);
			var ca = decodedCookie.split(';');
			for (var i = 0; i < ca.length; i++) {
				var c = ca[i];
				while (c.charAt(0) == ' ') {
					c = c.substring(1);
				}
				if (c.indexOf(name) == 0) {
					return c.substring(name.length, c.length);
				}
			}
			return "";
		}
		var textMessage = document.getElementById("send-txt");
		var messageText = document.getElementById("messageTextArea");
		var session = document.getElementById("session-id");
		var wsocket;
		var session_id;
		function connect() {
			wsocket = new WebSocket('ws://localhost:8080/TestWS/user/'
					+ getCookie("email"));
			console.log("websocket\t" + wsocket);
			wsocket.onmessage = function(msg) {
				onMessage(msg);
			};
			wsocket.onerror = function(msg) {
				onError(msg);
			};
			wsocket.onopen = function(msg) {
				onOpen(msg);
			};
			wsocket.onclose = function(msg) {
				onClose(msg);
			};
		}
		function onMessage(msg) {

			var json = JSON.parse(msg.data);
			console.log(json);
			if (json.isMessage == 0) {
				var res = json.online_user;
				$('.online-list').html("");
				for (let i = 0; i < res.length; i++) {
					console.log(res[i]["name"]);
					if (res[i]["email"] != getCookie("email")) {
						console.log("in loop : " + res[i]["email"]);
						var print_li = "<li class='links'><p class='" + res[i]['id']
								+ " btn btn-primary' onclick='myClick(this)''>"
								+ res[i]['name'] + "</p></li>"
						$('.online-list').append(print_li);
					}else{
						if(obj.sender_id == null){
							obj.sender_id = res[i]['id'];
						}
					}
				}
			}else if(json.isMessage == 1)
			{
				console.log(json);	
			}

		}
		function myClick(e) {
			var clas=e.className;
			var id=clas.split(' ')[0];
			obj.receiver_id = id;
		}
		function isJson(str) {
			try {
				JSON.parse(str);

			} catch (e) {
				return false;
			}
			return true;
		}
		function onError(msg) {
			/* messageText.value += "error ......." + msg + "\n"; */
		}
		function onOpen(msg) {
			session.value = 1;
			console.log("SERVER CONECTED");
			/* $('#message').val("Server Connect......\n"); */
		}
		function onClose(msg) {
			wsocket.send("Client disconnected..");
			/* messageText.value += "Server disconnected.."; */
		}
		function sendMessage() {
			if ($('#message').val() != "closed") {
				obj.msg=$('#message').val() ;
				console.log("sendMessage :- " + JSON.stringify(obj));
				wsocket.send(JSON.stringify(obj));
				//wsocket.send(textMessage.value);
				/* messageText.value += "Send to Server :-" + $('#message').val() 
						+ "\n"; */
				$('#message').val("");
			}
		}
		function onClose() {

		}
		window.addEventListener("load", connect, false);
	</script>
</body>
</html>