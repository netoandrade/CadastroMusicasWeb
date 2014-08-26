package dados.geral;

import java.util.List;

import basicas.Artista;
import basicas.Musica;

public interface IMusicaDAO extends IDAOGenerico<Musica>{

	public List<Musica> pesquisarMusicasPorArtista(Artista artista);

}
