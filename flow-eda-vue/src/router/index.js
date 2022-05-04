import {createRouter, createWebHashHistory} from "vue-router";
import Home from "../views/Home.vue";
import Flows from "../views/Flows.vue";
import Editor from "../views/Editor.vue";
import Logs from "../views/Logs.vue";

const routes = [
    {
        path: '/',
        redirect: '/flows'
    }, {
        path: "/",
        name: "Home",
        component: Home,
        children: [
            {
                path: "/flows",
                name: "Flows",
                meta: {
                    title: '流程管理'
                },
                component: Flows
            },
            {
                path: "/logs",
                name: "Logs",
                meta: {
                    title: '日志管理'
                },
                component: Logs
            },
            {
                path: "/flows/editor",
                name: "Editor",
                meta: {
                    title: '流程编辑器'
                },
                component: Editor,
                props: route => ({flowId: route.query.flowId})
            }
        ]
    }
];

const router = createRouter({
    history: createWebHashHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    document.title = `flow-eda-vue`;
    next();
});

export default router;
