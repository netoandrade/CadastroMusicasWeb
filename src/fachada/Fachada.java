package fachada;

import java.util.List;

import basicas.Artista;
import basicas.Musica;
import negocio.Controlador;
import negocio.NegocioException;

public class Fachada implements IFachada {

	private static IFachada fachada;
	private Controlador controlador;
	
	private Fachada(){
		this.controlador = new Controlador();
	}
	
	@Override
	public void inserir(Artista entidade) {
		controlador.inserir(entidade);
	}

	public static IFachada getInstancia(){
		if (fachada == null){
			fachada = new Fachada();
		}
		return fachada;
	}

	public List<Artista> consultarTodosArtistas() {
		return controlador.consultarTodosArtistas();
	}

	@Override
	public void excluir(Artista artista) throws NegocioException {
		controlador.excluir(artista);
	}

	@Override
	public void alterar(Artista entidade) {
		controlador.alterar(entidade);
	}

	@Override
	public List<Musica> consultarTodasMusicas() {
		return controlador.consultarTodasMusicas();
	}

	@Override
	public void inserirMusica(Musica entidade) {
		controlador.inserir(entidade);
	}

	@Override
	public void alterarMusica(Musica entidade) {
		controlador.alterar(entidade);
	}

	@Override
	public void removerMusica(Musica entidade) {
		controlador.remover(entidade);
	}

	public Musica consultarMusicaPorId(Integer id) {
		return controlador.consultarMusicaPorId(id);
	}

	@Override
	public List<Musica> pesquisarMusicas(Musica exemplo) {
		return controlador.pesquisarMusicas(exemplo);
	}

	public Artista consultarArtistaPorId(Integer id) {
		return controlador.consultarArtistaPorId(id);
	}
}
