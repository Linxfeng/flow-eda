export const defaultSetting = {
  grid: [10, 10],
  // 动态锚点、位置自适应
  Anchors: ['TopCenter', 'RightMiddle', 'BottomCenter', 'LeftMiddle'],
  Container: 'flow',
  // 连线的样式 StateMachine、Flowchart
  // 有四种默认类型：Bezier（贝塞尔曲线），Straight（直线），Flowchart（流程图），State machine（状态机）
  Connector: ['Flowchart', { cornerRadius: 5, alwaysRespectStubs: true, stub: 5 }],
  // 鼠标不能拖动删除线
  ConnectionsDetachable: false,
  // 删除线的时候节点不删除
  DeleteEndpointsOnDetach: false,
  // 连线的端点
  Endpoint: [
    'Rectangle',
    {
      height: 10,
      width: 10,
    },
  ],
  // 线端点的样式
  EndpointStyle: {
    fill: 'rgba(255,255,255,0)',
    outlineWidth: 1,
  },
  //是否打开jsPlumb的内部日志记录
  LogEnabled: false,
  // 绘制线
  PaintStyle: {
    stroke: '#409eff',
    strokeWidth: 2,
  },
  HoverPaintStyle: { stroke: '#ff00cc', strokeWidth: 2 },
  // 绘制箭头
  Overlays: [
    [
      'Arrow',
      {
        width: 8,
        length: 8,
        location: 1,
      },
    ],
  ],
  RenderMode: 'svg',
};

// jsplumb连接参数
export const connectOptions = {
  isSource: true,
  isTarget: true,
  // 动态锚点、提供了4个方向 Continuous、AutoDefault
  anchor: ['TopCenter', 'RightMiddle', 'BottomCenter', 'LeftMiddle'],
};

export const makeOptions = {
  //触发连线的区域
  filter: '.node-anchor',
  //"span"表示标签，".className"表示类，"#id"表示元素id
  filterExclude: false,
  anchor: ['TopCenter', 'RightMiddle', 'BottomCenter', 'LeftMiddle'],
  allowLoopback: false,
};
