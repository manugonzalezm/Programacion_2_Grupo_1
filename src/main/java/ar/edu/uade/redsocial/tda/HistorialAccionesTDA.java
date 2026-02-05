package ar.edu.uade.redsocial.tda;

public interface HistorialAccionesTDA {
    void Inicializar();
    void RegistrarAccion(int accion);
    void Deshacer();
    int UltimaAccion();
    boolean Vacio();
}
