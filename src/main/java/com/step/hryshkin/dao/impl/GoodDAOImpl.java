package com.step.hryshkin.dao.impl;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.model.OrderGood;
import com.step.hryshkin.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoodDAOImpl implements GoodDAO {
    private static final Logger LOGGER = LogManager.getLogger(GoodDAOImpl.class);
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();

    @Override
    public Optional<Good> getById(long id) {
        Optional<Good> good = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            good = Optional.of(session.createQuery("FROM Good WHERE id =:id", Good.class)
                    .setParameter("id", id)
                    .uniqueResult());
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at GoodDAOImpl at getById" + exception);
        }
        return good;
    }

    @Override
    public List<Good> getAll() {
        Optional<List<Good>> goodList = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            goodList = Optional.of(session.createQuery("FROM Good", Good.class).getResultList());
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at GoodDAOImpl at getAll" + exception);
        }
        return goodList.orElse(null);
    }

    @Override
    public List<String> getGoodListByOrderId(long id) {
        Optional<List<OrderGood>> goodsInBasket = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            goodsInBasket = Optional.of(session.createQuery("FROM OrderGood WHERE orderId =:orderId", OrderGood.class)
                    .setParameter("orderId", id).getResultList());
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at GoodDAOImpl at getGoodListByOrderId" + exception);
        }
        List<String> result = new ArrayList<>();
        if (goodsInBasket.isPresent()) {
            for (OrderGood orderGood: goodsInBasket.get()) {
                Optional<Good> good = Optional.empty();
                Long goodId = orderGood.getGoodId();
                try (Session session = sessionFactory.openSession()) {
                    good = Optional.of(session.createQuery("FROM Good WHERE id =:id", Good.class)
                            .setParameter("id", goodId)
                            .uniqueResult());
                } catch (HibernateException exception) {
                    LOGGER.error("HibernateException at GoodDAOImpl at getGoodListByOrderId (inner)" + exception);
                }
                good.ifPresent(value -> result.add(value.getTitle() + " " + value.getPrice()));
            }
        }
        return result;
    }
}
