package models

import javax.persistence.{Version, Id}

class User {
        @Id var id: String = _
        var name: String = _
        var addresses: java.util.List[Address] = new java.util.ArrayList()
        @Version var version: String = _

        override def toString = "User: " + this.id + ", name: " + this.name + ", addresses: " + this.addresses
}

class Address {
        var city: String = _
        var street: String = _

        override def toString = "Address: " + this.city + ", " + this.street
}

class Question {
        @Id var id: String = _
        var title: String = _
        var user: User = _
        @Version var version: String = _

        override def toString = "Question: " + this.id + ", title: " + this.title + ", belongs: " + user.name
}