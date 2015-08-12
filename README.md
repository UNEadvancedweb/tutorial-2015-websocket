# XHR and CORS

* clone the repository *twice* -- you will need two servers for this exercise

* start both servers, on different ports


## Checking it can call its own server

* Open a web browser, and open up the index page (/) for one of the servers. eg, 127.0.0.1:9000/
 
* Open your browser's developer tools

* Type the server's address into the topmost text box. 

* Try the "get gibberish" button, and use the Network tab of your developer tools to examine the data and response

* Try the "post gibberish" button, and use the Network tab of your developer tools to examine the data and response. Also check that on the console on the server, it printed a message.


## Checking it can't call a different origin

* Now change the server address to have the port of the other server you are running. This is identical code -- all that has changed is which server we are posting to.

* Try the "get gibberish" button, and use the Network tab of your developer tools to inspect what happens. Also see if anything appears in the error console.

* Try the "post gibberish" button, and use the Network tab of your developer tools to inspect what happens. Also see if anything appears in the error console.


## Implementing CORS

* Edit the server you are contacting to allow access from the other server. You will need to implement CORS headers.

* Note that for the second button (post) you will need to add an OPTIONS route and controller method.



## JSON tinkering

* Have a look at the JSON code in JsonExample.java. Why is this in its own file? Why is it not in Model?


## CoffeeScript and XHR

* We'll introduce CoffeeScript formally next week, but have a look at xhrexample.coffee and see if you can understand what it's doing

* Edit the calls that make the XMLHttpRequest so that they do something when the request succeeds (even if it's just printing something at the console)


