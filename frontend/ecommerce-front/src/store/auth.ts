import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { useCartStore } from "./cart";

export type UserRole = "ADMIN" | "CLIENTE" | null;

export const useAuthStore = defineStore("auth", () => {
  const token = ref<string | null>(localStorage.getItem("token"));
  const role = ref<UserRole>(
    (localStorage.getItem("userRole") as UserRole) || null,
  );
  const user = ref<string | null>(localStorage.getItem("userName"));
  const personaId = ref<number | null>(
    Number(localStorage.getItem("personaId")) || null,
  );

  const isAdmin = computed(() => role.value === "ADMIN");
  const isClient = computed(() => role.value === "CLIENTE");
  const isAuthenticated = computed(() => !!token.value);

  const setAuth = (
    newToken: string,
    newRole: UserRole,
    userName: string,
    newPersonaId: number,
  ) => {
    token.value = newToken;
    role.value = newRole;
    user.value = userName;
    personaId.value = newPersonaId;

    localStorage.setItem("token", newToken);
    if (newRole) localStorage.setItem("userRole", newRole);
    localStorage.setItem("userName", userName);
    localStorage.setItem("personaId", newPersonaId.toString());

    const cartStore = useCartStore();
    cartStore.fetchCarrito();
  };

  const logout = () => {
    token.value = null;
    role.value = null;
    user.value = null;
    personaId.value = null;

    localStorage.removeItem("token");
    localStorage.removeItem("userRole");
    localStorage.removeItem("userName");
    localStorage.removeItem("personaId");
    localStorage.removeItem("cart_id");

    const cartStore = useCartStore();
    cartStore.clearLocalData();
  };

  return {
    token,
    role,
    user,
    personaId,
    isAdmin,
    isClient,
    isAuthenticated,
    setAuth,
    logout,
  };
});
