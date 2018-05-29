import React from "react";
import ReactDOM from "react-dom";

import moment from 'moment'

function fetchBatchFiles() {
  return fetch("/api/batchFiles")
    .then(resp => resp.json());
}


function BatchFileUpload() {
  return (
    <form method="post" action="/api/batchFiles/" encType="multipart/form-data">
      <input type="file" name="batchfile" />
      <label>Type
        <select name="batchtype">
          <option id="fest">FEST file</option>
          <option id="random">Random file</option>
        </select>
      </label>
      <input type="submit" />
    </form>
  );
}

function BatchFileStatus(props) {
  const {batchfile: {
    batchtype, startTime, status, progress
  }} = props;
  return <li>
    <strong>{batchtype} started at {moment(startTime).fromNow()}: </strong>
    {status} ({progress})
    </li>;
}

class BatchFileList extends React.Component {
  state = {batchfiles: []};

  componentDidMount() {
    fetchBatchFiles().then(batchfiles => {
      console.log(batchfiles);
      this.setState({batchfiles})
    })
  }

  render() {
    return (
      <div>
        <h3>Current batch jobs</h3>
        <ul>
          {this.state.batchfiles.map(f => <BatchFileStatus key={f.id} batchfile={f} />)}
        </ul>
      </div>
    );
  }
}

class ErrorBoundary extends React.Component {
  constructor(props) {
    super(props);
    this.state = { hasError: false };
  }

  componentDidCatch(error, info) {
    this.setState({ hasError: true });
    console.error(error, info);
  }

  render() {
    if (this.state.hasError) {
      return <h1>Something went wrong.</h1>;
    }
    return this.props.children;
  }
}

class App extends React.Component {
  state = {};

  componentDidMount() {
    this.setState({hash: window.location.hash});
    window.addEventListener("hashchange", () => {
      this.setState({hash: window.location.hash})
    })
  }

  render() {
    const {hash} = this.state;

    let content;
    if (hash === "#/upload") {
      content = <BatchFileUpload />;
    } else if (hash === "#/list") {
      content = <BatchFileList />;
    } else {
      content = <BatchFileList />;
    }
    return (
      <div className="App">
        <header className="App-header">
          <h1 className="App-title">Welcome to Batch server</h1>
        </header>
        <nav>
          <ul>
            <li>
              <a href="#/upload">Upload file</a>
            </li>
            <li>
              <a href="#/list">See status</a>
            </li>
          </ul>
        </nav>
        <ErrorBoundary>
          {content}
        </ErrorBoundary>
      </div>
    );
  }
}

ReactDOM.render(<App />, document.getElementById("root"));
