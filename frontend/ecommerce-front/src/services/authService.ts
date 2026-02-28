// services/authService.ts
import axios from "./axiosConfig";

export interface LoginResponse {
  token: string;
  username: string;
  rol: "ADMIN" | "CLIENTE";
}

export const authService = {
  async login(username: string, password: string): Promise<LoginResponse> {
    const response = await axios.post<LoginResponse>("/login", {
      username,
      password,
    });

    return response.data;
  },
};
