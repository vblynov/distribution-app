package com.vblynov.distribution.server.service;

import com.vblynov.distirbution.model.Stock;
import com.vblynov.distirbution.model.StockIdentifierType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StockService {

    public List<Stock> getByIdentifier(StockIdentifierType type, String value) {
        switch (type) {
            case STOCK_RIC:
                return STOCKS.stream().filter(Stock -> Stock.getRic().equals(value)).collect(Collectors.toList());
            case STOCK_ISIN:
                return STOCKS.stream().filter(Stock -> Stock.getIsin().equals(value)).collect(Collectors.toList());
            default:
                return Collections.emptyList();
        }
    }

    public Stream<Stock> getAll() {
        return STOCKS.stream();
    }

    private static List<Stock> STOCKS = new ArrayList<>();

    static {
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric1").setName("name1").setDescription("Stock1").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric2").setName("name2").setDescription("Stock2").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric3").setName("name3").setDescription("Stock3").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric4").setName("name4").setDescription("Stock4").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric5").setName("name5").setDescription("Stock5").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric6").setName("name6").setDescription("Stock6").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric7").setName("name7").setDescription("Stock7").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric8").setName("name8").setDescription("Stock8").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric9").setName("name9").setDescription("Stock9").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin1").setRic("ric10").setName("name10").setDescription("Stock10").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric11").setName("name11").setDescription("Stock11").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric12").setName("name12").setDescription("Stock12").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric13").setName("name13").setDescription("Stock13").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric14").setName("name14").setDescription("Stock14").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric15").setName("name15").setDescription("Stock15").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric16").setName("name16").setDescription("Stock16").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric17").setName("name17").setDescription("Stock17").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric18").setName("name18").setDescription("Stock18").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin2").setRic("ric19").setName("name19").setDescription("Stock19").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric20").setName("name20").setDescription("Stock20").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric21").setName("name21").setDescription("Stock21").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric22").setName("name22").setDescription("Stock22").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric23").setName("name23").setDescription("Stock23").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric34").setName("name24").setDescription("Stock24").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric25").setName("name25").setDescription("Stock25").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric26").setName("name26").setDescription("Stock26").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric27").setName("name27").setDescription("Stock27").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric28").setName("name28").setDescription("Stock28").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric29").setName("name29").setDescription("Stock29").build());
        STOCKS.add(Stock.newBuilder().setIsin("isin3").setRic("ric30").setName("name30").setDescription("Stock30").build());
    }

}
