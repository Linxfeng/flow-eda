let ws = {};
let logWs = {};
let logContentWs = {};

// 建立连接，监听消息并进行回调
async function onOpen(id, callback) {
    if (Object.keys(ws).length === 0 || !ws[id]) {
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

// 监听日志消息
function onOpenLogs(id, callback) {
    if (Object.keys(logWs).length === 0 || !logWs[id]) {
        const url = 'ws://localhost:8082/ws/flow/' + id + '/logs';
        const socket = new WebSocket(url);
        socket.onmessage = function (msg) {
            callback(msg.data);
        };
        logWs[id] = socket;
    }
}

function onCloseLogs(id) {
    if (logWs && logWs[id]) {
        try {
            logWs[id].close();
        } catch (ignore) {
        }
        logWs[id] = null;
    }
}

// 接收日志文件内容
function onOpenLogDetail(path, callback) {
    if (Object.keys(logContentWs).length === 0 || !logContentWs[path]) {
        const url = 'ws://localhost:8082/ws/logs/content/' + path.replaceAll("/", ":");
        const socket = new WebSocket(url);
        socket.onmessage = function (msg) {
            callback(msg.data);
        };
        logContentWs[path] = socket;
    }
}

function onCloseLogDetail(path) {
    if (logContentWs && logContentWs[path]) {
        try {
            logContentWs[path].close();
        } catch (ignore) {
        }
        logContentWs[path] = null;
    }
}

export {onOpen, onClose, onOpenLogs, onCloseLogs, onOpenLogDetail, onCloseLogDetail}
