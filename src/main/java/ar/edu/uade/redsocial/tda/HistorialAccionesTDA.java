package ar.edu.uade.redsocial.tda;

public interface HistorialAccionesTDA {
    void Inicializar();
    void RegistrarAccion(int accion);
    void DeshacerUltimaAccion();
    int UltimaAccion();
    boolean Vacio();
}
