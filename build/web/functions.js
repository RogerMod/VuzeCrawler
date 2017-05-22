var socket = new WebSocket("ws://localhost:8080/Crawleer/actions");
socket.onmessage = onMessage;

// DOM element where the Timeline will be attached
var container = document.getElementById('visualization');
var container4graphs = document.getElementById('graphs');

// Create a DataSet (allows two way data-binding)
var items = new vis.DataSet([
]);
var names = ['Messages', 'Pings', 'Find Node', 'Find Value'];
var items2graphs = new vis.DataSet();
var groups = new vis.DataSet();

groups.add({
    id: 0,
    content: names[0],
    options: {
        drawPoints: {
            style: 'circle'
        }
    }
});
groups.add({
    id: 1,
    content: names[1],
    options: {
        drawPoints: {
            style: 'circle'
        }
    }
});

groups.add({
    id: 2,
    content: names[2],
    options: {
        drawPoints: {
            style: 'circle'
        }
    }
});


groups.add({
    id: 3,
    content: names[3],
    options: {
        drawPoints: {
            style: 'circle'
        }
    }
});

var options = {dataAttributes: ['tooltip', 'id'],
    //autoResize = true
    start: '2017-05-22T16:16:00',
    end:   '2017-05-22T16:16:30'

};
var options2 = {
    defaultGroup: 'ungrouped',
    legend: true,
    start: '2017-05-22T16:04:00',
    end:   '2017-05-22T16:05:00'
};

// Create a Timeline
var timeline = new vis.Timeline(container, items, null, options);
var graph2d = new vis.Graph2d(container4graphs, items2graphs, groups, options2);

function focus3() {
    var selection = timeline.getSelection();
    alert(selection);
}

function zoomPlus() {
    timeline.zoomIn(1);
}
function zoomMinus() {
    timeline.zoomOut(1);
}

function onMessage(event) {
    var device = JSON.parse(event.data);
    if (device.action === "add")
        addItem(device);
    else if (device.action === "graph") {
        addGraphItem(device);
    }
}

function addItem(element) {
    items.add([{id: element.id, content: element.type, start: element.timestamp}]);
}

function addGraphItem(element) {
    items2graphs.add([{x: element.x, y: element.y, group: element.group}]);
}

function formSubmit() {
    var type = document.getElementById("message_type").value;
    query_messages(type);
}

function query_messages(type) {
    var query = {
        type: "retrieve",
        object: type
    };
    socket.send(JSON.stringify(query));
}

function clearAll() {
    items.clear();
}