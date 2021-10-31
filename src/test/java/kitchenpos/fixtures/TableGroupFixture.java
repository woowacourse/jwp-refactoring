package kitchenpos.fixtures;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.fixtures.OderTableFixture.테이블그룹주문테이블1;
import static kitchenpos.fixtures.OderTableFixture.테이블그룹주문테이블2;

public class TableGroupFixture {

    public static TableGroup 첫번째테이블그룹() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(Arrays.asList(테이블그룹주문테이블1(tableGroup), 테이블그룹주문테이블2(tableGroup)));
        return tableGroup;
    }
}
