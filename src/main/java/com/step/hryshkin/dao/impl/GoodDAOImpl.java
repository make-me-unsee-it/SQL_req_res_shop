package com.step.hryshkin.dao.impl;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.UserDAO;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.model.User;
import com.step.hryshkin.utils.HibernateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GoodDAOImpl implements GoodDAO {
    private static final Logger LOGGER = LogManager.getLogger(GoodDAOImpl.class);
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    private final UserDAO userDAO = new UserDAOImpl();

    @Override
    public Optional<Good> getByTitle(String title) {
        Optional<Good> good = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            good = Optional.of(session.createQuery("FROM Good WHERE title =:title", Good.class)
                    .setParameter("title", title)
                    .uniqueResult()); //TODO по идее это должно работать
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at GoodDAOImpl at getByTitle" + exception);
        }
        return good;
    }

    @Override
    public Optional<Good> getById(long id) {
        System.out.println("Проверяем getById()");

        Optional<Good> good = Optional.empty();
        try (Session session = sessionFactory.openSession()) {
            good = Optional.of(session.createQuery("FROM Good WHERE id =:id", Good.class)
                    .setParameter("id", id)
                    .uniqueResult()); //TODO по идее это должно работать
            System.out.println("проверим - получен ли товар");
            System.out.println("Название " + good.get().getTitle());
            System.out.println("Цена " + good.get().getPrice());

        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at GoodDAOImpl at getById" + exception);
        }
        System.out.println("Метод getById() заканчивает работу");
        return good;
    }

    //TODO каменты. ОНО РАБОТЕТ, ЕПТА!
    @Override
    public List<Good> getAll() {
        Optional<List<Good>> goodList = Optional.empty();
            System.out.println("ЧЕК ВНУТРИ getAll() 1");
        try (Session session = sessionFactory.openSession()) {
            goodList = Optional.of(session.createQuery("FROM Good", Good.class).getResultList());
                System.out.println("ЧЕК ВНУТРИ getAll() 2");
                System.out.println(goodList.get().toString());
                System.out.println("ЧЕК ВНУТРИ getAll() 3");
            //TODO хрен знает как, но оно работает. Взял тут https://www.baeldung.com/hibernate-select-all
        } catch (HibernateException exception) {
            LOGGER.error("HibernateException at GoodDAOImpl at getAll" + exception);
        }
        System.out.println("ЧЕК ВНУТРИ getAll() 4");
        System.out.println(goodList);
        if (goodList.isPresent()) return goodList.get();
        else return null;
    }

    @Override
    public List<String> getGoodBasketByUserName(String userName) {
        Optional<User> newUser = userDAO.getUserByName(userName);
        List<String> goodsInBasket = new ArrayList<>();
        if (newUser.isPresent()) {
            String userNameId = String.valueOf(newUser.get().getId());
            //TODO вот тут полная жопа - это джойн. Пока что выпилен н***й
            try (Session session = sessionFactory.openSession()) {
                /*
                try (PreparedStatement ps = session.prepareStatement("g.title, o.totalPrice FROM orders AS o " +
                        "JOIN orderGoods AS b ON o.id = b.orderId " +
                        "JOIN goods AS g ON b.goodId = g.id " +
                        "WHERE o.userId = '" + userNameId + "';")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        goodsInBasket.add(rs.getNString("TITLE") + " " + rs.getBigDecimal("TOTALPRICE") + "$");
                    }
                }
                */
            } catch (HibernateException exception) {
                LOGGER.error("HibernateException at GoodDAOImpl at getGoodBasketByUserName" + exception);
            }
        }
        return goodsInBasket;
    }

    @Override
    public List<String> getGoodListByOrderId(long id) {
        List<String> goodsInBasket = new ArrayList<>();
        //TODO тут тоже не переделано
        /*
        try (Connection connection = Connector.createConnection()) {
            try (PreparedStatement ps = connection.prepareStatement("SELECT g.title, g.price FROM goods AS g " +
                    "JOIN orderGoods AS b ON g.id = b.goodId " +
                    "WHERE b.orderId = '" + id + "';")) {
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    goodsInBasket.add(rs.getNString("TITLE") + " " + rs.getBigDecimal("PRICE") + "$");
                }
            }
        } catch (SQLException throwable) {
            LOGGER.error("SQLException at GoodDAOImpl at getGoodBasketByUserName" + throwable);
        }

         */
        return goodsInBasket;
    }

    @Override
    public BigDecimal getTotalPriceByUserName(String userName) {
        Optional<User> newUser = userDAO.getUserByName(userName);
        BigDecimal totalPrice = new BigDecimal("0");
        if (newUser.isPresent()) {
            String userNameId = String.valueOf(newUser.get().getId());
            //TODO тут тоже не переделано
        /*

            try (Connection connection = Connector.createConnection()) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT SUM (totalPrice) AS RESULT FROM orders " +
                        "WHERE USERID = '" + userNameId + "';")) {
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        String result = rs.getNString("RESULT");
                        if (result == null) {
                            result = "0";
                        }
                        totalPrice = new BigDecimal(result);
                    }
                }
            } catch (SQLException throwable) {
                LOGGER.error("SQLException at GoodDAOImpl at getTotalPriceByUserName" + throwable);
            }
        */
        }
        return totalPrice;
    }
}
