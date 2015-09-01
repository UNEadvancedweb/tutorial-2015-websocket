var mountNode = document.getElementById('renderme')


items = [ "foo" ]

var TodoItem = React.createClass({
  render: function() {
    return <ul>{this.props.item}</ul>;
  }
});

var TodoList = React.createClass({
  render: function() {
    return <ul>{this.props.items.map(function(item) {
      return <TodoItem item={item} />
    })}</ul>;
  }
});

var TodoApp = React.createClass({
  getInitialState: function() {
    return {text : ""};
  },
  onChange: function(e) {
    this.setState({text: e.target.value});
  },
  handleSubmit: function(e) {
    e.preventDefault();
    items = items.concat([this.state.text]);
    this.forceUpdate();
  },
  render: function() {
    return (
      <div>
        <h3>TODO</h3>
        <TodoList items={items} />
        <form onSubmit={this.handleSubmit}>
          <input onChange={this.onChange} value={this.state.text} />
          <button>{'Add #' + (items.length + 1)}</button>
        </form>
      </div>
    );
  }
});

React.render(<TodoApp />, mountNode);