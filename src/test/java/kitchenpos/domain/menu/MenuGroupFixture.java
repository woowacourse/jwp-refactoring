package kitchenpos.domain.menu;

@SuppressWarnings("NonAsciiCharacters")
public class MenuGroupFixture {

    public static MenuGroup 메뉴_그룹(Long menuGroupId) {
        return new MenuGroup(menuGroupId, "menuGroupName");
    }

    public static MenuGroup 메뉴_그룹() {
        return new MenuGroup("menuGroupName");
    }
}
