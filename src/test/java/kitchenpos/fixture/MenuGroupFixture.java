package kitchenpos.fixture;

import kitchenpos.domain.MenuGroup;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹_엔티티_A = createMenuGroup(1L, "한식");
    public static MenuGroup 메뉴_그룹_엔티티_B = createMenuGroup(2L, "일식");

    private static MenuGroup createMenuGroup(Long id, String name) {
        return MenuGroup.builder()
                .name(name)
                .build();
    }
}
