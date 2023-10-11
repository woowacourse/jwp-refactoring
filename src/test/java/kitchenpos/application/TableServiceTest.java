package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@SpringBootTest
class TableServiceTest {

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderTableDao orderTableDao;

    @MockBean
    private OrderDao orderDao;

    @Test
    void 주문_테이블을_생성한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setNumberOfGuests(1);

        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);

        // when
        OrderTable result = tableService.create(orderTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests());
    }

    @Test
    void 주문_테이블을_전체_조회한다() {
        // given
        OrderTable orderTable1 = new OrderTable();
        OrderTable orderTable2 = new OrderTable();

        given(orderTableDao.findAll())
                .willReturn(List.of(orderTable1, orderTable2));

        // when
        List<OrderTable> result = tableService.list();

        // then
        assertThat(result).containsExactly(orderTable1, orderTable2);
    }

    @Test
    void 주문_테이블을_빈_공간으로_변경한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);

        OrderTable changedOrderTable = new OrderTable();
        changedOrderTable.setTableGroupId(orderTable.getTableGroupId());
        changedOrderTable.setEmpty(true);

        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);
        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);

        // when
        OrderTable result = tableService.changeEmpty(orderTable.getId(), changedOrderTable);

        // then
        assertThat(result.isEmpty()).isEqualTo(changedOrderTable.isEmpty());
    }

    @Test
    void 주문_테이블의_게스트_수를_변경한다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(1);

        OrderTable changedOrderTable = new OrderTable();
        changedOrderTable.setNumberOfGuests(5);

        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);

        // when
        OrderTable result = tableService.changeNumberOfGuests(orderTable.getId(), changedOrderTable);

        // then
        assertThat(result.getNumberOfGuests()).isEqualTo(changedOrderTable.getNumberOfGuests());
    }

    @Test
    void 주문_테이블의_게스트_수를_0명보다_적게_변경하면_예외를_던진다() {
        // given
        OrderTable orderTable = new OrderTable();
        orderTable.setId(1L);
        orderTable.setNumberOfGuests(1);

        OrderTable changedOrderTable = new OrderTable();
        changedOrderTable.setNumberOfGuests(-1);

        given(orderTableDao.findById(orderTable.getId()))
                .willReturn(Optional.of(orderTable));
        given(orderTableDao.save(orderTable))
                .willReturn(orderTable);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTable.getId(), changedOrderTable))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
