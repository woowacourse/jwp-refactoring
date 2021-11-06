//package kitchenpos.service;
//
//import kitchenpos.application.TableGroupService;
//import kitchenpos.dao.OrderDao;
//import kitchenpos.dao.OrderTableDao;
//import kitchenpos.dao.TableGroupDao;
//import kitchenpos.domain.Order;
//import kitchenpos.domain.OrderStatus;
//import kitchenpos.domain.OrderTable;
//import kitchenpos.domain.TableGroup;
//import kitchenpos.fixture.OrderFixture;
//import kitchenpos.fixture.OrderTableFixture;
//import kitchenpos.fixture.TableGroupFixture;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.time.LocalDateTime;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//@SpringBootTest
//@Transactional
//@DisplayName("TableGroupService 테스트")
//class TableGroupServiceTest {
//
//    @Autowired
//    private TableGroupService tableGroupService;
//
//    @Autowired
//    private OrderDao orderDao;
//
//    @Autowired
//    private OrderTableDao orderTableDao;
//
//    private OrderTable orderTable1;
//    private OrderTable orderTable2;
//    private TableGroup tableGroup;
//
//    @BeforeEach
//    void setUp() {
//        OrderTable orderTable1 = OrderTableFixture.create(null, null, 1, true);
//        OrderTable orderTable2 = OrderTableFixture.create(null, null, 1, true);
//        this.orderTable1 = orderTableDao.save(orderTable1);
//        this.orderTable2 = orderTableDao.save(orderTable2);
//
//        tableGroup = TableGroupFixture.create(this.orderTable1, this.orderTable2);
//    }
//
//    @DisplayName("테이블 그룹 추가 - 성공")
//    @Test
//    void create() {
//        //given
//        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
//        //then
//        assertThat(savedTableGroup.getId()).isNotNull();
//        assertThat(savedTableGroup.getOrderTables().get(0).isEmpty()).isFalse();
//        assertThat(savedTableGroup.getOrderTables().get(1).isEmpty()).isFalse();
//    }
//
//    @DisplayName("테이블 그룹 추가 - 실패 - Ordertable이 비어있지 않은 경우")
//    @Test
//    void createFailure() {
//        //given
//        OrderTable notEmptyTable = OrderTableFixture.create();
//        orderTableDao.save(notEmptyTable);
//        //when
//        TableGroup tableGroup = TableGroupFixture.create(orderTable1, notEmptyTable);
//        //then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 그룹 추가 - 실패 - orderTables의 사이즈가 한 개이하인 경우")
//    @Test
//    void createFailureWhenInvalidOrderTablesSize() {
//        //given
//        //when
//        TableGroup tableGroup = TableGroupFixture.create(orderTable1);
//        //then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 그룹 추가 - 실패 - 저장되지 않은 테이블 그룹 추가")
//    @Test
//    void createFailureWhenNotExistTable() {
//        //given
//        OrderTable orderTable1 = OrderTableFixture.create(null, 99L, 1, true);
//        OrderTable orderTable2 = OrderTableFixture.create(null, 99L, 1, true);
//        TableGroup tableGroup = TableGroupFixture.create(orderTable1, orderTable2);
//        //when
//        //then
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("테이블 그룹 추가 - 실패 - 다른 그룹의 테이블을 포함한 테이블 그룹 추가")
//    @Test
//    void create_Fail_With_OthersTable() {
//        OrderTable othersTable = OrderTableFixture.create();
//        OrderTable table = OrderTableFixture.create();
//        orderTableDao.save(othersTable);
//        orderTableDao.save(table);
//
//        TableGroup tableGroup = TableGroupFixture.create(othersTable, table);
//
//        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//
//    @DisplayName("TableGroup 해제 테스트 - 성공")
//    @Test
//    void ungroup() {
//        //given
//        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
//        //when
//        tableGroupService.ungroup(savedTableGroup.getId());
//        OrderTable ungroupedTable1 = orderTableDao.findById(orderTable1.getId()).get();
//        OrderTable ungroupedTable2 = orderTableDao.findById(orderTable2.getId()).get();
//        //then
//        assertThat(ungroupedTable1.getTableGroupId()).isNull();
//        assertThat(ungroupedTable2.getTableGroupId()).isNull();
//        assertThat(ungroupedTable1.isEmpty()).isFalse();
//        assertThat(ungroupedTable2.isEmpty()).isFalse();
//    }
//
//    @DisplayName("TableGroup 해제 테스트 - 실패 - 조리, 식사 중인 테이블을 포함한 테이블 그룹 해제")
//    @Test
//    void ungroup_Fail_With_TableInProgress() {
//        Order order = OrderFixture.create(null, orderTable1.getId(), OrderStatus.COOKING.name(), LocalDateTime.now(), OrderFixture.orderLineItems());
//        orderDao.save(order);
//
//        TableGroup savedTableGroup = tableGroupService.create(tableGroup);
//
//        assertThatThrownBy(() -> tableGroupService.ungroup(savedTableGroup.getId()))
//                .isInstanceOf(IllegalArgumentException.class);
//    }
//}
