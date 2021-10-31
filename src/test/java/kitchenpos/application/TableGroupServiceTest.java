package kitchenpos.application;

import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.*;
import kitchenpos.domain.Order;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import support.IntegrationTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("단체 지정 서비스 테스트")
@IntegrationTest
class TableGroupServiceTest {

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @Autowired
    private OrderService orderService;

    private OrderTable firstOrderTable;
    private OrderTable secondOrderTable;


    @BeforeEach
    void setUp() {
        firstOrderTable = registerOrderTable();
        secondOrderTable = registerOrderTable();
    }

    @AfterEach
    void tearDown() {
        firstOrderTable.setTableGroupId(null);
        secondOrderTable.setTableGroupId(null);
    }

    @Nested
    @DisplayName("[단체 지정 - 조회]")
    class CreateTableGroup {

        @DisplayName("성공")
        @Test
        void create() {
            //given //when
            TableGroup actual = registerTableGroup(Arrays.asList(firstOrderTable, secondOrderTable));

            //then
            assertThat(actual).isNotNull();
            assertThat(actual.getOrderTables()).hasSize(2);
        }

        @DisplayName("실패 - 주문 테이블의 갯수가 2개 미만인 경우")
        @Test
        void createWhenOrderTablesSizeSmallerThanTwo() {
            //given
            TableGroup tableGroup = new TableGroup();
            tableGroup.setCreatedDate(LocalDateTime.now());
            tableGroup.setOrderTables(Arrays.asList(firstOrderTable));

            //when //then
            assertThatThrownBy(() -> registerTableGroup(Collections.singletonList(firstOrderTable)))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 존재하지 않는 주문 테이블이 있는 경우")
        @Test
        void createWhenOrderTablesContainNotExistOrderTable() {
            //given
            OrderTable notExistOrderTable = new OrderTable();
            notExistOrderTable.setId(100L);
            notExistOrderTable.setEmpty(true);

            //when //then
            assertThatThrownBy(() -> registerTableGroup(Arrays.asList(firstOrderTable, notExistOrderTable)))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 주문 테이블이 비어있지 않은 경우")
        @Test
        void createWhenOrderTablesContainNotEmptyOrderTable() {
            //given
            OrderTable notEmptyOrderTable = registerOrderTable(false);

            //when //then
            assertThatThrownBy(() -> registerTableGroup(Arrays.asList(firstOrderTable, notEmptyOrderTable)))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("실패 - 이미 단체 지정이 되어있으면 예외가 발생한다")
        @Test
        void createWhenAlreadyRegistered() {
            //given
            OrderTable registeredOrderTable = registerOrderTable(true);
            registerTableGroup(Arrays.asList(registerOrderTable(), registeredOrderTable));

            //when //then
            assertThatThrownBy(() -> registerTableGroup(Arrays.asList(firstOrderTable, registeredOrderTable)))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("[단체 지정 취소]")
    class UngroupTableGroup {

        @DisplayName("성공")
        @Test
        void ungroup() {
            //given
            List<OrderTable> orderTables = Arrays.asList(registerOrderTable(), registerOrderTable());
            TableGroup tableGroup = registerTableGroup(orderTables);

            //when
            tableGroupService.ungroup(tableGroup.getId());

            //then
            TableGroup actual = tableGroupDao.findById(tableGroup.getId()).get();
            assertThat(actual.getOrderTables()).usingElementComparatorOnFields("tableGroupId").isNull();
        }

        @DisplayName("실패 - '계산 완료' 상태가 아닌 주문 테이블이 있는 경우")
        @Test
        void ungroupWhenOrderStatusNotCOMPLETION() {
            //given
            OrderTable cookingOrderTable = registerOrderTable();

            List<OrderTable> orderTables = Arrays.asList(cookingOrderTable, registerOrderTable());
            TableGroup tableGroup = registerTableGroup(orderTables);

            registerOrder(cookingOrderTable);

            //when //then
            assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                    .isExactlyInstanceOf(IllegalArgumentException.class);
        }
    }

    private TableGroup registerTableGroup(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        tableGroup.setOrderTables(orderTables);

        return tableGroupService.create(tableGroup);
    }

    private OrderTable registerOrderTable(Long id, boolean empty) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(id);
        orderTable.setEmpty(empty);

        return tableService.create(orderTable);
    }

    private OrderTable registerOrderTable(boolean empty) {
        return registerOrderTable(null, empty);
    }

    private OrderTable registerOrderTable() {
        return registerOrderTable(true);
    }

    private void registerOrder(OrderTable cookingOrderTable) {
        Order order = new Order();
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(1);

        order.setOrderTableId(cookingOrderTable.getId());
        order.setOrderLineItems(Collections.singletonList(orderLineItem));

        orderService.create(order);
    }
}
