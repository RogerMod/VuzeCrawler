var socket = new WebSocket("ws://localhost:8080/VuzeCrawler/actions");
socket.onmessage = onMessage;

// DOM element where the Timeline will be attached
var container = document.getElementById('visualization');
var container4graphs = document.getElementById('graphs');

// Create a DataSet (allows two way data-binding)
var items = new vis.DataSet([
]);
var items2graphs = [
    {x: '2017-05-12T13:45:28', y: 10},
    {x: '2017-05-12T13:46:28', y: 5},
    {x: '2017-05-12T13:47:28', y: 12}
  ];
var dataset = new vis.DataSet(items2graphs);



// Configuration for the Timeline
var options = {dataAttributes: ['tooltip', 'id'],
    //autoResize = true
    start: '2017-05-12T13:45:00',
    end: '2017-05-12T13:59:00'
    //rollingMode: true
};
  var options2 = {
    start: '2017-05-12T13:45:00',
    end: '2017-05-12T13:59:00'
  };


// Create a Timeline
var timeline = new vis.Timeline(container, items, null, options);
var graph2d = new vis.Graph2d(container4graphs, dataset, options2);

/*
timeline.on('select', function (properties) {
  alert('selected items: ' + properties.items[0].content );
});
*/
function focus3() {
    var selection = timeline.getSelection();
    alert(selection);
  };

function zoomPlus() {
    timeline.zoomIn(1);
}
function zoomMinus() {
    timeline.zoomOut(1);
}

function onMessage(event) {
    var device = JSON.parse(event.data);
    if (device.action === "add") {
        addItem(device);
    }
}

function addItem(element) {
    items.add([{id: element.id, content: element.type, start: element.timestamp}]);
}

function formSubmit() {
    var type = document.getElementById("message_type").value;
    query_messages(type);
}

function query_messages(type){
     var query = { 
        type: "retrieve",
        object: type
    };
    socket.send(JSON.stringify(query));
}

function clearAll(){
    items.clear();
}