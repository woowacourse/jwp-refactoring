package kitchenpos;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import kitchenpos.domain.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static kitchenpos.TableGroupFixture.GROUP1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableValidator validator;

    @Test
    @DisplayName("테이블의 빈(empty) 여부를 수정할 수 있다.")
    void changeEmpty() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, false);

        // when
        table.changeEmpty(true, validator);

        // then
        assertTrue(table.isEmpty());
    }

    @Test
    @DisplayName("테이블이 특정 그룹에 속해있다면 빈(empty) 여부를 수정할 수 없다.")
    void changeEmptyBelongToGroup() {
        // given
        OrderTable table = new OrderTable(1L, GROUP1, 4, false);

        // when & then
        assertThatThrownBy(() -> table.changeEmpty(true, validator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 테이블이 그룹에 속해있습니다. 그룹을 해제해주세요.");
    }

    @Test
    @DisplayName("주문 상태가 조리중(COOKING)이나 식사중(MEAL)이라면, 빈(empty) 여부를 수정할 수 없다.")
    void changeEmptyCookingOrMeal() {
        // given
       Order COOKING_ORDER = new Order(
                1L,
                1L,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                new ArrayList<>()
        );
        List<Order> orders = Collections.singletonList(COOKING_ORDER);
        OrderTable table = new OrderTable(1L, null, 4, false);

        given(orderRepository.findAllByOrderTableId(anyLong())).willReturn(orders);

        // when & then
        assertThatThrownBy(() -> table.changeEmpty(true, validator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리중이나 식사중입니다.");
    }

    @Test
    @DisplayName("그룹을 해제하면 테이블의 그룹이 null이 되고, 비어있지 않게 된다.")
    void ungroup() {
        // given
        OrderTable table = new OrderTable(1L, null, 4, false);

        // when
        table.ungroup(validator);

        // then
        assertNull(table.getTableGroup());
        assertFalse(table.isEmpty());
    }

    @Test
    @DisplayName("주문 상태가 조리중이나 식사중이라면 그룹을 해제할 수 없다.")
    void ungroupWrongStatus() {
        // given
        Order COOKING_ORDER = new Order(
                1L,
                1L,
                OrderStatus.COOKING,
                LocalDateTime.now(),
                new ArrayList<>()
        );
        List<Order> orders = Collections.singletonList(COOKING_ORDER);
        OrderTable table = new OrderTable(
                1L,
                null,
                4,
                false
        );
        given(orderRepository.findAllByOrderTableId(anyLong())).willReturn(orders);

        // when & then
        assertThatThrownBy(() -> table.ungroup(validator))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 상태가 조리중이나 식사중입니다.");
    }
}
