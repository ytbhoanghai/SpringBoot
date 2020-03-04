$(function($) {
    "use strict";

    // Get List Response Message Of Topic
    function getAllListResponseMessage () {
        $.ajax({
            url: "/messages",
            crossDomain: true,
            type: 'GET',
            dataType: 'json',
        }).done(function (messageList) {
            for (let message of messageList) {
                let codeHtmlMediaList = createCodeHtmlMediaList(message);
                $('#mark').prepend(codeHtmlMediaList);
            }
        })
    }
    getAllListResponseMessage();

    // Get Element Button Show Chat Box
    let btnShowChatBox = $('#btn-show-chatbox');

    // Event Show/Hide ChatBox
    btnShowChatBox.on('click', function (event) {
        $(event.target).prev().slideToggle();
    });

    // WebSocket
    let webSocket = new SockJS("/listen");
    let stompClient = Stomp.over(webSocket);

    stompClient.connect({}, function () {
        stompClient.subscribe("/topic/messages", function (message) {
            let _message = JSON.parse(message.body);
            let codeHtmlMediaList = createCodeHtmlMediaList(_message);
            $('#mark').prepend(codeHtmlMediaList);
        }, function (error) {
            console.log("STOMP Protocol Error " + error);
        })
    });

    // Sent Message In Topic To Server
    let inputMessage = $('#inputMessage');
    function sendMessageToServer() {
        stompClient.send("/app/messages", {}, inputMessage.val());
        inputMessage.val('');
    }

    // Event Click Button Post
    $('#post').on('click', sendMessageToServer);

    // Generate Code Html Form Response Message
    function createCodeHtmlMediaList({urlAvatar, fullName, content, dateSend}) {
        return ` <ul class="media-list">
                                <li class="media">
                                    <a href="#" class="pull-left">
                                        <img src="${urlAvatar}" alt="" class="img-circle">
                                    </a>
                                    <div class="media-body">
                                <span class="text-muted pull-right">
                                    <small class="text-muted">${dateSend}</small>
                                </span>
                                        <strong class="text-primary">#${fullName}</strong>
                                        <p>
                                            ${content}
                                        </p>
                                    </div>
                                </li>
                            </ul>`;
    }
});