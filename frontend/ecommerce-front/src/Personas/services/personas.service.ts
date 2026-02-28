import api from "../../services/api";

export const actualizarPersona = async (personaForm: any) => {
  const { data } = await api.put(
    "/ms-personas/personas/me",
    personaForm
  );

  return data;
};

export const eliminarPersona = async (id: number) => {
  await api.delete(`/ms-personas/personas/${id}`);
};