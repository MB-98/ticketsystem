package com.iu.ticketsystem.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import com.iu.ticketsystem.entity.Requirement;
import com.iu.ticketsystem.util.HibernateUtil;

public class RequirementDao implements Dao<Requirement> {

	@Override
	public Optional<Requirement> get(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Optional<Requirement> req = Optional.ofNullable(session.find(Requirement.class, id));
		session.close();
		return req;
	}

	@Override
	public List<Requirement> getAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<Requirement> reqList = session.createQuery("FROM Requirement r ", Requirement.class).getResultList();
		session.close();
		return reqList;
	}

	@Override
	public void save(Requirement r) {
		executeTransaction(entityManager -> entityManager.persist(r));

	}

	@Override
	public void update(Requirement r) {
		executeTransaction(entityManager -> entityManager.merge(r));

	}

	@Override
	public void delete(Requirement r) {
		executeTransaction(entityManager -> entityManager.remove(r));
	}

	private void executeTransaction(Consumer<EntityManager> action) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		EntityTransaction tx = session.getTransaction();
		try {
			tx.begin();
			action.accept(session);
			tx.commit();
		} catch (RuntimeException e) {
			tx.rollback();
			throw e;
		} finally {
			session.close();
		}
	}

}
