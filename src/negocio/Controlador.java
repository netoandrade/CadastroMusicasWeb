package negocio;

import java.util.List;

import basicas.Artista;
import basicas.Musica;
import dados.geral.ArtistaDAO;
import dados.geral.IArtistaDAO;
import dados.geral.IMusicaDAO;
import dados.geral.MusicaDAO;

public class Controlador {

	private IArtistaDAO artistaDAO;
	private IMusicaDAO musicaDAO;
	
	public Controlador(){
		this.artistaDAO = new ArtistaDAO();
		this.musicaDAO = new MusicaDAO();
	}
	

	public List<Artista> consultarTodosArtistas() {
		return artistaDAO.consultarTodos();
	}

	public void inserir(Artista entidade) {
		artistaDAO.inserir(entidade);
	}
	
	public void excluir(Artista artista) throws NegocioException{
		List<Musica> resultado = musicaDAO.pesquisarMusicasPorArtista(artista);
		if (resultado.isEmpty()){
			artistaDAO.remover(artista);	
		} else {
			throw new NegocioException(resultado.size() + " músicas cadastradas");
		}
		
	}

	public void alterar(Artista entidade) {
		artistaDAO.alterar(entidade);
	}

	public void inserir(Musica entidade) {
		musicaDAO.inserir(entidade);
	}

	public void alterar(Musica entidade) {
		musicaDAO.alterar(entidade);
	}

	public void remover(Musica entidade) {
		musicaDAO.remover(entidade);
	}

	public Musica consultarMusicaPorId(Integer id) {
		return musicaDAO.consultarPorId(id);
	}

	public List<Musica> pesquisarMusicas(Musica exemplo) {
		return musicaDAO.pesquisar(exemplo);
	}

	public List<Musica> consultarTodasMusicas() {
		return musicaDAO.consultarTodos();
	}

	public Artista consultarArtistaPorId(Integer id) {
		return artistaDAO.consultarPorId(id);
	}
}
