# EventSource tutorial

In this branch, I've adapted the solution to the WebSocket tutorial to use EventSource instead. All the WebSocket code
is still there, but I've switched the browser to connect on the /eventsource URL instead.

## What's changed

We're still using the GibberishHub, and creating a GibberishListener, but the code that was particular to websockets is
no longer used -- particularly, we're not using `GibberishWebSocketActor`, which brought in several concepts (eg, Actors)
that you might not have heard of.

Instead, all our EventSource code is contained in the single controller method `JsonExample.eventSource`

```java
/**
 * Our Server Sent Events endpoint. This uses an HTTP response with a chunked encoding, and a Content-Type of
 * text/event-stream so it looks very much like any other controller method (returning Result, etc)
 */
public static Result eventSource(final String topic) {

    /*
     Use Play's helper method to create the event source.
     It takes a lambda, which is passed the event source as its argument
     */
    EventSource e = EventSource.whenConnected(eventSource -> {

        // Create a listener for gibberish
        GibberishListener l = (g) -> {
            if (g.getSubject().equals(topic)) {
                // If the subject matches the topic we're listening for, send the gibberish as a server-sent event
                eventSource.send(EventSource.Event.event(toJson(g)));
            }
        };

        // Add the listener, to listen for gibberish
        GibberishHub.getInstance().addListener(l);

        // When the event source closes, remove the gibberish listener to clean up
        eventSource.onDisconnected(() -> GibberishHub.getInstance().removeListener(l));
    });

    // Return ok with our event source
    return ok(e);
}
```

As you can see, that's not very long. 

We use `EventSource.whenConnected` to create the event source in Play, passing it a callback (as a Java 8 lambda).

That callback registers a `GibberishListener`, that converts matching messages to JSON and sends them out on the event
source. And when the client disconnects, it removes the `GibberishListener` so we don't get a memory leak.

Note that the `topic` argument should be marked `final`, as we're using it inside the lambdas.

And of course we have a `routes` entry for the controller method:

```
GET     /eventsource                controllers.JsonExample.eventSource(topic:String)
```


## On the client side

This was the code for our websocket connection from the client, in `xhrexample.coffee`:

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

The new code for an event source (Server Sent Events) connection is almost identical:

```coffee
window.getSSE = () ->
  sse = new EventSource("eventsource?topic=Algernon");
  sse.onmessage = (msg) ->
    console.log("Received a message over the EventSource:")
    console.log(msg)
    console.log("---")
    json = JSON.parse(msg.data)
    window.received.push(json)
    rerender()

window.getSSE()
```


