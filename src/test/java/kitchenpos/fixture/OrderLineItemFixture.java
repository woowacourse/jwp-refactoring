package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderLineItem;

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
        MenuGroup menuGroup = MenuGroup.from("존재하지 않는 메뉴 그룹");
        Menu menu = Menu.of("존재하지 않는 메뉴", Long.MAX_VALUE, menuGroup.getId());

        return OrderLineItem.of(
                menu.getName(),
                menu.getPrice(),
                menu.getId(),
                1L
        );
    }

}
