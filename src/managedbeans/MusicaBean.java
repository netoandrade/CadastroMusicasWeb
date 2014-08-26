package managedbeans;

import java.util.List;

import javax.faces.bean.ManagedBean;

import fachada.Fachada;
import basicas.Artista;
import basicas.Musica;

@ManagedBean
public class MusicaBean {

	private Musica musica = new Musica();
	private Integer artistaSelecionado;

	public List<Artista> getColecaoArtistas() {
		return Fachada.getInstancia().consultarTodosArtistas();
	}

	public List<Musica> getColecaoMusicas() {
		return Fachada.getInstancia().consultarTodasMusicas();
	}

	public String salvar() {
		musica.setArtista(Fachada.getInstancia().consultarArtistaPorId(
				artistaSelecionado));
		if (musica.getCodigo() == null || musica.getCodigo() == 0) {
			musica.setCodigo(null);
			Fachada.getInstancia().inserirMusica(musica);
		} else {
			Fachada.getInstancia().alterarMusica(musica);
		}
		return "musica-listar.xhtml";
	}

	public String editar(Musica param) {
		this.musica = param;
		this.artistaSelecionado = param.getArtista().getCodigo();
		return "musica-salvar.xhtml";
	}

	public String excluir(Musica param) {
		Fachada.getInstancia().removerMusica(param);
		return "artista-listar.xhtml";
	}

	public Musica getMusica() {
		return musica;
	}

	public void setMusica(Musica musica) {
		this.musica = musica;
	}

	public Integer getArtistaSelecionado() {
		return artistaSelecionado;
	}

	public void setArtistaSelecionado(Integer artistaSelecionado) {
		this.artistaSelecionado = artistaSelecionado;
	}

}
