package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(value = MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    private TableService tableService;

    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(false);
        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setTableGroupId(1L);
        orderTable2.setEmpty(true);
    }

    @DisplayName("OrderTable 생성시 Id와 tableGroupId가 부여된다.")
    @Test
    void create() {
        OrderTable orderTable = new OrderTable();

        given(orderTableDao.save(orderTable)).willReturn(orderTable1);

        OrderTable actual = tableService.create(orderTable);

        assertThat(actual.getId()).isEqualTo(orderTable1.getId());
        assertThat(actual.getTableGroupId()).isEqualTo(orderTable1.getTableGroupId());
    }

    @DisplayName("테이블 전체 조회")
    @Test
    void list() {
        List<OrderTable> expected = Arrays.asList(orderTable1, orderTable2);

        given(orderTableDao.findAll()).willReturn(expected);

        List<OrderTable> actual = tableService.list();

        assertThat(actual.size()).isEqualTo(2);
        assertThat(actual.get(0).getId()).isEqualTo(1L);
        assertThat(actual.get(0).getTableGroupId()).isNull();
        assertThat(actual.get(1).getId()).isEqualTo(2L);
        assertThat(actual.get(1).getTableGroupId()).isEqualTo(1L);
    }

    @DisplayName("테이블 주문 여부 변경")
    @TestFactory
    Stream<DynamicTest> changeEmpty() {
        return Stream.of(
                dynamicTest("테이블 주문 여부를 변경한다.", () -> {
                    OrderTable request = new OrderTable();
                    request.setEmpty(true);

                    OrderTable expected = new OrderTable();
                    request.setId(orderTable1.getId());
                    request.setEmpty(true);

                    given(orderTableDao.findById(orderTable1.getId()))
                            .willReturn(Optional.of(orderTable1));
                    given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                            .willReturn(false);
                    given(orderTableDao.save(orderTable1)).willReturn(expected);

                    OrderTable actual = tableService.changeEmpty(orderTable1.getId(), request);

                    assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
                    assertThat(actual.getId()).isEqualTo(expected.getId());
                }),
                dynamicTest("요청한 Id와 일치하는 OrderTable이 존재하지 않을때, IllegalArgumentException 발생.",
                        () -> {
                            given(orderTableDao.findById(orderTable1.getId()))
                                    .willReturn(Optional.empty());

                            assertThatIllegalArgumentException()
                                    .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(),
                                            any()));
                        }),
                dynamicTest("요청한 OrderTable의 TableGroupId가 존재할때, IllegalArgumentException 발생.",
                        () -> {
                            given(orderTableDao.findById(orderTable2.getId()))
                                    .willReturn(Optional.of(orderTable2));

                            assertThatIllegalArgumentException()
                                    .isThrownBy(() -> tableService.changeEmpty(orderTable2.getId(),
                                            any()));
                        }),
                dynamicTest("요청한 OrderTable의 Order 상태가 조리 또는 식사일때, IllegalArgumentException 발생.",
                        () -> {
                            given(orderTableDao.findById(orderTable1.getId()))
                                    .willReturn(Optional.of(orderTable1));
                            given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable1.getId(),
                                    Arrays.asList(OrderStatus.COOKING.name(),
                                            OrderStatus.MEAL.name())))
                                    .willReturn(true);

                            assertThatIllegalArgumentException()
                                    .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(),
                                            any()));
                        })
        );
    }

    @DisplayName("테이블 손님 수 변경")
    @TestFactory
    Stream<DynamicTest> changeNumberOfGuests() {
        return Stream.of(
                dynamicTest("테이블 손님 수를 변경한다.", () -> {
                    OrderTable request = new OrderTable();
                    request.setNumberOfGuests(3);

                    given(orderTableDao.findById(orderTable1.getId()))
                            .willReturn(Optional.of(orderTable1));
                    given(orderTableDao.save(orderTable1)).willReturn(orderTable1);

                    assertThat(tableService.changeNumberOfGuests(orderTable1.getId(), request)
                            .getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
                }),
                dynamicTest("요청의 손님 수가 음수일때, IllegalArgumentException 발생.", () -> {
                    OrderTable request = new OrderTable();
                    request.setNumberOfGuests(-1);

                    assertThatIllegalArgumentException().isThrownBy(
                            () -> tableService.changeNumberOfGuests(orderTable1.getId(), request));
                }),
                dynamicTest("요청한 Id와 일치하는 OrderTable이 존재하지 않을때, IllegalArgumentException 발생.",
                        () -> {
                            OrderTable request = new OrderTable();
                            request.setNumberOfGuests(3);

                            given(orderTableDao.findById(orderTable1.getId()))
                                    .willReturn(Optional.empty());

                            assertThatIllegalArgumentException()
                                    .isThrownBy(() -> tableService.changeNumberOfGuests(
                                            orderTable1.getId(), request));
                        }),
                dynamicTest("요청한 Id와 일치하는 OrderTable에 주문이 존재하지 않을때, IllegalArgumentException 발생.",
                        () -> {
                            OrderTable request = new OrderTable();
                            request.setNumberOfGuests(3);

                            given(orderTableDao.findById(orderTable2.getId()))
                                    .willReturn(Optional.of(orderTable2));

                            assertThatIllegalArgumentException()
                                    .isThrownBy(() -> tableService.changeNumberOfGuests(
                                            orderTable2.getId(), request));
                        })
        );
    }
}