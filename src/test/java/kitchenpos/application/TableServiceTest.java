package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
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

@SpringBootTest
@Sql("/deleteAll.sql")
class TableServiceTest {

    @Autowired
    private TableService tableService;
    private OrderTable table;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderDao orderDao;

    @BeforeEach
    void setUp() {
        table = OrderTable.builder()
            .numberOfGuests(0)
            .empty(true)
            .build();
    }

    @DisplayName("테이블 추가")
    @Test
    void create() {
        OrderTable create = tableService.create(table);

        assertThat(create.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        tableService.create(table);
        tableService.create(table);

        List<OrderTable> list = tableService.list();

        assertThat(list).hasSize(2);
    }

    @DisplayName("주문 등록 불가 여부 변경")
    @Test
    void changeEmpty() {
        OrderTable create = tableService.create(table);
        OrderTable target = OrderTable.builder()
            .empty(!table.isEmpty())
            .build();

        OrderTable changeEmpty = tableService.changeEmpty(create.getId(), target);

        assertThat(changeEmpty.isEmpty()).isEqualTo(target.isEmpty());
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_NotExistTable() {
        OrderTable target = OrderTable.builder()
            .empty(false)
            .build();

        assertThatThrownBy(() -> tableService.changeEmpty(100L, target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 그룹에 포함된 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInGroup() {
        OrderTable create = tableService.create(table);

        OrderTable table2 = OrderTable.builder()
            .empty(true)
            .build();
        OrderTable create2 = tableService.create(table2);

        TableGroup tableGroup = TableGroup.builder()
            .orderTables(Arrays.asList(create, create2))
            .createdDate(LocalDateTime.now())
            .build();
        tableGroupService.create(tableGroup);

        OrderTable target = OrderTable.builder()
            .empty(!table.isEmpty())
            .build();

        assertThatThrownBy(() -> tableService.changeEmpty(create.getId(), target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 조리, 식사 중인 테이블의 주문 등록 불가 여부 변경")
    @Test
    void changeEmpty_Fail_With_TableInProgress() {
        OrderTable create = tableService.create(table);
        Order order = Order.builder()
            .orderTableId(create.getId())
            .orderStatus(OrderStatus.COOKING.name())
            .orderedTime(LocalDateTime.now())
            .build();
        orderDao.save(order);

        OrderTable target = OrderTable.builder()
            .empty(!table.isEmpty())
            .build();

        assertThatThrownBy(() -> tableService.changeEmpty(create.getId(), target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable notEmptyTable = OrderTable.builder()
            .numberOfGuests(0)
            .empty(false)
            .build();
        OrderTable create = tableService.create(notEmptyTable);
        OrderTable target = OrderTable.builder()
            .numberOfGuests(10)
            .build();

        OrderTable changeNumberOfGuests = tableService.changeNumberOfGuests(create.getId(), target);

        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(target.getNumberOfGuests());
    }

    @DisplayName("[예외] 0보다 작은 수로 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_InvalidNumberOfGuest() {
        OrderTable notEmptyTable = OrderTable.builder()
            .numberOfGuests(0)
            .empty(false)
            .build();
        OrderTable create = tableService.create(notEmptyTable);
        OrderTable target = OrderTable.builder()
            .numberOfGuests(-1)
            .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(create.getId(), target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 존재하지 않는 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_NotExistTable() {
        OrderTable target = OrderTable.builder()
            .numberOfGuests(10)
            .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, target))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("[예외] 빈 테이블의 손님 수 변경")
    @Test
    void changeNumberOfGuests_Fail_With_EmptyTable() {
        OrderTable create = tableService.create(table);
        OrderTable target = OrderTable.builder()
            .numberOfGuests(-1)
            .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(create.getId(), target))
            .isInstanceOf(IllegalArgumentException.class);
    }
}