$(function ($) {
    // holder element
    let commentWrapper = $('div.comment-wrapper');
    let nameRoom = $('#nameRoom');
    let listRoom = $('select');
    let createRoom = $('#createRoom');
    let copyToClipboard = $('#copyToClipboard');
    let joinRoom = $('#joinRoom');
    let inputMessage = $('#inputMessage');
    let post = $('#post');
    let messages = $('#messages');

    let webSocket = new SockJS('/listen');
    let stompClient = Stomp.over(webSocket);
    stompClient.connect({}, onConnected);

    // function
    function showCommentWrapper() {
        commentWrapper.show();
        commentWrapper.find('#messages').empty();
        getAllMessageInRoom(listRoom.val());
    }

    function hideCommentWrapper() {
        commentWrapper.hide();
    }

    function getNameRoomOfUser() {
        $.ajax({
            url: '/room',
            type: 'GET',
            dataType: 'json',
        }).done(function (data) {
            for (let {name, id} of data) {
                addNameRoom(name, id);
            }
        })
    }

    function addNameRoom(name, id) {
        let option = $('<option></option>').text(name).val(id);
        listRoom.append(option);
    }

    function getAllMessageInRoom(id) {
        $.ajax({
            url: `/message/${id}`,
            type: 'GET',
            contentType: 'text/plain',
            dataType: 'json',
        }).done(function (listMessageDto) {
            for (let messageDto of listMessageDto) {
                let html = createCodeHtmlMediaList(messageDto);
                messages.prepend(html);
            }
        })
    }

    function onConnected() {
        listRoom.children('option').each(function () {
            if (+$(this).val() !== -1) {
                subscribe(`/topic/room/${$(this).val()}`);
            }
        })
    }

    function subscribe(uri) {
        stompClient.subscribe(uri, function (payload) {
            let messageDto = JSON.parse(payload.body);
            console.log(messageDto);
            if (messageDto.idRoom === listRoom.val()) {
                let html = createCodeHtmlMediaList(messageDto);
                messages.prepend(html);
            }
        });
    }

    function sendMessage() {
        stompClient.send(`/app/chat.sendMessage/${listRoom.val()}`, {}, inputMessage.val());
    }

    function createCodeHtmlMediaList({id, urlAvatar, username, author, content, dateSend, idRoom}) {
        return ` <ul class="media-list">
                                <li class="media">
                                    <a href="#${username}" class="pull-left">
                                        <img src="${urlAvatar}" alt="" class="img-circle">
                                    </a>
                                    <div class="media-body">
                                <span class="text-muted pull-right">
                                    <small class="text-muted">${dateSend}</small>
                                </span>
                                        <strong class="text-primary">#${author}</strong>
                                        <p>
                                            ${content}
                                        </p>
                                    </div>
                                </li>
                            </ul>`;
    }
    // event
    listRoom.on('change', function (event) {
        if (+$(event.target).val() !== -1) {
            copyToClipboard.show();
            showCommentWrapper();
        }
        else {
            copyToClipboard.hide();
            hideCommentWrapper();
        }
    });

    createRoom.on('click', function (event) {
        event.preventDefault();
        let name = nameRoom.val();
        $.ajax({
            url: '/room',
            type: 'POST',
            dataType: 'text',
            contentType: 'plain/text',
            data: name,
        }).done(id => {
            alert(`register room success with id is ${id}`);
            addNameRoom(name, id);
            subscribe(`/topic/room/${id}`);
        }).fail(t => {
            alert(`Failed`);
        })
    });

    copyToClipboard.on('click', function (event) {
        if (+listRoom.val() === -1) { return }
        let textArea = $('<textarea>').val(listRoom.val());
        $('body').append(textArea);
        textArea.select();
        document.execCommand("Copy");
        textArea.remove();
    });

    joinRoom.on('click', function (event) {
        event.preventDefault();
        $.ajax({
            url: '/room/join',
            type: 'PUT',
            contentType: 'text/plain',
            dataType: 'json',
            data: nameRoom.val(),
        }).done(roomDto => {
            alert(`joined to room ${roomDto.name}`);
            addNameRoom(roomDto.name, roomDto.id);
            subscribe(`/topic/room/${roomDto.id}`);
        }).fail(() => {
            alert(`Failed`);
        })
    });

    post.on('click', () => {
        sendMessage();
        inputMessage.val('');
    });
    // preparing data
    getNameRoomOfUser();
});