$(function($) {

    // elements
    let btnShowChatBox = $('#btn-show-chatbox');
    let chatBox = $('#chatbox');
    let selectUsers = $('#selectUsers');
    let boxRecent = $('#box-recent');
    let messages = $('#messages');
    let formSendMessage = $('#form-send-message');
    let contentMessage = $('#content-message');
    let formSelectUser = $('#form-select-user');

    //
    loadUser();
    getAllChatChannel();

    let webSocket = new SockJS('/listen');
    let stompClient = Stomp.over(webSocket);
    stompClient.connect({}, onConnected);

    // events;
    btnShowChatBox.on('click', () => {
        onClickBtnShowChatBox();
    });

    formSelectUser.on('submit', function (event) {
        event.preventDefault();
        createChatChannel(selectUsers.val());
    });


    formSendMessage.on('submit', function (event) {
        event.preventDefault();
        let idChatChannel = $('a.active').attr('data-idChatChannel');
        let content = contentMessage.val();
        if(content) {
            stompClient.send(`/app/sendMessage.to/${idChatChannel}`, {}, content);
            contentMessage.val('');
        }
    });

    // functions
    function onClickBtnShowChatBox() {
        chatBox.fadeToggle(500);
    }

    function loadUser() {
        let jqXHR = $.ajax({
            url: "/user",
            type: "GET",
            dataType: "json",
        }).done(function (userResponses) {
            for (let userResponse of userResponses) {
                $('<option></option>')
                    .val(userResponse.username)
                    .text(userResponse.fullName)
                    .appendTo(selectUsers);
            }
        })
    }

    function createChatChannel(withUsername) {
        let jqXHR = $.ajax({
            url: "/chatChannel",
            type: "POST",
            dataType: "json",
            contentType: "text/plain",
            data: withUsername,
        }).done(function (chatChannelResponse) {
            drawChatChannelInBoxRecent(false, chatChannelResponse);
        }).fail(function (xhr) {
            alert(xhr.responseJSON.message)
        });
    }

    function getAllChatChannel() {
        let jqXHR = $.ajax({
            url: "/chatChannel",
            type: "GET",
            dataType: "json",
        }).done(function (chatChannelResponses) {
            let first = true;
            for (let chatChannelResponse of chatChannelResponses) {
                if (first) {
                    drawChatChannelInBoxRecent(true, chatChannelResponse);
                    getAllMessageByIdChatChannel(chatChannelResponse.idChatChannel);
                    first = false;
                }
                else {
                    drawChatChannelInBoxRecent(false, chatChannelResponse);
                }

            }
        }).fail(function (xhr) {
            console.log(xhr);
        })
    }

    function drawChatChannelInBoxRecent(active, chatChannelResponse) {
        let classTagA = "list-group-item list-group-item-action list-group-item-light rounded-0";
        if (active) {
            classTagA = "list-group-item list-group-item-action active text-white rounded-0";
        }

        let html = `<a class="${classTagA}" data-withUsername="${chatChannelResponse.username}" data-idChatChannel="${chatChannelResponse.idChatChannel}">
                                    <div class="media"><img src="${chatChannelResponse.urlAvatar}" alt="user" width="50" class="rounded-circle">
                                        <div class="media-body ml-4">
                                            <div class="d-flex align-items-center justify-content-between mb-1">
                                                <h6 class="mb-0">${chatChannelResponse.fullName}</h6><small class="small font-weight-bold">${chatChannelResponse.dateSeenRecently}</small>
                                            </div>
                                            <p class="font-italic mb-0 text-small">${chatChannelResponse.contentRecently.substring(0, 15) + "..." }</p>
                                        </div>
                                    </div>
                                </a>`;
        boxRecent.append(html);
        $('a[data-idChatChannel]').on('click', function () {
            let idChatChannel = $(this).attr('data-idChatChannel');
            getAllMessageByIdChatChannel(idChatChannel);

            disableChatChannelActive();
            activeChatChannel(this);
        })
    }

    function getAllMessageByIdChatChannel(idChatChannel) {
        let jqXHR = $.ajax({
            url: `/messages/${idChatChannel}`,
            type: "GET",
            dataType: "json",
        }).done(function (messageResponses) {
            messages.empty();
            for (let messageResponse of messageResponses) {
                if (messageResponse.type === 1) {
                    drawSenderMessages(messageResponse);
                }
                else {
                    drawReceiverMessages(messageResponse);
                }
            }
        })
    }

    function drawSenderMessages(messageResponse) {
        let html = `<div class="media w-50 mb-3"><img src="${messageResponse.urlAvatar}" alt="user" width="50" class="rounded-circle">
                            <div class="media-body ml-3">
                                <div class="bg-light rounded py-2 px-3 mb-2">
                                    <p class="text-small mb-0 text-muted">${messageResponse.content}</p>
                                </div>
                                <p class="small text-muted">${messageResponse.dateSend}</p>
                            </div>
                        </div>`;
        messages.append(html);
        messages.scrollTop(messages.prop('scrollHeight'));
    }

    function drawReceiverMessages(messageResponse) {
        let html = `<div class="media w-50 ml-auto mb-3">
                            <div class="media-body">
                                <div class="bg-primary rounded py-2 px-3 mb-2">
                                    <p class="text-small mb-0 text-white">${messageResponse.content}</p>
                                </div>
                                <p class="small text-muted">${messageResponse.dateSend}</p>
                            </div>
                        </div>`;
        messages.append(html);
        messages.scrollTop(messages.prop('scrollHeight'));
    }

    function disableChatChannelActive() {
        $('.active.text-white')
            .removeClass('active')
            .removeClass('text-white')
            .addClass('list-group-item-light');
    }

    function activeChatChannel(chatChannel) {
        $(chatChannel).removeClass('list-group-item-light')
            .addClass('active')
            .addClass('text-white');
    }

    function onConnected() {
        stompClient.subscribe("/user/queue/reply", function (payload) {
            let messageResponse = JSON.parse(payload.body);
            let chatChannelActive = $('a.active');
            if (chatChannelActive.attr('data-idChatChannel') === messageResponse.idChatChannel) {
                if (messageResponse.type === 1) {
                    drawSenderMessages(messageResponse);
                }
                else {
                    drawReceiverMessages(messageResponse);
                }
            }
            else {
                if (messageResponse.type === 1) {
                    console.log(`bạn có 1 tin nhắn mới từ ${messageResponse.fullName}`);
                }
            }
            $(`a[data-idChatChannel=${messageResponse.idChatChannel}]`).find('p').text(messageResponse.content.substring(0, 15) + "...");
        })
    }
});