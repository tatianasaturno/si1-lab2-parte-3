package models.bd;

import java.util.List;

import javax.persistence.Query;
import play.db.jpa.JPA;

public class BD {

	public boolean persist(Object e) {
		JPA.em().persist(e);
		return true;
	}

	public void flush() {
		JPA.em().flush();
	}

	public void merge(Object e) {
		JPA.em().merge(e);
	}

	public void refresh(Object e) {
		JPA.em().refresh(e);
	}

	public <T> T findByEntityId(Class<T> classe, Long id) {
		return JPA.em().find(classe, id);
	}

	public <T> List<T> findAllByClass(Class calsse) {
		String hql = "FROM " + calsse.getName();
		Query hqlQuery = JPA.em().createQuery(hql);
		return hqlQuery.getResultList();
	}

	public <T> void removeById(Class<T> classe, Long id) {
		JPA.em().remove(findByEntityId(classe, id));
	}

	public void remove(Object objeto) {
		JPA.em().remove(objeto);
	}

	public <T> List<T> findByAttributeName(String classe, String atributo,
			String valor) {
		String hql = "FROM " + classe + " c" + " WHERE c." + atributo + " = '"
				+ valor + "'";
		Query hqlQuery = JPA.em().createQuery(hql);
		return hqlQuery.getResultList();
	}

	private Query createQuery(String query) {
		return JPA.em().createQuery(query);
	}
}
