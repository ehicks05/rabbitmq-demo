'use strict';

const e = React.createElement;

class ProgressTracker extends React.Component {
    constructor(props)
    {
        super(props);
        this.state = {};
        this.state.messagesPerSecond = 0;
        this.state.activeConsumers = 0;
        this.state.queuedMessages = 0;
        this.state.consumerProgress = [];
    }

    componentDidMount()
    {
        var stompClient = null;
        let it = this;

        function connect() {
            var socket = new SockJS('/progress');
            stompClient = Stomp.over(socket);
            stompClient.debug = null;
            stompClient.connect({}, function(frame) {
                // console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/messages', function(messageOutput) {
                    let body = JSON.parse(messageOutput.body);
                    // console.log(body);
                    it.handleData(body);
                });
            });
        }

        connect();
    }

    handleData(response)
    {
        this.setState({
            messagesPerSecond: response.messagesPerSecond,
            activeConsumers: response.activeConsumers,
            queuedMessages: response.queuedMessages,
            consumerProgress: response.consumerProgress
        });
    }

    render()
    {
        let progressRows = this.state.consumerProgress.map((value, index) => {
            return (<tr key={value.consumerKey}>
                <td>{value.consumerKey}</td>
                <td>{value.workMessage}</td>
                <td>
                    <progress className="progress is-primary is-small" value={value.progress} max="100">{value.progress}%</progress>
                </td>
            </tr>);
        });

        return (
            <div>
                Messages Per Second: {this.state.messagesPerSecond}
                <br />Active Consumers: {this.state.activeConsumers}
                <br />Queued Messages: {this.state.queuedMessages}

                <table className={'table is-narrow is-striped'}>
                    <thead>
                        <tr>
                            <th>thread</th>
                            <th>work message</th>
                            <th style={pStyle}>progress</th>
                        </tr>
                    </thead>
                    <tbody>
                        {progressRows}
                    </tbody>
                </table>
            </div>
        );
    }
}

const pStyle = {
    width: '50%'
};

const domContainer = document.querySelector('#react-component-container');
ReactDOM.render(e(ProgressTracker), domContainer);