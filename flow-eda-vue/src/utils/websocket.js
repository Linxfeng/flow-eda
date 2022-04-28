let ws = {};

// 建立连接，监听消息并进行回调
async function onOpen(id, callback) {
    if (Object.keys(ws) === 0 || !ws[id]) {
        const url = 'ws://localhost:8088/ws/flow/' + id + '/nodes';
        const socket = new WebSocket(url);
        socket.onmessage = function (msg) {
            callback(msg.data);
        };
        ws[id] = socket;
    }
}

// 关闭连接
function onClose(id) {
    if (ws && ws[id]) {
        try {
            ws[id].close();
        } catch (ignore) {
        }
        ws[id] = null;
    }
}

export {onOpen, onClose}
