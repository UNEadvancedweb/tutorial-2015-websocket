
console.log("I was loaded")


window.getGibberish = () ->

  server = $("#i_server").val()
  howmany = $("#i_howmany").val()

  path = "http://" + server + "/gibberish?n=" + howmany

  xhr = new XMLHttpRequest()
  xhr.onreadystatechange = () ->
    if xhr.readyState == 4
      arr = JSON.parse(xhr.responseText)
      gibberish = ""
      for obj in arr
        console.log(obj)
        gibberish += "#{obj.subject} #{obj.adverb} #{obj.verb} #{obj.adjective} #{obj.object}\n"
      $("#gibber").val(gibberish)


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
