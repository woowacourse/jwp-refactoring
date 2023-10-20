package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.builder()
                .name(name)
                .build();
    }
}
