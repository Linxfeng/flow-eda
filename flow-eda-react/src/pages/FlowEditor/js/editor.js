import panzoom from 'panzoom';
import screenfull from 'screenfull';
import { message } from 'antd';

/** 更改连线状态 */
export const changeLineState = (nodeId, show, jsPlumbInstance) => {
  jsPlumbInstance.getAllConnections()?.forEach((line) => {
    if (line.targetId === nodeId || line.sourceId === nodeId) {
      const canvas = line.canvas;
      if (canvas) {
        if (show) {
          canvas.classList?.add('active');
        } else {
          canvas.classList?.remove('active');
        }
      }
    }
  });
};

/** 设置编辑器面板缩放 */
export const setPanZoom = (jsPlumbInstance, setLineStyle) => {
  const mainContainer = jsPlumbInstance.getContainer();
  const mainContainerWrap = mainContainer.parentNode;
  const pan = panzoom(mainContainer, {
    smoothScroll: false,
    bounds: true,
    zoomDoubleClickSpeed: 1,
    minZoom: 0.5,
    maxZoom: 2,
  });
  jsPlumbInstance.mainContainerWrap = mainContainerWrap;
  jsPlumbInstance.pan = pan;
  pan.on('zoom', (e) => {
    const { x, y, scale } = e.getTransform();
    jsPlumbInstance.setZoom(scale);
    //根据缩放比例，缩放对齐辅助线长度和位置
    setLineStyle({
      width: (1 / scale) * 100 + '%',
      height: (1 / scale) * 100 + '%',
      offsetX: -(x / scale),
      offsetY: -(y / scale),
    });
  });
  pan.on('panend', (e) => {
    const { x, y, scale } = e.getTransform();
    setLineStyle({
      width: (1 / scale) * 100 + '%',
      height: (1 / scale) * 100 + '%',
      offsetX: -(x / scale),
      offsetY: -(y / scale),
    });
  });
  // 设置平移时鼠标样式
  mainContainerWrap.style.cursor = 'grab';
  mainContainerWrap?.addEventListener('mousedown', function wrapMousedown(style) {
    style['cursor'] = 'grabbing';
    mainContainerWrap?.addEventListener('mouseout', function wrapMouseout(e) {
      e['cursor'] = 'grab';
    });
  });
  mainContainerWrap?.addEventListener('mouseup', function wrapMouseup(style) {
    style['cursor'] = 'grab';
  });
};

/** 编辑器工具栏的缩放功能，以绘制面板原点为基准，每次缩放25% */
export const zoomPan = async (command, jsPlumbInstance) => {
  const scale = jsPlumbInstance.getZoom();
  const max = jsPlumbInstance.pan?.getMaxZoom();
  const min = jsPlumbInstance.pan?.getMinZoom();
  let temp;
  if (command === 'in') {
    if (scale < max) {
      temp = scale + scale * 0.25;
    }
  } else if (command === 'out') {
    if (scale > min) {
      temp = scale - scale * 0.25;
    }
  } else if (command === 'full') {
    if (!screenfull.isEnabled) {
      message.warn('Your browser does not support full screen');
      return;
    }
    await screenfull.request(document.getElementById('flow-content'));
  } else if (command === 'reset') {
    temp = 1;
  }
  if (temp) {
    // 限制缩放范围
    if (temp > max) {
      temp = max;
    } else if (temp < min) {
      temp = min;
    }
    jsPlumbInstance.setZoom(temp);
    const flowDom = document.getElementById('flow');
    if (flowDom?.style.transform) {
      flowDom.style.transform = 'scale(' + temp + ')';
    }
  }
};
