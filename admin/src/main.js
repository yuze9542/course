import Vue from 'vue'
import App from './App.vue'
import router from './router'
import axios from 'axios'
import filter from './filter/filter'
Vue.config.productionTip = false;
Vue.prototype.$ajax = axios;

//解决前后端 sessionId 不一样的问题
axios.defaults.withCredentials = true

/**
 * axios拦截器
 */
//请求
axios.interceptors.request.use(function (config) {
  console.log("请求:",config);
  let token = Tool.getLoginUser().token;
  if(Tool.isNotEmpty(token)){
    config.headers.token = token;
    console.log("请求参数增加token",token);
  }
  return config
},error => {});
//返回
axios.interceptors.response.use(function (response) {
  console.log("返回:",response);
  return response
},error => {});

// 全局过滤器
Object.keys(filter).forEach(key =>{
  Vue.filter(key,filter[key]);
});

// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    return item.meta.loginRequire
  })) {
    let loginUser = Tool.getLoginUser();
    if (Tool.isEmpty(loginUser)) {
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});


new Vue({
  router,
  render: h => h(App),
}).$mount('#app');

console.log("环境",process.env.NODE_ENV)
