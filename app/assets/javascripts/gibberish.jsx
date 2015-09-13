var mountNode = document.getElementById('renderme')

var Gibberish = React.createClass({
  render: function() {
    var obj = this.props.gibberish
    return <div className="gibberish">{obj.subject} {obj.adverb} {obj.verb} {obj.adjective} {obj.object}</div>;
  }
});

var GibberishList = React.createClass({
  render: function() {
    return <ul>{this.props.gibberishList.map(function(item) {
      return <Gibberish gibberish={item} />
    })}</ul>;
  }
});

var GibberishApp = React.createClass({
  getInitialState: function() {
    return {
      server: "localhost:9000",
      num: 5
    }
  },
  setServer: function(e) {
    var s = this.state
    s["server"] = e.target.value
    this.setState(s)
  },
  setNum: function(e) {
    var s = this.state
    s["num"] = e.target.value
    this.setState(s)
  },
  handleSubmit: function(e) {
    e.preventDefault()
    window.getGibberish(this.state)
  },
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
      </div>
    );
  }
});



var rerender = function() {
  React.render(<GibberishApp />, mountNode);
}
rerender();