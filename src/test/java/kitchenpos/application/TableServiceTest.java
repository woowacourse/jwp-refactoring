package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private TableService tableService;

    @Test
    void 주문_테이블_생성할_수_있다() {
        OrderTable orderTable = OrderTableFixtures.빈테이블3번();
        tableService.create(orderTable);
        verify(orderTableDao).save(orderTable);
    }

    @Test
    void 주문_테이블_전체_조회할_수_있다() {
        tableService.list();
        verify(orderTableDao).findAll();
    }

    @Test
    void 주문_테이블_Id가_존재하지_않는경우_예외가_발생한다() {
        OrderTable orderTable = OrderTableFixtures.빈테이블3번();
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_그룹_Id가_존재하는_경우_예외가_발생한다() {
        OrderTable orderTable = OrderTableFixtures.빈테이블3번();
        orderTable.attachTableGroup(1L);
        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_아이디_주문상태가_존재하는_경우_예외가_발생한다() {
        OrderTable orderTable = OrderTableFixtures.빈테이블3번();

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(orderTable.getId(), orderTable)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_상태를_빈_테이블_변경할_수_있다() {
        OrderTable orderTable = OrderTableFixtures.빈테이블3번();

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);

        tableService.changeEmpty(orderTable.getId(), orderTable);

        verify(orderTableDao).save(orderTable);
    }

    @Test
    void 게스트_수는_0명_이하일_수_없다() {
        OrderTable orderTable = OrderTableFixtures.빈테이블3번();
        orderTable.changeNumberOfGuests(0);
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_Id가_존재하지_않는_경우_예외가_발생한다() {
        OrderTable orderTable = OrderTableFixtures.주문테이블1번();

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문_테이블_방문한_손님_수_변경을_할_수_있다() {
        OrderTable orderTable = OrderTableFixtures.주문테이블1번();

        when(orderTableDao.findById(orderTable.getId())).thenReturn(Optional.of(orderTable));

        tableService.changeNumberOfGuests(orderTable.getId(), orderTable);

        verify(orderTableDao).save(orderTable);
    }
}
