import Vue from "vue"
import Router from "vue-router"
import Login from "./views/login.vue"
import Admin from "./views/admin.vue"
import Welcome from "./views/admin/welcome.vue"
import Chapter from "./views/admin/chapter.vue"
import Section from "./views/admin/section.vue"
import Course from "./views/admin/course.vue"
import Category from "./views/admin/category.vue"
import Teacher from "./views/admin/teacher.vue"
import File from "./views/admin/file.vue"
import Content from "./views/admin/content.vue"
import User from "./views/admin/user"
import Resource from "./views/admin/resource"
import Role from "./views/admin/role"

Vue.use(Router);

export default new Router({
    mode: "history",
    base: process.env.BASE_URL,
    routes: [{
        path: "*",
        redirect: "/login",
    }, {
        path: "",
        redirect: "/login",
    }, {
        path: "/login",
        component: Login
    }, {
        path: "/",
        name:"admin",
        component: Admin,
        meta:{
          loginRequire:true,    //登录拦截
        },
        children:[{
            path: "welcome", //子路由不用 / 标志
            name:"welcome",
            component: Welcome,
        },{
            path: "business/chapter",
            name:"business/chapter",
            component: Chapter,
        },{
            path: "business/section",
            name:"business/section",
            component: Section,
        },{
            path: "business/course",
            name:"business/course",
            component: Course,
        },{
            path: "business/category",
            name:"business/category",
            component: Category,
        },{
            path: "business/teacher",
            name:"business/teacher",
            component: Teacher,
        },{
            path: "file/file",
            name:"file/file",
            component: File,
        },{
            path: "business/content",
            name:"business/content",
            component: Content,
        },{
            path: "system/user",
            name:"system/user",
            component: User,
        },{
            path: "system/resource",
            name:"system/resource",
            component: Resource,
        },{
            path: "system/role",
            name:"system/role",
            component: Role,
        }]
            }]
        })