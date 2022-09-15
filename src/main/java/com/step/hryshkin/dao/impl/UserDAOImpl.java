package com.step.hryshkin.dao.impl;

import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.User;
import com.step.hryshkin.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.Optional;

public class UserDAOImpl implements UserDAO {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static final Logger LOGGER = LogManager.getLogger(UserDAOImpl.class);

    @Override
    public Optional<User> getUserByName(String name) {
        Optional<User> user = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            user = Optional.ofNullable(session.createQuery("FROM User WHERE userName =:userName", User.class)
                    .setParameter("userName", name).uniqueResult());
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at UserDAOImpl at getUserByName" + exception);
        }
        return user;
    }

    @Override
    public void createNewUser(User user) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(user);
            transaction.commit();
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at UserDAOImpl at createNewUser" + exception);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
