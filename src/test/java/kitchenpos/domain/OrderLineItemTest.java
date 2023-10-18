package kitchenpos.domain;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;

class OrderLineItemTest {

    @DisplayName("[SUCCESS] 생성한다.")
    @Test
    void success_create() {
        // given
        final MenuGroup menuGroup = new MenuGroup(new Name("테스트용 메뉴 그룹명"));
        final Menu menu = new Menu(new Name("테스트용 메뉴명"), new Price("10000"), menuGroup, new ArrayList<>());

        final OrderTable orderTable = new OrderTable(null, 10, true);
        final Order order = new Order(orderTable, OrderStatus.COOKING, LocalDateTime.now(), new ArrayList<>());

        // expect
        assertThatCode(() -> new OrderLineItem(order, menu, new Quantity(10)))
                .doesNotThrowAnyException();
    }
}
