package com.iu.ticketsystem.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import com.iu.ticketsystem.entity.User;
import com.iu.ticketsystem.util.HibernateUtil;

public class UserDao implements Dao<User> {

	@Override
	public Optional<User> get(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Optional<User> user = Optional.ofNullable(session.find(User.class, id));
		session.close();
		return user;
	}

	@Override
	public List<User> getAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<User> userList = session.createQuery("FROM User u ", User.class).getResultList();
		session.close();
		return userList;
	}

	@Override
	public void save(User t) {
		executeTransaction(entityManager -> entityManager.persist(t));

	}

	@Override
	public void update(User t) {
		executeTransaction(entityManager -> entityManager.merge(t));

	}

	@Override
	public void delete(User t) {
		executeTransaction(entityManager -> entityManager.remove(t));

	}

	public static Optional<User> getByEmailAndPassword(String email, String password) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Optional<User> oUser = session
				.createQuery("FROM User u where email = :email and password = :password", User.class)
				.setParameter("email", email).setParameter("password", password).uniqueResultOptional();
		session.close();
		return oUser;
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
