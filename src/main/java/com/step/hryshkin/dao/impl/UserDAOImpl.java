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
                System.out.println("ПРИШЛИ ПРОВЕРЯТЬ getUserByName() с именем " + name);
        Optional<User> user = Optional.empty();
        //User user = null;
        try (Session session = sessionFactory.openSession()) {
                    System.out.println("внутри блока try");
            user = Optional.ofNullable(session.createQuery("FROM User WHERE userName =:userName", User.class)
                    .setParameter("userName", name).uniqueResult());
            System.out.println("ИДЕМ К САУТАМ");
            if (user.isPresent()) {
                System.out.println("ПОЛУЧЕН ЮЗЕР. Имя " + user.get().getUserName());
                System.out.println("ПОЛУЧЕН ЮЗЕР. Пароль " + user.get().getPassword());
                System.out.println("ПОЛУЧЕН ЮЗЕР. Айди " + user.get().getId());
            }
            System.out.println("в конце блока try");
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at UserDAOImpl at getUserByName" + exception);
        }
        //return Optional.empty();
        System.out.println("возвращаем опшнл.юзер");
        return user;
    }


    //тут все правильно!
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
