package kitchenpos.menu.domain;

import java.util.List;
import java.util.Objects;

public class Menus {
    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public Menu findById(Long menuId) {
        return this.menus.stream()
                .filter(menu -> Objects.equals(menu.getId(), menuId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format("%d번 메뉴: 존재하지 않는 메뉴입니다.", menuId)));
    }

    public int size() {
        return this.menus.size();
    }
}
