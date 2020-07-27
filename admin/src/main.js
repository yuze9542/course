import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'

Vue.config.productionTip = false
Vue.prototype.$ajax = axios

/**
 * axios拦截器
 */
//请求
axios.interceptors.request.use(function (config) {
  console.log("请求:",config);
  return config
},error => {});
//返回
axios.interceptors.response.use(function (response) {
  console.log("返回:",response);
  return response
},error => {});
new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
