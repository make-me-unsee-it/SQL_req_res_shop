package com.step.hryshkin.service.impl;

import com.step.hryshkin.dao.GoodDAO;
import com.step.hryshkin.dao.impl.GoodDAOImpl;
import com.step.hryshkin.model.Good;
import com.step.hryshkin.service.GoodService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodServiceImpl implements GoodService {
    private final GoodDAO goodDAO = new GoodDAOImpl();

    @Override
    public List<String> printGoods() {
        List<Good> list = goodDAO.getAll();
        List<String> result = new ArrayList<>();
        for (Good good : list) {
            result.add(good.getTitle() +
                    " (" +
                    good.getPrice().toString().replace(".", ",") +
                    "$)");
        }
        System.out.println(result);
        return result;
    }

    @Override
    public String printLowestPriceGood() {
        List<Good> list = goodDAO.getAll();
        String result = list.get(0).getTitle() + " (" +
                list.get(0).getPrice().toString().replace(".", ",") + "$)";
        BigDecimal lowestPrice = list.get(0).getPrice();
        for (Good good : list) {
            if (good.getPrice().compareTo(lowestPrice) < 0) {
                result = good.getTitle() + " (" +
                        good.getPrice().toString().replace(".", ",") + "$)";
                lowestPrice = good.getPrice();
            }
        }
        return result;
    }

    @Override
    public String printMaxPriceGood() {
        List<Good> list = goodDAO.getAll();
        String result = "";
        BigDecimal maxPrice = new BigDecimal("0");
        for (Good good : list) {
            if (good.getPrice().compareTo(maxPrice) > 0) {
                result = good.getTitle() + " (" +
                        good.getPrice().toString().replace(".", ",") + "$)";
                maxPrice = good.getPrice();
            }
        }
        return result;
    }

    @Override
    public String printGoodsCountryOrigin() {
        List<Good> list = goodDAO.getAll();
        List<String> resultCountries = new ArrayList<>();
        for (Good good : list) {
            boolean countryAlreadyExist = false;
            for (String currentCountry : resultCountries) {
                if (currentCountry.equals(good.getCountry())) {
                    countryAlreadyExist = true;
                    break;
                }
            }
            if (!countryAlreadyExist) resultCountries.add(good.getCountry());
        }
        StringBuilder output = new StringBuilder();
        for (String countryCurrent : resultCountries) {
            output.append(countryCurrent).append(", ");
        }
        output.setLength(output.length() - 2);
        return output.toString();
    }
}
