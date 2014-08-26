package dados.geral;

import java.lang.reflect.ParameterizedType;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;

import util.Parametros;

/**
 * PSC
 * 
 * @param <Entidade, PK>
 */
public abstract class DAOGenerico<Entidade> implements IDAOGenerico<Entidade>{

	protected EntityManager entityManager;
	protected Class<Entidade> classePersistente;

	@SuppressWarnings("unchecked")
	public DAOGenerico(){
		EntityManagerFactory emf = Persistence.createEntityManagerFactory(Parametros.UNIT_PERSISTENCE_NAME);
		entityManager = emf.createEntityManager();
		
		ParameterizedType parameterizedType = (ParameterizedType) getClass().getGenericSuperclass();  
	    classePersistente = (Class<Entidade>) parameterizedType.getActualTypeArguments()[0];  
	}
	
	/**
	 * Executa o merge do objeto que se encontra em mem�ria.
	 * 
	 * @param objeto
	 *            a ser realizado o merge
	 * @return objeto que foi executado o merge
	 */
	public final void alterar(Entidade objeto) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
			 
			objeto = getEntityManager().merge(objeto);
			
			tx.commit();
			
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null && tx.isActive()){
				tx.rollback();
			}
		}
	}

	/**
	 * Salva o objeto atual na base de dados.
	 * 
	 * @param objeto a ser salvo
	 */
	public final void inserir(Entidade objeto) {
		EntityTransaction tx = getEntityManager().getTransaction();		
		try {
			tx.begin();
			getEntityManager().persist(objeto);
			tx.commit();
			System.out.println(classePersistente.getSimpleName() + " salvo com sucesso");
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null && tx.isActive()){
				tx.rollback();
			}
		}
	}

	/**
	 * Salva o objeto atual na base de dados.
	 * 
	 * @param objeto
	 *            a ser salvo
	 */
	public final void inserirColecao(Collection<Entidade> colecao) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();

			for (Entidade entidade : colecao) {
				getEntityManager().persist(entidade);	
			}
			
			tx.commit();
			
			System.out.println(classePersistente.getSimpleName() + " salvos com sucesso: " + colecao.size());
		} catch (Exception e) {
			e.printStackTrace();
			if (tx != null && tx.isActive()){
				tx.rollback();
			}
		}
	}

	/**
	 * Remove o objeto da base de dados.
	 * 
	 * @param objeto
	 *            a ser removido
	 */
	public final void remover(Entidade objeto) {
		EntityTransaction tx = getEntityManager().getTransaction();
		try {
			tx.begin();
	
			refresh(objeto);
			getEntityManager().remove(objeto);
			
			tx.commit();
			
			System.out.println(classePersistente.getSimpleName() + " removido com sucesso");
		} catch (Exception e){
			e.printStackTrace();
			if (tx != null && tx.isActive()){
				tx.rollback();
			}
		}
	}

	
	
	/**
	 * Busca o objeto uma vez passado sua chave como par�metro.
	 * 
	 * @param chave
	 *            identificador
	 * @return Objeto do tipo T
	 */
	public final Entidade consultarPorId(Integer chave) {
		Entidade instance = null;
		try {
			instance = (Entidade) getEntityManager().find(classePersistente, chave);
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return instance;
	}

	public List<Entidade> consultarTodos() {
		try {
			String sql = "from " + classePersistente.getSimpleName();
			TypedQuery<Entidade> query = entityManager.createQuery(sql, classePersistente);
			return query.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Busca o objeto de acordo com o objeto preenchido com os valores passado
	 * como exemplo.
	 * 
	 * @param objeto
	 *            utilizado para realizar a busca
	 * @param ordenacoes
	 *            lista de crit�rios de ordena��o
	 * @return Lista de objetos retornada
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final List<Entidade> pesquisar(Entidade objeto) {
		Example example = criaExemplo(objeto);
		Criteria criteria = criaCriteria().add(example);
		criteria.add(example);
		return (List<Entidade>) criteria.list();
	}

	@Override
	@SuppressWarnings("unchecked")
	public final List<Entidade> pesquisar(Entidade objeto, Order... ordenacoes) {
		Example example = criaExemplo(objeto);
		Criteria criteria = criaCriteria().add(example);
		for(Order ord : ordenacoes){
			criteria.addOrder(ord);
		}
		return (List<Entidade>) criteria.list();
	}

	/**
	 * Busca o objeto de acordo com o objeto preenchido com os valores passado
	 * como exemplo.
	 * 
	 * @param objeto
	 * @param indiceInicial
	 * @param indiceFinal
	 * @param ordenacoes
	 *            lista de crit�rios de ordena��o.
	 * @return Lista de orden
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final List<Entidade> pesquisar(Entidade objeto, Integer indiceInicial,
			Integer quantidade) {
		Example example = criaExemplo(objeto);
		Criteria criteria = criaCriteria().add(example);
		criteria.setFirstResult(indiceInicial);
		criteria.setMaxResults(quantidade);

		return (List<Entidade>) criteria.list();
	}

	@Override 
	@SuppressWarnings("unchecked")
	public final List<Entidade> pesquisar(Entidade objeto, Integer indiceInicial,
			Integer quantidade, Order... ordenacoes) {
		Example example = criaExemplo(objeto);
		Criteria criteria = criaCriteria().add(example);
		criteria.setFirstResult(indiceInicial);
		criteria.setMaxResults(quantidade);
		for(Order ord : ordenacoes){
			criteria.addOrder(ord);
		}
		return (List<Entidade>) criteria.list();
	}
	
	/**
	 * M�todo utilizado para criar o objeto Example. Este objeto � utilizado
	 * para realizar a busca por exemplo.
	 * 
	 * @param objeto
	 *            sobre o qual o Example ser� criado
	 * @return em objeto do tipo Example
	 */
	protected final Example criaExemplo(Entidade objeto) {

		Example example = Example.create(objeto);
		example.enableLike(MatchMode.ANYWHERE);
		example.excludeZeroes();
		example.ignoreCase();

		return example;
	}

	/**
	 * Retorna o objeto da clases Criteria.
	 * 
	 * @return um objeto do tipo Criteria do Hibernate
	 */
	protected final Criteria criaCriteria() {
		Session session = (Session) getEntityManager().getDelegate();
		return session.createCriteria(classePersistente);
	}

	/**
	 * Retorna a quantidade total de objetos para aquela entidade espec�fica.
	 * 
	 * @return quantidade total de objetos
	 */
	public final int buscaQuantidadeTotal() {
		Criteria criteria = criaCriteria();
		criteria.setProjection(Projections.rowCount());
		return ((Long) criteria.uniqueResult()).intValue();
	}
	
	
	/**
	 * Atualiza o objeto que se encontra em mem�ria.
	 * 
	 * @param object
	 *            objeto a ser atualizado
	 */
	public final void refresh(Entidade object) {
		getEntityManager().refresh(object);
	}
	
	/**
	 * Utilizado para se injetar o Entity manager no DAO.
	 * 
	 * @param entityManager
	 *            entity manager
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}

		
}
