package com.summermessenger.data.model

import java.util.*

class Message(sender: User, text: String, timeStamp: Date) {
    var sender: User = sender
        get() = field;
        set(value) {field = value};
    var text: String = text
        get() = field;
        set(value) {field = value};
    var timeStamp: Date = timeStamp
        get() = field;
        set(value) {field = value};
}