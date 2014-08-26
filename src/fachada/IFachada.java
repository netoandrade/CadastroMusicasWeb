package fachada;

import java.util.List;

import negocio.NegocioException;
import basicas.Artista;
import basicas.Musica;

public interface IFachada {

	public  void inserir(Artista entidade);
	public List<Artista> consultarTodosArtistas();
	public void excluir(Artista artista) throws NegocioException;
	public void alterar(Artista entidade);
	public List<Musica> pesquisarMusicas(Musica exemplo);
	public void removerMusica(Musica entidade);
	public void alterarMusica(Musica entidade);
	public void inserirMusica(Musica entidade);
	public List<Musica> consultarTodasMusicas();
	public Artista consultarArtistaPorId(Integer id);
}
