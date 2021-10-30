package kitchenpos.fixture;

import java.time.LocalDateTime;
import java.util.Arrays;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupFixture {
    //    public static TableGroup GROUP1 = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(단일_손님0_테이블1, 단일_손님0_테이블2));
    public static TableGroup GROUP1 = new TableGroup(1L, LocalDateTime.now(),
            Arrays.asList(new OrderTable(0, true), new OrderTable(0, true)));
    //    public static TableGroup GROUP2 = new TableGroup(2L, LocalDateTime.now(), Collections.emptyList());
}
