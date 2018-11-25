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
        this.state.consumerProgress = {};
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
        let theSame = this.state.messagesPerSecond === response.messagesPerSecond
                     && this.state.activeConsumers === response.activeConsumers
                     && this.state.queuedMessages  === response.queuedMessages
                    && JSON.stringify(this.state.consumerProgress) === JSON.stringify(response.consumerProgress);

        if (!theSame)
            this.setState({
                messagesPerSecond: response.messagesPerSecond,
                activeConsumers: response.activeConsumers,
                queuedMessages: response.queuedMessages,
                consumerProgress: response.consumerProgress
            });
    }

    render()
    {
        let progressRows = Object.keys(this.state.consumerProgress).map((key, index) => {
            let value = this.state.consumerProgress[key];
            // console.log(value);
            return (<tr key={index}>
                <td>{index + 1}</td>
                <td>{value.consumerKey}</td>
                <td>{value.workMessage}</td>
                <td>
                    <progress className="progress is-primary is-small" value={value.progress} max="100">{value.progress}%</progress>
                </td>
            </tr>);
        });

        for (let i = progressRows.length; i < this.state.activeConsumers; i++)
        {
            let n = i + 1;
            let row = (<tr key={n}>
                <td>{n}</td>
                <td className={'has-text-grey'}>idle</td>
                <td className={'has-text-grey'}>idle</td>
                <td>
                    <progress className="progress is-primary is-small" value="0" max="100">0%</progress>
                </td>
            </tr>);
            progressRows.push(row);
        }

        return (
            <div>
                Messages Per Second: {this.state.messagesPerSecond}
                <br />Active Consumers: {this.state.activeConsumers}
                <br />Queued Messages: {this.state.queuedMessages}

                <table className={'table is-narrow is-striped'}>
                    <thead>
                        <tr>
                            <th></th>
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