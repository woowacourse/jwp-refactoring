package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    private OrderTable orderTable;

    @BeforeEach
    void setUp() {
        orderTable = new OrderTable(1L, null, 4, true);
    }

    @Test
    @DisplayName("테이블을 만드는 테스트")
    void create() {
        // Given
        given(orderTableDao.save(any(OrderTable.class))).willReturn(orderTable);

        // When
        OrderTable result = tableService.create(orderTable);

        // Then
        then(orderTableDao).should().save(any(OrderTable.class));
        assertThat(result).isEqualTo(orderTable);
    }

    @Test
    @DisplayName("테이블을 조회하는 테스트")
    void list() {
        // Given
        given(orderTableDao.findAll()).willReturn(List.of(orderTable));

        // When
        List<OrderTable> result = tableService.list();

        // Then
        then(orderTableDao).should().findAll();
        assertThat(result).containsExactly(orderTable);
    }

    @Test
    @DisplayName("테이블을 비워주는 테스트")
    void changeEmpty() {
        // Given
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // When
        OrderTable result = tableService.changeEmpty(orderTable.getId(), orderTable);

        // Then
        then(orderTableDao).should().findById(orderTable.getId());
        then(orderDao).should().existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                List.of(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블의 인원수를 바꿔주는 테스트")
    void changeNumberOfGuests() {
        // Given
        given(orderTableDao.findById(orderTable.getId())).willReturn(Optional.of(orderTable));

        // When
        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        // Then
        then(orderTableDao).should().findById(orderTable.getId());
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }
}
