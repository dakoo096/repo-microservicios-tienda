import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "../store/auth";
import Home from "../views/Home.vue";
import Products from "../views/Products.vue";
import Cart from "../views/Cart.vue";
import Checkout from "../views/Checkout.vue";
import AddProductView from "../views/AddProductView.vue";
import VentasView from "../Ventas/VentasView.vue";

const routes = [
  { path: "/", component: Home },
  { path: "/productos", component: Products },
  { path: "/carrito", component: Cart },
  { path: "/checkout", component: Checkout },
  { path: "/login", component: () => import("../views/Login.vue") },
  {
    path: "/agregarProducto",
    component: AddProductView,
    meta: { requiresAdmin: true },
  },
  {
    path: "/ventas",
    component: VentasView,
    meta: { requiresAdmin: true },
  },
  {
    path: "/editar/:id",
    component: () => import("../views/EditProductView.vue"),
    meta: { requiresAdmin: true },
  },
  {
    path: "/mis-compras",
    component: () => import("../views/MisComprasView.vue"),
  },
  {
    path: "/admin",
    component: () => import("../views/AdminView.vue"),
    meta: { requiresAdmin: true },
  },
  {
    path: "/register",
    component: () => import("../views/Register.vue"),
  },
  {
  path: '/productos/:codigo',
  name: 'ProductoDetalle',
  component: () => import("../views/ProductoDetalle.vue"),
  props: true
}
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  const auth = useAuthStore();

  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    next("/login");
  } else if (to.meta.requiresAdmin && !auth.isAdmin) {
    next("/");
  } else {
    next();
  }
});

export default router;
