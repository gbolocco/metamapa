package ar.edu.utn.frba.dds.compartido.servicios.agregador;

import ar.edu.utn.frba.dds.dominio.filtros.Filtro;
import ar.edu.utn.frba.dds.dominio.fuentes.Fuente;
import ar.edu.utn.frba.dds.dominio.fuentes.TipoFuente;
import ar.edu.utn.frba.dds.dominio.hechos.Hecho;

import java.util.ArrayList;
import java.util.List;

//el crontab llama a la funcion cargarHechos desde fuente cada cierto tiempo para poder ir
//actualizando esa cache, se parte de la base que el hecho no va a estar en la mimsa fuente 2 veces
//con esa logica modifico los algortimos

public class ServicioDeAgregacion {
    private List<Fuente> fuentes = new ArrayList<>();

    private List<Hecho> hechosCache = new ArrayList<>();

    public void agregarFuente(Fuente fuente) {
        if(fuente.getTipoFuente()!=TipoFuente.FUENTE_AGREGADORA){
            return;
        }
        this.fuentes.add(fuente);
    }

    public List<Fuente> getFuentes() {
        return fuentes;
    }

    public  List<Hecho> getHechos(List<Filtro> filtros) {
        return this.hechosCache.stream().filter(hecho -> filtros.stream().allMatch(f -> f.cumpleFiltro(hecho)) ).toList();
    }

    // me traigo a la cache todos los hechos de todas las fuentes
    public void cargarHechosDesdeFuentes() {
        this.hechosCache = this.fuentes
                .stream()
                .flatMap(fuente -> fuente.obtenerHechos(new ArrayList<>()).stream())
                .toList();
    }

    public Integer getCantFuentes(){
        return this.fuentes.size();
    }
}
