package dados.geral;

import java.util.List;

import javax.persistence.TypedQuery;

import basicas.Artista;
import basicas.Musica;

public class MusicaDAO extends DAOGenerico<Musica> implements IMusicaDAO {

	public List<Musica> pesquisarMusicasPorArtista(Artista artista){
		String jpql = "select m from Musica m where m.artista = :art";
		TypedQuery<Musica> query = getEntityManager().createQuery(jpql, Musica.class);
		query.setParameter("art", artista);
		return query.getResultList();
	}
}
