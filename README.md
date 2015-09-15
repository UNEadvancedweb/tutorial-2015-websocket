# WebSockets and React.js tutorial

So far, we've looked at a web programming model where the server is always reacting to the client -- the client makes a
request and the server responds.

In this tutorial, we'll integrate WebSockets. Although the server is typically still reacting to requests from a client,
sometimes it then needs to send a message out to *other* clients that are also interested in the event.


## What's there

We're going to build off the "gibberish" app that we've used in previous tutorials.

In this tutorial, we're going to introduce a React.js view that can listen for gibberish events that contain a particular
word.

You will need one server and two browsers to try this out.

The project's starting point is the solution to the react.js tutorial from Week 8.


## Let's add an Actor

An **Actor** is a very simple model of concurrency. Think of it like a person who constantly checks an in-tray. You can
post it a message, in which case it might go away and do some processing. And then when it's done, it'll come back and
check for new messages in its in-tray.

And at some point in its processing, it might send other Actors messages too.


## First, let's add a publish-subscribe interface and some events

Each of our WebSocket connections is going to be an Actor. They will receive messages and broadcast them out to the browser.

But how will we get the WebSocket actors to hear about the events? In this tutorial, let's just do that using plain old
Java.

First, in the model package, let's create an interface called `MessageListener`. This should have one method in it:

```java
public interface GibberishListener {
    
    public void receiveGibberish(Gibberish g);

}
```

Then create a class called `MessageHub`. This is going to be a singleton (there'll be one of them) that holds a list of
GibberishListeners, and can broadcast Gibberish to them.

```java
package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is a simple publish-subscribe list. It maintains a list of listeners, and whenever it receives a call to 
 * <code>send</code>, it calls <code>receiveGibberish</code> on every registered listener.
 */
public class GibberishHub {

    List<GibberishListener> listeners;

    static final GibberishHub instance = new GibberishHub();

    public static GibberishHub getInstance() {
        return instance;
    }

    protected GibberishHub() {
        this.listeners = Collections.synchronizedList(new ArrayList<>());
    }

    public void send(Gibberish g) {
        for (GibberishListener listener : listeners) {
            listener.receiveGibberish(g);
        }
    }

    public void addListener(GibberishListener l) {
        this.listeners.add(l);
    }

    public void removeListener(GibberishListener l) {
        this.listeners.remove(l);
    }

}
```

## Now let's create an Actor that will be the server side of our websocket endpoint

Play uses the "Akka" framework for actors. So some of what we'll see here is Akka code.

In controllers, create a class for the actor. Let's call it `GibberishWebsocketActor`. There's quite a lot to this class,
so rather than give instructions on how to write it piece-by-piece (which would be error-prone), I'm pasting in the code
with comments to explain what each part of the code does.


```java
package controllers;

import akka.actor.*;
import model.GibberishHub;
import model.GibberishListener;

public class GibberishWebSocketActor extends UntypedActor {

    /**
     * We don't create the actor ourselves. Instead, Play will ask Akka to make it for us. We have to give Akka a
     * "Props" object that tells Akka what kind of actor to create, and what constructor arguments to pass to it.
     * This method produces that Props object.
     */
    public static Props props(String topic, ActorRef out) {
        // Create a Props object that says:
        // - I want a GibberishWebSocketActor,
        // - and pass (topic, out) as the arguments to its constructor
        return Props.create(GibberishWebSocketActor.class, topic, out);
    }

    /** The Actor for the client (browser) */
    private final ActorRef out;

    /** The topic string we have subscribed to */
    private final String topic;

    /** A listener that we will register with our GibberishHub */
    private final GibberishListener listener;

    /**
     * This constructor is called by Akka to create our actor (we don't call it ourselves).
     */
    public GibberishWebSocketActor(String topic, ActorRef out) {
        this.topic = topic;
        this.out = out;

        /*
          Our GibberishListener, written as a Java 8 Lambda.
          Whenever we receive a gibberish, convert it to a JSON string, and send it to the client.
         */
        this.listener = (g) -> {
            if (g.getSubject().equals(this.topic)) {
                // Convert the Gibberish to a JSON string
                String message = JsonExample.toJson(g).toString();

                /*
                 This asynchronously sends the message to the WebSocket client.
                 Self is a reference to this actor (the sender)
                 */
                out.tell(message, self());
            }
        };

        // Register this actor to hear gibberish
        GibberishHub.getInstance().addListener(listener);
    }

    /**
     * This is called whenever the browser sends a message to the serverover the websocket
     */
    public void onReceive(Object message) throws Exception {
        // The client isn't going to send us messages down the websocket in this example, so this doesn't matter
        if (message instanceof String) {
            out.tell("I received your message: " + message, self());
        }
    }

    /**
     * This is called by Play after the WebSocket has closed
     */
    public void postStop() throws Exception {
        // De-register our listener
        GibberishHub.getInstance().removeListener(this.listener);
    }
}
```

## Now let's create the API endpoint

The class we've written above defines what our server WebSocket actors do. But we still need to create a controller 
method to respond to a URL and create one.

This is going to need a new route, so add this into your `conf/routes` file, to accept websocket connections on
`/websocket?topic=topic`.

```scala
GET     /websocket                  controllers.JsonExample.socket(topic:String)
```

In `JsonExample.java` (the controller we used for the Gibberish JSON), let's now create the controller method. It's one line of code, but as it includes a 
generic method call and a Java 8 lambda, which you might not often see, it might look a little curious.

```java
    /**
     * Our WebSockets endpoint. We'll assume we're sending String messages for now
     */
    public static WebSocket<String> socket(String topic) {

        /*
         Play framework provides an Actor for client automatically. That's the <code>out</code> argument below.

         We need to tell Play what kind of actor we want on the server side. We do that with a "Props" object, because
         Play will ask Akka to create the actor for us.

         We want a GibberishWebSocketActor, and we want to pass the client actor and the topic as constructor arguments.
         GibberishWebSocketActor.props(topic, out) will produce a Props object saying that.
         */
        return WebSocket.<String>withActor((out) -> GibberishWebSocketActor.props(topic, out));
    }
```

## And now let's make the Gibberish controller send messages to the GibberishHub

Open `JsonExample.java`, and edit the `getGibberish` method, so that every time it generates a Gibberish, it also calls
`GibberishHub.send`

```java
    public static Result getGibberish(int n) {
        ObjectNode[] arr = new ObjectNode[n];

        ArrayNode an = Json.newArray();
        for (int i = 0; i < n; i++) {
            Gibberish g = new Gibberish();
            an.add(toJson(g));

            // Send the gibberish to the GibberishHub
            GibberishHub.getInstance().send(g);
        }

        return ok(an);
    }
```

This will now broadcast Gibberish to any listeners. And as our GibberishWebSocketActors each register a listener, they will
be called with any Gibberish. And we programmed their listeners to send the JSON string to the Actor for the browser (so Play will
send it out on the websocket).

## Test your Websocket

At this point, we can start the server, and make a test connection to it.

Open a browser, and visit [http://www.websocket.org/echo.html](http://www.websocket.org/echo.html). Open the developer
tools to the console, to check for errors from the browser.

In the Location field on the form on that page, put `ws://127.0.0.1:9000/websocket?topic=Algernon`, but replace `9000` 
with whichever port you started the server on. Then click connect. You should see `CONNECTED` appear in the textarea
after a moment. (If not, check for errors on the JavaScript console and the server). We've now connected to our websocket
and are listening for Gibberish with Algernon as the subject.

In another browser window, open `http://localhost:9000` and click "Get Gibberish" a few times. Every time it generates a
Gibberish with Algernon as the subject, you should see a message appear in the text area on the echo page.


## Connect up React.js

Let's now connect the websocket into our React.js app

First, in `assets/javascripts/xhrexample.coffee`, we'll add a function to open the websocket (listening for Algernon) and, whenever we receive a
Gibberish, we'll parse it into JSON and put it into an array `window.received`. The last line immediately calls this function, 
so we'll open the socket as soon as we open the page.

```coffee
window.received = []

window.getSocket = () ->
  websocket = new WebSocket("ws://#{window.location.host}/websocket?topic=Algernon");
  websocket.onmessage = (msg) ->
    console.log("Received a message over the websocket:")
    console.log(msg)
    console.log("---")
    json = JSON.parse(msg.data)
    window.received.push(json)
    rerender()

window.getSocket()
```

By the way, `rerender()` at the end of our onmessage function calls a method defined in `gibberish.jsx` to cause our
React.js view to rerender itself. The method is already there, but here it is:

```js
var rerender = function() {
  React.render(<GibberishApp />, mountNode);
}
```

And next, let's add a GibberishList for the messages we have received over the websocket (which we stored in `window.received`)

In `gibberish.jsx`, change GibberishApp's render method to:

```jsx
render: function() {
    return (
      <div>
        <form onSubmit={this.handleSubmit}>
          <p>
            http://
            <input type="text" value={this.state.server} onChange={this.setServer} />
            /gibberish?num=
            <input type="number" value={this.state.num} onChange={this.setNum} />
          </p>
          <button type="submit">Get gibberish</button>
        </form>
        <GibberishList gibberishList={window.gibberish} />

        <h2>And over the WebSocket...</h2>
        <GibberishList gibberishList={window.received} />
      </div>
    );
  }
```

Now refresh the page. Every time you click the Get Gibberish button, the top part of the page will show the gibberish 
that has just been collected. But every time there's a gibberish about Algernon, it will also be sent over the websocket,
collected in `window.received` and then displayed under the "And over the Websocket..." heading.

