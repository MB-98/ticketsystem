package com.iu.ticketsystem.dao;

import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import com.iu.ticketsystem.entity.TestRunTestCase;
import com.iu.ticketsystem.util.HibernateUtil;

public class TestRunTestCaseDao {

	public Optional<TestRunTestCase> get(int testrunId, int testcaseId) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Optional<TestRunTestCase> oTestRunTestCase = session
				.createQuery("FROM TestRunTestCase u where testrun_id = :testrunId and testcase_id = :testcaseId", TestRunTestCase.class)
				.setParameter("testrunId", testrunId).setParameter("testcaseId", testcaseId).uniqueResultOptional();
		session.close();
		return oTestRunTestCase;
	}

	public void save(TestRunTestCase t) {
		executeTransaction(entityManager -> entityManager.persist(t));

	}

	public void update(TestRunTestCase t) {
		executeTransaction(entityManager -> entityManager.merge(t));

	}

	public void delete(TestRunTestCase t) {
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
