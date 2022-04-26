let flowWs = {};
let nodeWs = {};

// 监听流程状态信息
async function onOpenFlow(id, callback) {
    if (Object.keys(flowWs) === 0 || !flowWs[id]) {
        const url = 'ws://localhost:8088/ws/flow/status/' + id;
        const socket = new WebSocket(url);
        socket.onmessage = function (msg) {
            callback(msg.data);
        };
        flowWs[id] = socket;
    }
}

// 监听节点信息
async function onOpenNode(id, callback) {
    if (Object.keys(nodeWs) === 0 || !nodeWs[id]) {
        const url = 'ws://localhost:8088/ws/flow/' + id + '/nodes';
        const socket = new WebSocket(url);
        socket.onmessage = function (msg) {
            callback(msg.data);
        };
        nodeWs[id] = socket;
    }
}

// 关闭连接
function onClose(id) {
    if (nodeWs && nodeWs[id]) {
        try {
            nodeWs[id].close();
        } catch (ignore) {
        }
        nodeWs[id] = null;
    }
    if (flowWs && flowWs[id]) {
        try {
            flowWs[id].close();
        } catch (ignore) {
        }
        flowWs[id] = null;
    }
}

export {onOpenFlow, onOpenNode, onClose}
