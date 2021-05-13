package com.summermessenger

class Message(sender: User, text: String, timeStamp: String) {
    var sender: User = sender
        get() = field;
        set(value) {field = value};
    var text: String = text
        get() = field;
        set(value) {field = value};
    var timeStamp: String = timeStamp
        get() = field;
        set(value) {field = value};
}