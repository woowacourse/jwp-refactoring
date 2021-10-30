package kitchenpos;

import kitchenpos.application.TableGroupService;
import kitchenpos.application.TableService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DisplayName("TableService 테스트")
class TableServiceTest {

    @Autowired
    private TableService tableService;
    @Autowired
    private TableGroupService tableGroupService;
    @Autowired
    private OrderDao orderDao;

    private OrderTable table;

    @BeforeEach
    void setUp() {
        table = OrderTable.builder()
                .numberOfGuests(0)
                .empty(true)
                .build();
    }

    @DisplayName("테이블 추가 - 성공")
    @Test
    void create() {
        OrderTable table = OrderTable.builder()
                .numberOfGuests(0)
                .empty(true)
                .build();

        OrderTable savedTable = tableService.create(table);

        assertThat(savedTable.getId()).isNotNull();
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        tableService.create(table);
        tableService.create(table);
        List<OrderTable> tables = tableService.list();

        assertThat(tables).isNotEmpty();
    }

    @DisplayName("테이블 empty 변경 테스트")
    @Test
    void changeEmpty() {
        OrderTable savedTable = tableService.create(table);
        OrderTable target = OrderTable.builder()
                .empty(!table.isEmpty())
                .build();
        OrderTable changeEmpty = tableService.changeEmpty(savedTable.getId(), target);

        assertThat(changeEmpty.isEmpty()).isEqualTo(target.isEmpty());
    }

    @DisplayName("테이블 empty 변경 테스트 - 실패 - 그룹에 포함된 테이블인 경우")
    @Test
    void changeEmptyFailureWhenTableInGroup() {
        OrderTable savedTable = tableService.create(table);

        OrderTable table2 = OrderTable.builder()
                .empty(true)
                .build();
        OrderTable savedTable2 = tableService.create(table2);

        TableGroup tableGroup = TableGroup.builder()
                .orderTables(Arrays.asList(savedTable, savedTable2))
                .createdDate(LocalDateTime.now())
                .build();
        tableGroupService.create(tableGroup);

        OrderTable target = OrderTable.builder()
                .empty(!table.isEmpty())
                .build();

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 empty 변경 테스트 - 실패 - OrderStatus가 COOKING, MEAL 테이블인 경우")
    @Test
    void changeEmptyFailureWhenInvalidStatus() {
        OrderTable savedTable = tableService.create(table);
        Order order = Order.builder()
                .orderTableId(savedTable.getId())
                .orderStatus(OrderStatus.COOKING.name())
                .orderedTime(LocalDateTime.now())
                .build();
        orderDao.save(order);

        OrderTable target = OrderTable.builder()
                .empty(!table.isEmpty())
                .build();

        assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경")
    @Test
    void changeNumberOfGuests() {
        OrderTable notEmptyTable = OrderTable.builder()
                .numberOfGuests(0)
                .empty(false)
                .build();
        OrderTable savedTable = tableService.create(notEmptyTable);
        OrderTable target = OrderTable.builder()
                .numberOfGuests(10)
                .build();
        OrderTable changeNumberOfGuests = tableService.changeNumberOfGuests(savedTable.getId(), target);
        assertThat(changeNumberOfGuests.getNumberOfGuests()).isEqualTo(target.getNumberOfGuests());
    }

    @DisplayName("GuestNumber 변경 - 실패 - 0보다 작은 수의 GuestNumber 인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenInvalidNumberOfGuest() {
        OrderTable notEmptyTable = OrderTable.builder()
                .numberOfGuests(0)
                .empty(false)
                .build();
        OrderTable savedTable = tableService.create(notEmptyTable);
        OrderTable target = OrderTable.builder()
                .numberOfGuests(-1)
                .build();
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경 - 실패 - 존재하지 않는 테이블인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenNotExistTable() {
        OrderTable target = OrderTable.builder()
                .numberOfGuests(10)
                .build();
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(100L, target))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("GuestNumber 변경 - 실패 - 빈 테이블인 경우")
    @Test
    void changeNumberOfGuestsFailureWhenEmptyTable() {
        OrderTable savedTable = tableService.create(table);
        OrderTable target = OrderTable.builder()
                .numberOfGuests(10)
                .build();

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), target))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
