declare namespace API {
  type ApiResult = {
    result?: any[];
    error?: string;
    message?: string;
    status?: number;
  };

  type Flow = {
    id?: string;
    name?: string;
    description?: string;
    status?: string;
    createDate?: string;
    updateDate?: string;
  };

  type Log = {
    type?: string;
    path?: string;
    date?: string;
    flow?: string;
    size?: number;
  };

  type Node = {
    id: string;
    flowId: string;
    left?: string;
    top?: string;
    nodeName?: string;
    typeId?: number;
    from?: string;
    to?: string;
    remark?: string;
    params?: object;
    payload?: object;
    status?: string;
    error?: string;
    output?: object;
    nodeType?: API.NodeType;
  };

  type NodeType = {
    id: number;
    type: string;
    typeName?: string;
    svg?: string;
    background?: number;
    params?: any[];
    description?: string;
  };
}
