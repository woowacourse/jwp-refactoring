package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    private TableService tableService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @BeforeEach
    void setUp() {
        tableService = new TableService(orderDao, orderTableDao);
    }

    @DisplayName("오더 테이블 생성")
    @Test
    void createOrderTable() {
        OrderTable orderTableRequest = new OrderTable();
        orderTableRequest.setNumberOfGuests(0);
        orderTableRequest.setEmpty(true);

        given(orderTableDao.save(orderTableRequest)).willAnswer(answer-> {
            OrderTable savedOrderTable = answer.getArgument(0, OrderTable.class);
            savedOrderTable.setId(1L);
            return savedOrderTable;
        });

        OrderTable savedOrderTable = tableService.create(orderTableRequest);

        assertAll(
            () -> assertThat(savedOrderTable.getId()).isNotNull(),
            () -> assertThat(savedOrderTable.getTableGroupId()).isNull(),
            () -> assertThat(savedOrderTable.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests()),
            () -> assertThat(savedOrderTable.isEmpty()).isEqualTo(orderTableRequest.isEmpty())
        );
    }

    @DisplayName("주문 테이블을 빈 테이블로 설정 또는 해지한다.")
    @ParameterizedTest
    @CsvSource("true,false")
    void changeEmpty(boolean empty) {
        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(empty);

        OrderTable orderTable = getOrderTableFixture();

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        tableService.changeEmpty(orderTable.getId(), emptyRequest);

        assertThat(orderTable.isEmpty()).isEqualTo(empty);

    }

    @DisplayName("존재하지 않는 테이블의 빈 테이블 설정 요청시 예외 발생")
    @Test
    void changeEmptyException() {
        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(true);

        OrderTable orderTable = getOrderTableFixture();

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("그룹화 된 테이블의 빈 테이블 설정 요청시 예외 발생")
    @Test
    void changeEmptyTableGroupException() {
        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(true);

        OrderTable orderTable = getOrderTableFixture();
        orderTable.setTableGroupId(1L);

        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블의 주문 목록 중 '조리' 또는 '식사 중'인 주문이 있을 경우 예외 발생")
    @Test
    void changeEmptyOrderStatusException() {
        OrderTable emptyRequest = new OrderTable();
        emptyRequest.setEmpty(true);

        OrderTable orderTable = getOrderTableFixture();
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), emptyRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static OrderTable getOrderTableFixture() {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(0);
        orderTable.setEmpty(false);
        return orderTable;
    }
}