# React.js tutorial

In this tutorial, you're going to write React.js components, using JSX, to render gibberish sent from the server.

First, clone the repository and open it with IntelliJ and sbt


## What's there

* The server side of the application has already been written.

  A GET request to /gibberish?n=5 will return a JSON array of five gibberish objects.
  
```json
[
  { 
    "subject":"Edwin", 
    "adverb":"grudgingly", 
    "verb":"dropped", 
    "adjective":"polkadot", 
    "object":"mice"
  },
  // etc
]
```

* Look in `project/plugins.sbt` to see which plugins are loaded

* Look in `app/assets/javascripts` to see the client side code. There are two files: 

    * `xhrexample.coffee` performs AJAX requests (but is incomplete)

    * `gibberish.jsx` is waiting for your React.js code

* Look in `app/views/index.html` 

  Check that both scripts are being loaded.


## Add something to prove React is running

Let's begin by just rendering a React.js component

In `gibberish.jsx`, create a function called `rerender()`. This will re-render your React.js app, in order to update the view.

```js
var rerender = function() {
  React.render(<GibberishApp />, mountNode);
}
rerender();
```

Before this works, we'd better declare a `GibberishApp` component. Let's make it empty to start with

```js
var GibberishApp = React.createClass({
  render: function() {
    return (
      <div>
        Yes, this rendered!
      </div>
    );
  }
});
```

Reload the page to check that the React code renders.

## Make the client fetch some data

1. At the moment, `window.getGibberish()` doesn't do anything with the response except parse it.

  Make it store the data in `window.gibberish`, and call your `rerender()` function, to update the UI. (At the moment the UI won't have changed, but let's wire it in while we're here)

2. Add a button to the GibberishApp's render method. 

   Write a `handleSubmit(e)` function in the component, which will call `window.getGibberish(5)`.

   Wire the button to the `handleSubmit(e)` event handler
   
3. Reload, and using the Network tab of the browser developer tools, check that when you press the button, the request is made.

## Create a Gibberish component

5. Create the Gibberish component that can display one gibberish object. 

```js
var Gibberish = React.createClass({
  render: function() {
    var obj = this.props.gibberish
    return <div className="gibberish">{obj.subject} {obj.adverb} {obj.verb} {obj.adjective} {obj.object}</div>;
  }
});
```

6. Change your `GibberishApp` component to show one `Gibberish` (the first element of the array). Refresh and check it works.

### Create a GibberishList component

7. Next, create a `GibberishList` component, that takes `window.gibberish`, and renders a `Gibberish` for each entry in the array. 

  This should look something like this:
  
```js
var GibberishList = React.createClass({
  render: function() {
    return <ul>{this.props.gibberishList.map(function(item) {
      return <Gibberish gibberish={item} />
    })}</ul>;
  }
});
```

8. Now alter your `GibberishApp` component to show the `GibberishList` instead of just the first Gibberish entry. Reload and check it works

### Add state to GibberishApp

9. The last step is to add a form field to `GibberishApp` that lets you change the number of gibberish items to retrieve from the server.

   This step is left as an exercise for the reader, but you may find looking at the code from the TODO app example in the lecture helpful
   
    https://github.com/UNEadvancedweb/2015_reactjs_example/blob/master/app/assets/javascripts/gibberish.jsx  


