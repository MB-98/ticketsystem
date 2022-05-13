package com.iu.ticketsystem.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import com.iu.ticketsystem.entity.Status;
import com.iu.ticketsystem.util.HibernateUtil;

public class StatusDao implements Dao<Status> {

	@Override
	public Optional<Status> get(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Optional<Status> status = Optional.ofNullable(session.find(Status.class, id));
		session.close();
		return status;
	}

	@Override
	public List<Status> getAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<Status> statusList = session.createQuery("FROM Status s ", Status.class).getResultList();
		session.close();
		return statusList;
	}

	@Override
	public void save(Status t) {
		executeTransaction(entityManager -> entityManager.persist(t));

	}

	@Override
	public void update(Status t) {
		executeTransaction(entityManager -> entityManager.merge(t));

	}

	@Override
	public void delete(Status t) {
		executeTransaction(entityManager -> entityManager.remove(t));

	}

	public List<Status> getByType(String type) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<Status> statusList = session.createQuery("FROM Status s where type = :type", Status.class)
				.setParameter("type", type).getResultList();
		session.close();
		return statusList;
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
