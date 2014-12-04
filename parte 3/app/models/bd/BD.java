package models.bd;

/**
 * Tatiana Saturno
 */

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
	
	public <T> T findByEntityId(Class<T> clazz, Long id) {
		return JPA.em().find(clazz, id);
	}

	public <T> List<T> findAllByClass(Class clazz) {
		String hql = "FROM " + clazz.getName();
		Query hqlQuery = JPA.em().createQuery(hql);
		return hqlQuery.getResultList();
	}
	
	public <T> void removeById(Class<T> classe, Long id) {
		JPA.em().remove(findByEntityId(classe, id));
	}

	public void remove(Object objeto) {
		JPA.em().remove(objeto);
	}
	
	public <T> List<T> findByAttributeName(String className,
			String attributeName, String attributeValue) {
		String hql = "FROM " + className + " c" + " WHERE c." + attributeName
				+ " = '" + attributeValue + "'";
		Query hqlQuery = JPA.em().createQuery(hql);
		return hqlQuery.getResultList();
	}

	private Query createQuery(String query) {
		return JPA.em().createQuery(query);
	}
}
