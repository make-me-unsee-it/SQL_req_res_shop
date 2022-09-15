package com.step.hryshkin.dao.impl;

import com.step.hryshkin.dao.OrderDAO;
import com.step.hryshkin.model.Order;

import java.math.BigDecimal;
import java.util.Optional;

import com.step.hryshkin.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class OrderDAOImpl implements OrderDAO {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private static final Logger LOGGER = LogManager.getLogger(OrderDAOImpl.class);

    @Override
    public void createNewOrder(Order order) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(order);
            transaction.commit();
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderDAOImpl at createNewOrder" + exception);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Optional<Order> getLastOrder() {
        Optional<Order> order = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            order = Optional.of(session.createQuery("FROM Order WHERE id IN (SELECT MAX(id) FROM Order)", Order.class)
                    .uniqueResult());
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderDAOImpl at getLastOrder" + exception);
        }
        return order;
    }

    @Override
    public void updateOrder(Order order) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(order);
            transaction.commit();
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderDAOImpl at updateOrder" + exception);
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public BigDecimal getTotalPriceByOrderId(long userId) {
        BigDecimal totalPrice = new BigDecimal("0");
        Optional<Order> order;
        try (Session session = sessionFactory.openSession()) {
            order = Optional.of(session.createQuery("FROM Order WHERE id IN (SELECT MAX(id) FROM Order)", Order.class)
                    .uniqueResult());
            totalPrice = order.get().getTotalPrice();
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderDAOImpl at getTotalPriceByOrderId" + exception);
        }
        return totalPrice;
    }
}
