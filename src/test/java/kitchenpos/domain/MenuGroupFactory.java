package kitchenpos.domain;

public final class MenuGroupFactory {

    private MenuGroupFactory() {
    }

    public static MenuGroup createMenuGroupOf(final String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}
