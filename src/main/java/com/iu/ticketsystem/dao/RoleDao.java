package com.iu.ticketsystem.dao;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import com.iu.ticketsystem.entity.Role;
import com.iu.ticketsystem.util.HibernateUtil;

public class RoleDao implements Dao<Role> {

	@Override
	public Optional<Role> get(int id) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Optional<Role> role = Optional.ofNullable(session.find(Role.class, id));
		session.close();
		return role;
	}

	@Override
	public List<Role> getAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		List<Role> roleList = session.createQuery("FROM Role r ", Role.class).getResultList();
		session.close();
		return roleList;
	}

	@Override
	public void save(Role t) {
		executeTransaction(entityManager -> entityManager.persist(t));

	}

	@Override
	public void update(Role t) {
		executeTransaction(entityManager -> entityManager.merge(t));

	}

	@Override
	public void delete(Role t) {
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
