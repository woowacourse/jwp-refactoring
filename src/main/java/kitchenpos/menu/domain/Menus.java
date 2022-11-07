package kitchenpos.menu.domain;

import java.util.List;

public class Menus {

    private final List<Menu> menus;

    public Menus(final List<Menu> menus) {
        this.menus = menus;
    }

    public int size() {
        return menus.size();
    }

    public List<Menu> getMenus() {
        return menus;
    }
}
