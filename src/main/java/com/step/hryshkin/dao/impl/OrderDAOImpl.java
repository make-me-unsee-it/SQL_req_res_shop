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


    //TODO каменты. Вроде бы работает
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

    //TODO каменты. Кажется заработал. Но стоит понаблюдать
    @Override
    public Optional<Order> getLastOrder() {
        System.out.println("Чиним неисправный getLastOrder()");
        Optional<Order> order = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            System.out.println("внутри трай");
            order = Optional.of(session.createQuery("FROM Order WHERE id IN (SELECT MAX(id) FROM Order)", Order.class)
                    .uniqueResult()); //TODO - наблюдать. Не факт, что сработает как надо дальше
            System.out.println("стоимость ордера = " + order.get().getTotalPrice());
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderDAOImpl at getLastOrder" + exception);
        }
        System.out.println("возвращаем последний ордер");
        return order;
    }

    @Override
    public void updateOrder(Order order) {
        System.out.println("выполняется updateOrder()");
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(order); //TODO не уверен, что это работает!
            transaction.commit();
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderDAOImpl at updateOrder" + exception);
            if (transaction != null) {
                transaction.rollback();
            }
        }
        System.out.println("updateOrder() выполнился. Проверить вручную в h2");
    }

    @Override
    public BigDecimal getTotalPriceByOrderId(long id) {
        BigDecimal totalPrice = new BigDecimal("0");
        try (Session session = sessionFactory.openSession()) {
            String sum = Optional.of(session.createQuery("FROM Orders WHERE Id = '" + id + "';")
                    .uniqueResult()).toString(); //TODO не уверен, что это работает!
            if (sum != null) {
                totalPrice = new BigDecimal(sum);
            }
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at OrderDAOImpl at getTotalPriceByOrderId" + exception);
        }
        return totalPrice;
    }
}
