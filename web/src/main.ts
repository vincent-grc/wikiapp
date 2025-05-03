import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import * as Icons from '@ant-design/icons-vue';
import axios from "axios";
import {Tool} from "@/utils/tool";

axios.defaults.baseURL = process.env.VUE_APP_SERVER;

/**
 * axios interceptor
 */
axios.interceptors.request.use(function (config) {
    console.log('Request parameters：', config);
    const token = store.state.user.token;
    if (Tool.isNotEmpty(token)) {
        config.headers.token = token;
        console.log("Request headers to add tokens:", token);
    }
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('Return result：', response);
    return response;
}, error => {
    console.log('Return error：', error);
    // const response = error.response;
    // const status = response.status;
    // if (status === 401) {
    //     // If status code is 401, redirect to home
    //     console.log("Not logged in, redirect to home");
    //     store.commit("setUser", {});
    //     message.error("Not logged in or log in time out");
    //     router.push('/');
    // }
    return Promise.reject(error);
});

const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');

const icons: any = Icons;
for (const i in icons) {
    app.component(i, icons[i]);
}

