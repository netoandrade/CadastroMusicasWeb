package managedbeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import negocio.NegocioException;
import fachada.Fachada;
import fachada.IFachada;
import basicas.Artista;
import basicas.Genero;

@ManagedBean
public class ArtistaBean {

	private Artista artista = new Artista();
	private Genero[] generos;
	private IFachada fachada = Fachada.getInstancia();

	public String salvar(){
		if (artista.getCodigo() == null || artista.getCodigo() == 0){
			artista.setCodigo(null);
			fachada.inserir(artista);
		} else {
			fachada.alterar(artista);
		}
		return "artista-listar.xhtml";
	}
	
	public String editar(Artista artParam){
		this.artista = artParam;
		return "artista-salvar.xhtml";
	}
	
	public String excluir(Artista artParam){
		try {
			fachada.excluir(artParam);
			return "artista-listar.xhtml";	
		} catch (NegocioException e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Não pode remover artista " + artParam.getNome(), 
				 e.getMessage()));
			//e.printStackTrace();
			System.out.println("Não pode excluir!");
			return null;
		}
	}

	public List<Artista> getColecao(){
		return Fachada.getInstancia().consultarTodosArtistas();
	}
	
	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}

	public Genero[] getGeneros() {
		return Genero.values();
	}

//	public List<String> getGeneros2() {
//		List<String> lista = new ArrayList<String>();
//		for (Genero genero : Genero.values()) {
//			lista.add(genero.name());
//			System.out.println(genero.name());
//
//		}
//		return lista;
//	}

}
