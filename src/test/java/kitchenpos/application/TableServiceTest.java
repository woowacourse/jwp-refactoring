package kitchenpos.application;

import static java.util.Arrays.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.factory.OrderTableFactory;

@ExtendWith(value = MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTableFactory orderTableFactory;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTableFactory = new OrderTableFactory();
        orderTable1 = orderTableFactory.create(1L, false);
        orderTable2 = orderTableFactory.create(2L, 1L, true);
    }

    @DisplayName("OrderTable 생성시 Id와 tableGroupId가 부여된다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();

        given(orderTableDao.save(orderTable)).willReturn(orderTable1);

        OrderTable actual = tableService.create(orderTable);
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(orderTable1.getId()),
                () -> assertThat(actual.getTableGroupId()).isEqualTo(orderTable1.getTableGroupId())
        );
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        List<OrderTable> expected = asList(orderTable1, orderTable2);

        given(orderTableDao.findAll()).willReturn(expected);

        List<OrderTable> actual = tableService.list();
        assertAll(
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual.get(0).getId()).isEqualTo(1L),
                () -> assertThat(actual.get(0).getTableGroupId()).isNull(),
                () -> assertThat(actual.get(1).getId()).isEqualTo(2L),
                () -> assertThat(actual.get(1).getTableGroupId()).isEqualTo(1L)
        );
    }

    @DisplayName("테이블 주문 여부 변경")
    @TestFactory
    Stream<DynamicTest> changeEmpty() {
        return Stream.of(
                dynamicTest("테이블 주문 여부를 변경한다.", this::changeEmptySuccess),
                dynamicTest("테이블이 존재해야 한다.", this::orderTableNotFound),
                dynamicTest("단체 지정은 존재하지 않아야한다.", this::orderTableHasTableGroupId),
                dynamicTest("테이블에 모든 주문은 완료 상태이어야 한다.", this::invalidOrderStatus)
        );
    }

    @DisplayName("테이블 손님 수 변경")
    @TestFactory
    Stream<DynamicTest> changeNumberOfGuests() {
        return Stream.of(
                dynamicTest("테이블 손님 수를 변경한다.", this::changeNumberOfGuestsSuccess),
                dynamicTest("손님 수를 음수로 변경할 수 없다.", this::invalidNumberOfGuests),
                dynamicTest("테이블이 존재해야 한다.", this::orderTableNotFoundInNumberOfGuests),
                dynamicTest("테이블에 주문이 존재해야 한다.", this::invalidOrderTable)
        );
    }

    private void changeEmptySuccess() {
        OrderTable request = orderTableFactory.create(true);
        OrderTable expected = orderTableFactory.create(orderTable1.getId(), true);

        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderTableDao.save(orderTable1)).willReturn(expected);
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        OrderTable actual = tableService.changeEmpty(orderTable1.getId(), request);
        assertAll(
                () -> assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty()),
                () -> assertThat(actual.getId()).isEqualTo(expected.getId())
        );
    }

    private void orderTableNotFound() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), any()));
    }

    private void orderTableHasTableGroupId() {
        given(orderTableDao.findById(orderTable2.getId())).willReturn(Optional.of(orderTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable2.getId(), any()));
    }

    private void invalidOrderStatus() {
        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), any()));
    }

    private void changeNumberOfGuestsSuccess() {
        OrderTable request = orderTableFactory.create(3);

        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.of(orderTable1));
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

        assertThat(tableService.changeNumberOfGuests(orderTable1.getId(), request)
                .getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    private void invalidNumberOfGuests() {
        OrderTable request = orderTableFactory.create(-1);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), request));
    }

    private void orderTableNotFoundInNumberOfGuests() {
        OrderTable request = orderTableFactory.create(3);

        given(orderTableDao.findById(orderTable1.getId())).willReturn(Optional.empty());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable1.getId(), request));
    }

    private void invalidOrderTable() {
        OrderTable request = orderTableFactory.create(3);

        given(orderTableDao.findById(orderTable2.getId())).willReturn(Optional.of(orderTable2));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(orderTable2.getId(), request));
    }
}