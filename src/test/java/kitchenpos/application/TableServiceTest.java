package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql({"/h2-truncate.sql"})
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderDao orderDao;

    @Test
    @DisplayName("테이블 등록에 성공한다.")
    void succeedInRegisteringTable() {
        // given
        OrderTable orderTable = new OrderTable();
        int numberOfGuest = 4;
        boolean tableStatus = true;
        orderTable.setNumberOfGuests(numberOfGuest);
        orderTable.setEmpty(tableStatus);

        // when
        OrderTable savedOrdertable = tableService.create(orderTable);

        // then
        assertSoftly(softly -> {
            softly.assertThat(savedOrdertable.getId()).isNotNull();
            softly.assertThat(savedOrdertable.getNumberOfGuests()).isEqualTo(numberOfGuest);
            softly.assertThat(savedOrdertable.isEmpty()).isTrue();
        });
    }

    @Test
    @DisplayName("전체 테이블 조회에 성공한다.")
    void succeedInSearchingTableList() {
        // given
        OrderTable orderTableA = new OrderTable();
        orderTableA.setNumberOfGuests(3);
        orderTableA.setEmpty(true);

        OrderTable orderTableB = new OrderTable();
        orderTableB.setNumberOfGuests(4);
        orderTableB.setEmpty(true);

        // when
        OrderTable savedTableA = tableService.create(orderTableA);
        OrderTable savedTableB = tableService.create(orderTableB);

        // then
        assertThat(tableService.list()).hasSize(2);
    }

    @Test
    @DisplayName("테이블의 손님 유무 상태를 변경에 성공한다.")
    void succeedInChangingTableStatus() {
        // give
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);
        OrderTable changedOrderTable = tableService.changeEmpty(savedOrderTable.getId(), newOrderTable);

        // then
        assertThat(changedOrderTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("등록된 테이블이 존재하지 않을 경우 테이블 상태 변경 시 예외가 발생한다.")
    void failToChangeTableStatusWithUnRegisteredTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 그룹으로 지정된 테이블의 상태를 변경할 경우 예외가 발생한다.")
    void failToChangeTableStatusAlreadyGrouped() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setCreatedDate(LocalDateTime.now());
        TableGroup savedTableGroup = tableGroupDao.save(tableGroup);

        OrderTable orderTableA = new OrderTable();
        orderTableA.setNumberOfGuests(4);
        orderTableA.setEmpty(false);
        orderTableA.setTableGroupId(savedTableGroup.getId());
        OrderTable savedOrderTableA = tableService.create(orderTableA);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.MEAL.name());
        order.setOrderTableId(savedOrderTableA.getId());
        order.setOrderedTime(LocalDateTime.now());
        orderDao.save(order);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTableA.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.INCLUDE, names = {"COOKING", "MEAL"})
    @DisplayName("주문 상태가 조리 중이거나 식사 중인 테이블의 상태를 변경할 경우 예외가 발생한다.")
    void failToChangeTableStatusWithOrderStatus(OrderStatus orderStatus) {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(savedOrderTable.getId());
        orderDao.save(order);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setEmpty(true);

        // then
        assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @Test
    @DisplayName("손님수 변경에 성공한다.")
    void succeedInChangingNumberOfGuests() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(2);
        OrderTable changedOrderTable = tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable);

        // then
        assertThat(changedOrderTable.getNumberOfGuests()).isEqualTo(2);
    }

    @Test
    @DisplayName("변경하려는 손님 수가 0미만일 경우 예외가 발생한다.")
    void failToChangeNumberOfGuestWithWrongNumber() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(-4);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("사전에 등록된 테이블이 없는 상태에서 테이블 손님 수를 변경할 경우 예외가 발생한다.")
    void failToChangeNumberOfGuestWithNonExistTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(false);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(2);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블이 빈 상태일 때 손님 수를 변경하면 예외가 발생한다.")
    void failToChangeNumberOfGuestWithNotEmptyTable() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(4);
        orderTable.setEmpty(true);
        OrderTable savedOrderTable = tableService.create(orderTable);

        // when
        OrderTable newOrderTable = new OrderTable();
        newOrderTable.setNumberOfGuests(2);

        // then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
