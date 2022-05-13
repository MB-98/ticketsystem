package com.iu.ticketsystem.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import com.iu.ticketsystem.entity.TestCase;
import com.iu.ticketsystem.util.HibernateUtil;

public class TestCaseDao implements Dao<TestCase> {

	@Override
	public Optional<TestCase> get(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Optional<TestCase> testCase = Optional.ofNullable(session.find(TestCase.class, id));
		session.close();
		return testCase;
	}

	@Override
	public List<TestCase> getAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<TestCase> testCaseList = session.createQuery("FROM TestCase u ", TestCase.class).getResultList();
		session.close();
		return testCaseList;
	}

	@Override
	public void save(TestCase t) {
		executeTransaction(entityManager -> entityManager.persist(t));

	}

	@Override
	public void update(TestCase t) {
		executeTransaction(entityManager -> entityManager.merge(t));

	}

	@Override
	public void delete(TestCase t) {
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
