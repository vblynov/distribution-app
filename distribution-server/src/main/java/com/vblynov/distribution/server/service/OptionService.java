package com.vblynov.distribution.server.service;

import com.vblynov.distirbution.model.Option;
import com.vblynov.distirbution.model.OptionIdentifierType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OptionService {

    public Stream<Option> getByIdentifier(OptionIdentifierType type, String value) {
        switch (type) {
            case OPTION_RIC:
                return OPTIONS.stream().filter(option -> option.getRic().equals(value));
            case OPTION_ISIN:
                return OPTIONS.stream().filter(option -> option.getIsin().equals(value));
            default:
                return Stream.empty();
        }
    }

    public Stream<Option> getAll() {
        return OPTIONS.stream();
    }

    private static List<Option> OPTIONS = new ArrayList<>();

    static {
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric1").setName("name1").setDescription("option1").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric2").setName("name2").setDescription("option2").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric3").setName("name3").setDescription("option3").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric4").setName("name4").setDescription("option4").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric5").setName("name5").setDescription("option5").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric6").setName("name6").setDescription("option6").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric7").setName("name7").setDescription("option7").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric8").setName("name8").setDescription("option8").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric9").setName("name9").setDescription("option9").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin1").setRic("ric10").setName("name10").setDescription("option10").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric11").setName("name11").setDescription("option11").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric12").setName("name12").setDescription("option12").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric13").setName("name13").setDescription("option13").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric14").setName("name14").setDescription("option14").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric15").setName("name15").setDescription("option15").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric16").setName("name16").setDescription("option16").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric17").setName("name17").setDescription("option17").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric18").setName("name18").setDescription("option18").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin2").setRic("ric19").setName("name19").setDescription("option19").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric20").setName("name20").setDescription("option20").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric21").setName("name21").setDescription("option21").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric22").setName("name22").setDescription("option22").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric23").setName("name23").setDescription("option23").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric34").setName("name24").setDescription("option24").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric25").setName("name25").setDescription("option25").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric26").setName("name26").setDescription("option26").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric27").setName("name27").setDescription("option27").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric28").setName("name28").setDescription("option28").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric29").setName("name29").setDescription("option29").build());
        OPTIONS.add(Option.newBuilder().setIsin("isin3").setRic("ric30").setName("name30").setDescription("option30").build());
    }

}
