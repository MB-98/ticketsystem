package com.iu.ticketsystem.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import com.iu.ticketsystem.entity.TestRun;
import com.iu.ticketsystem.util.HibernateUtil;

public class TestRunDao implements Dao<TestRun> {

	@Override
	public Optional<TestRun> get(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Optional<TestRun> testRun = Optional.ofNullable(session.find(TestRun.class, id));
		session.close();
		return testRun;
	}

	@Override
	public List<TestRun> getAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<TestRun> testRunList = session.createQuery("FROM TestRun u ", TestRun.class).getResultList();
		session.close();
		return testRunList;
	}

	public List<TestRun> getAllByUserId(Integer userId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<TestRun> testRunList = session.createQuery("FROM TestRun u where user_id = :userId", TestRun.class)
				.setParameter("userId", userId).getResultList();
		session.close();
		return testRunList;
	}

	@Override
	public void save(TestRun t) {
		executeTransaction(entityManager -> entityManager.persist(t));

	}

	@Override
	public void update(TestRun t) {
		executeTransaction(entityManager -> entityManager.merge(t));

	}

	@Override
	public void delete(TestRun t) {
		executeTransaction(entityManager -> entityManager.remove(t));

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
