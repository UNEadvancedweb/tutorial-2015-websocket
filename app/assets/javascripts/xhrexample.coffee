
console.log("I was loaded")


window.getGibberish = () ->

  server = $("#i_server").val()
  howmany = $("#i_howmany").val()

  path = "http://" + server + "/gibberish?n=" + howmany

  xhr = new XMLHttpRequest()
  console.log("asking for gibberish")
  xhr.open("GET", path, true)
  xhr.send()



window.postGibberish = () ->

  server = $("#i_server").val()
  path = "http://" + server + "/gibberish"

  xhr = new XMLHttpRequest()
  console.log("posting gibberish")
  xhr.open("POST", path, true)
  xhr.setRequestHeader("Content-Type", "application/json")

  g = {
    subject: "The browser"
    adverb: "easily"
    verb: "communicated with the"
    adjective: "very simple"
    object: "server"
  }

  json = JSON.stringify(g)

  xhr.send(json)
