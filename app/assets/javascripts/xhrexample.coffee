
console.log("I was loaded")

window.gibberish = []

window.received = []

window.getGibberish = (state) ->

  path = "http://" + state.server + "/gibberish?n=" + state.num

  xhr = new XMLHttpRequest()
  xhr.onreadystatechange = () ->
    if xhr.readyState == 4
      arr = JSON.parse(xhr.responseText)
      window.gibberish = arr
      console.log(arr)
      rerender();

  console.log("asking for gibberish")
  xhr.open("GET", path, true)
  xhr.send()


window.getSocket = () ->
  websocket = new WebSocket("ws://#{window.location.host}/websocket?topic=Algernon");
  websocket.onmessage = (msg) ->
    console.log("Received a message over the websocket:")
    console.log(msg)
    console.log("---")
    json = JSON.parse(msg.data)
    window.received.push(json)
    rerender()


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
