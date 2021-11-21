//package kitchenpos.fixture;
//
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.List;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//
//public class TableGroupFixture {
//
//    public TableGroup 테이블_그룹_생성(LocalDateTime localDateTime, List<OrderTable> orderTables) {
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setCreatedDate(localDateTime);
//        tableGroup.setOrderTables(orderTables);
//        return tableGroup;
//    }
//
//    public TableGroup 테이블_그룹_생성(
//            Long id,
//            LocalDateTime localDateTime,
//            List<OrderTable> orderTables
//    ) {
//        TableGroup tableGroup = new TableGroup();
//        tableGroup.setId(id);
//        tableGroup.setCreatedDate(localDateTime);
//        tableGroup.setOrderTables(orderTables);
//        return tableGroup;
//    }
//
//    public List<TableGroup> 테이블_그룹_리스트_생성(TableGroup... tableGroups) {
//        return Arrays.asList(tableGroups);
//    }
//}
