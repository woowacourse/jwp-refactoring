package kitchenpos.application;

import kitchenpos.TestDomainFactory;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("/truncate.sql")
class TableGroupServiceTest {

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @Autowired
    private OrderDao orderDao;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        this.orderTable1 = createSavedOrderTable(0, true);
        this.orderTable2 = createSavedOrderTable(0, true);
    }

    @DisplayName("새로운 단체 지정 생성")
    @Test
    void createTableGroupTest() {
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Arrays.asList(this.orderTable1, this.orderTable2));

        TableGroup savedTableGroup = this.tableGroupService.create(tableGroup);

        assertAll(
                () -> assertThat(savedTableGroup).isNotNull(),
                () -> assertThat(savedTableGroup.getCreatedDate()).isNotNull()
                        .isInstanceOf(LocalDateTime.class),
                () -> assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(tableGroup.getOrderTables().size())
        );
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 없으면 예외 발생")
    @Test
    void createTableGroupWithNoOrderTableThenThrowException() {
        List<OrderTable> orderTables = Collections.emptyList();
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(), orderTables);

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 1개면 예외 발생")
    @Test
    void createTableGroupWithZeroOrOneOrderTableThenThrowException() {
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Collections.singletonList(this.orderTable1));

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 존재하지 않는 테이블이면 예외 발생")
    @Test
    void createTableGroupWithNotExistOrderTableThenThrowException() {
        long notExistOrderTableId = -1L;
        OrderTable orderTable1 = TestDomainFactory.createOrderTable(0, true);
        orderTable1.setId(notExistOrderTableId);
        OrderTable orderTable2 = createSavedOrderTable(0, true);
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블이 주문을 등록할 수 있으면(빈 테이블이 아니면) 예외 발생")
    @Test
    void createTableGroupWithNotEmptyOrderTableThenThrowException() {
        boolean isEmpty = false;
        OrderTable orderTable1 = createSavedOrderTable(0, isEmpty);
        OrderTable orderTable2 = createSavedOrderTable(0, !isEmpty);
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("새로운 단체 지정을 생성할 때 단체 지정될 주문 테이블에 다른 단체 지정이 존재하면 예외 발생")
    @Test
    void createTableGroupWithOrderTableInOtherTableGroupThenThrowException() {
        TableGroup savedTableGroup = createSavedTableGroup();
        OrderTable orderTable = TestDomainFactory.createOrderTable(0, true);
        orderTable.setTableGroupId(savedTableGroup.getId());
        OrderTable orderTable1 = this.orderTableDao.save(orderTable);
        OrderTable orderTable2 = createSavedOrderTable(0, true);
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Arrays.asList(orderTable1, orderTable2));

        assertThatThrownBy(() -> this.tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("특정 단체 지정을 제거하면 소속되었던 테이블에는 존재하는 단체 지정이 없어야 하며 동시에 주문을 등록할 수 있어야(빈 테이블이 아니어야) 한다")
    @Test
    void ungroupTest() {
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Arrays.asList(this.orderTable1, this.orderTable2));
        TableGroup savedTableGroup = this.tableGroupService.create(tableGroup);
        this.tableGroupService.ungroup(savedTableGroup.getId());

        OrderTable savedOrderTable1 =
                this.orderTableDao.findById(this.orderTable1.getId()).orElseThrow(IllegalArgumentException::new);
        OrderTable savedOrderTable2 =
                this.orderTableDao.findById(this.orderTable2.getId()).orElseThrow(IllegalArgumentException::new);

        assertAll(
                () -> assertThat(savedOrderTable1.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable1.isEmpty()).isFalse(),
                () -> assertThat(savedOrderTable2.getTableGroupId()).isNull(),
                () -> assertThat(savedOrderTable2.isEmpty()).isFalse()
        );
    }

    @DisplayName("특정 단체 지정을 제거할 때 소속 테이블의 주문 상태가 조리 혹은 식사면 예외 발생")
    @Test
    void ungroupWithOrderTableOfCookingOrMealThenThrowException() {
        createSavedOrder(this.orderTable1.getId(), OrderStatus.COOKING.name());
        createSavedOrder(this.orderTable2.getId(), OrderStatus.MEAL.name());

        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Arrays.asList(this.orderTable1, this.orderTable2));
        TableGroup savedTableGroup = this.tableGroupService.create(tableGroup);

        assertThatThrownBy(() -> this.tableGroupService.ungroup(savedTableGroup.getId())).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable createSavedOrderTable(int numberOfGuests, boolean empty) {
        OrderTable orderTable = TestDomainFactory.createOrderTable(numberOfGuests, empty);
        return this.orderTableDao.save(orderTable);
    }

    private TableGroup createSavedTableGroup() {
        OrderTable orderTable1 = createSavedOrderTable(0, true);
        OrderTable orderTable2 = createSavedOrderTable(0, true);
        TableGroup tableGroup = TestDomainFactory.createTableGroup(LocalDateTime.now(),
                                                                   Arrays.asList(orderTable1, orderTable2));

        return this.tableGroupDao.save(tableGroup);
    }

    private Order createSavedOrder(long orderTableId, String orderStatus) {
        Order order = TestDomainFactory.createOrder(orderTableId);
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderStatus(orderStatus);

        return this.orderDao.save(order);
    }
}