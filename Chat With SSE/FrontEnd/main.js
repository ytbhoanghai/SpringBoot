$(function($) {
    "use strict";

    /* ================== ELEMENT ================== */
    let username = $('#username');
    let inputMessage = $('#inputMessage');
    let mark = $('#mark');
    let post = $('#post');

    /* ================== DATA PREPARATION ================== */
    // 1. Generate random username
    username.text(`Username: ${Math.random().toString(36).substring(5)}`);

    /* ================== VARIABLES ================== */
    // 1. Get Full Name
    let  fullName = username.text().substring(10);

    /* ================== EVENT CLICK ================== */
    // 1. Click Button Send Message
    post.on('click', function() {
        sendMessageToServer();
        inputMessage.val('');
    });

    /* ================== EVENT PAGE ================== */
    // 1. Close Listen Client And Remove SSE server
    $(window).on('beforeunload', function() {
        closeEventSource(fullName);
        eventSource.close();
    });

    /* ================== FUNCTIONS ================== */
    // Create code html for one message
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

    /* ================== AJAX FUNCTION ================== */
    // 1. Send Message To Server
    function sendMessageToServer() {
        let content = inputMessage.val();

        $.ajax({
            url: "http://127.0.0.1:8080/new.message",
            crossDomain: true,
            type: "POST",
            contentType: 'application/json',
            data: JSON.stringify({fullName, content}),
        });
    }

    // 2. Close Listen
    function closeEventSource(fullName) {
        $.ajax({
            url: `http://127.0.0.1:8080/listen.close/${fullName}`,
            crossDomain: true,
            type: "GET",
        })
    }

    /* ================== SERVER SEND EVENT ================== */
    let eventSource = new EventSource(`http://127.0.0.1:8080/listen/${fullName}`);

    eventSource.onmessage = function(event) {
        let responseMessage = JSON.parse(event.data);
        mark.prepend(createCodeHtmlMediaList(responseMessage));
    }
});