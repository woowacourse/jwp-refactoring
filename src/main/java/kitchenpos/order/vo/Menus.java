package kitchenpos.order.vo;

import java.util.List;
import java.util.Objects;
import kitchenpos.menu.domain.Menu;

public class Menus {

    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public MenuSpecification getMenuSpecificationById(Long menuId) {
        Menu menu = getById(menuId);
        return new MenuSpecification(menu.getName(), menu.getPriceValue());
    }

    private Menu getById(Long menuId) {
        return menus.stream()
                .filter(menu -> Objects.equals(menu.getId(), menuId))
                .findAny()
                .orElseThrow();
    }

    public int counts() {
        return menus.size();
    }

    public List<Menu> getMenus() {
        return menus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menus menus1 = (Menus) o;
        return Objects.equals(menus, menus1.menus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menus);
    }
}
