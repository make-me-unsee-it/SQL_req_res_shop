package com.step.hryshkin.dao.impl;

import com.step.hryshkin.dao.OrderGoodDAO;
import com.step.hryshkin.model.OrderGood;
import com.step.hryshkin.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class OrderGoodDAOImpl implements OrderGoodDAO {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static final Logger LOGGER = LogManager.getLogger(OrderGoodDAOImpl.class);

    @Override
    public void createNewOrderGoodDAO(OrderGood orderGood) {
        System.out.println("Запущен createNewOrderGoodDAO()");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(orderGood);
            transaction.commit();
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderGoodDAOImpl at createNewOrderGoodDAO" + exception);
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("createNewOrderGoodDAO() выполнен. Проверить в h2");
    }
}
