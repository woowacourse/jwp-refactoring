package kitchenpos.fixture;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.OrderLineItem;

@SuppressWarnings("NonAsciiCharacters")
public class OrderLineItemFixture {

    public static OrderLineItem 메뉴을_가진_주문_항목_생성(Menu savedMenu, long quantity) {
        return OrderLineItem.of(
                savedMenu.getName(),
                savedMenu.getPrice(),
                savedMenu.getId(),
                quantity
        );
    }

    public static OrderLineItem 존재하지_않는_메뉴를_가진_주문_항목_생성() {
        return OrderLineItem.of(
                "존재하지 않는 메뉴",
                BigDecimal.valueOf(Long.MAX_VALUE),
                null,
                1L
        );
    }

}
