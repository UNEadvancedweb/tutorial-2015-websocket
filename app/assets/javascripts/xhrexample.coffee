
console.log("I was loaded")

window.gibberish = []

window.getGibberish = (state) ->

  path = "http://" + state.server + "/gibberish?n=" + state.num

  xhr = new XMLHttpRequest()
  xhr.onreadystatechange = () ->
    if xhr.readyState == 4
      arr = JSON.parse(xhr.responseText)
      # Now you need to make it do something with the array...

  console.log("asking for gibberish")
  xhr.open("GET", path, true)
  xhr.send()

