// services/axiosConfig.ts
import axios from "axios";

const axiosInstance = axios.create({
  baseURL: "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

// Interceptor para agregar token y personaId en cada request
axiosInstance.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  const personaId = localStorage.getItem("personaId");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  if (personaId) {
    // Asegurarse que sea number y no string con comillas
    config.headers["X-Persona-Id"] = Number(personaId);
  }

  return config;
});

export default axiosInstance;