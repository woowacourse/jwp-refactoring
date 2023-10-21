package kitchenpos.fixture;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu createMenu(Long id, String name, long price) {
        return Menu.builder()
                .name(name)
                .price(price)
                .menuGroup(MenuGroup.builder().build())
                .build();
    }
}
