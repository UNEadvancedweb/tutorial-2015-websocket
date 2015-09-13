
console.log("I was loaded")

window.gibberish = []

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

