package kitchenpos.fixture;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuFixture {

    public static Menu createMenu(Long id, String name, long price) {
        return Menu.builder()
                .name(name)
                .price(price)
                .menuGroupId(MenuGroup.builder().build().getId())
                .build();
    }
}
