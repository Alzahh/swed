import {createWebHistory, createRouter} from "vue-router";
// import Home from "./components/Home.vue";
import Signin from "@/pages/Signin";
import {isAdmin, isAuthenticated} from "@/utils/utils";
import AdminPage from "@/pages/AdminPage";
import AdminEdit from "@/pages/AdminEdit";
import CurrencyExchange from "@/pages/CurrencyExchange";
import SignUp from "@/pages/SignUp";
import ProfilePage from "@/pages/ProfilePage";

const routes = [
    {
        path: "/exchange", component: CurrencyExchange,
    }, {
        path: "/login", component: Signin,
    }, {
        path: "/admin/users", component: AdminPage,
    }, {
        path: "/admin/users/edit/:id", component: AdminEdit,
    }, {
        path: "/register", component: SignUp,
    }, {
        path: "/info", component: ProfilePage,
    }
];

const router = createRouter({
    history: createWebHistory(), routes,
});

router.beforeEach((to, from, next) => {
    const publicPages = ['/login', '/register'];
    const authRequired = !publicPages.includes(to.path);
    const adminRoleRequired = to.path.includes("admin")
    const authNotRequired = publicPages.includes(to.path);

    if ((authRequired || adminRoleRequired) && !isAuthenticated()) {
        console.log("to login page")
        next('/login');
    } else if (adminRoleRequired && !isAdmin()) {
        console.log("Not admin")
        next('/exchange')
    } else if (isAdmin() && authNotRequired) {
        console.log("to main page")
        next('/admin/users')
    } else if (isAuthenticated() && authNotRequired) {
        console.log("to main page")
        next('/exchange')
    } else {
        next();
    }
});

export default router;