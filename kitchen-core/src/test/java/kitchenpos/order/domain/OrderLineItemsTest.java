package kitchenpos.order.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

class OrderLineItemsTest {

    @Test
    @DisplayName("extractMenuIds()를 호출하면 메뉴 id의 리스트를 얻을 수 있다.")
    void extractMenuIds() {
        //given
        final OrderLineItems orderLineItems = new OrderLineItems(
                List.of(
                        new OrderLineItem(1L, 1L),
                        new OrderLineItem(3L, 2L),
                        new OrderLineItem(2L, 1L),
                        new OrderLineItem(4L, 3L)
                )
        );
        final List<Long> expected = List.of(1L, 3L, 2L, 4L);

        //when
        final List<Long> actual = orderLineItems.extractMenuIds();

        //then
        Assertions.assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("isEmpty()를 호출하면 OrderLineItems의 리스트가 비어있는지 여부를 알 수 있다.")
    void isEmpty() {
        //given
        final OrderLineItems orderLineItems = new OrderLineItems(List.of());

        //when
        boolean actual = orderLineItems.isEmpty();

        //then
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("isDifferentSize()를 호출하면 OrderLineItems의 크기가 입력받은 값과 다른지 여부를 알 수 있다.")
    void isDifferentSize() {
        //given
        final OrderLineItems orderLineItems = new OrderLineItems(
                List.of(
                        new OrderLineItem(1L, 1L),
                        new OrderLineItem(3L, 2L)
                )
        );

        //when
        boolean actual = orderLineItems.isDifferentSize(3L);

        //then
        Assertions.assertThat(actual).isTrue();
    }
}