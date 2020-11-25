package kitchenpos.domain;

import java.util.List;
import java.util.Objects;

public class Menus {

    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public Menu findById(Long menuId) {
        return menus.stream()
            .filter(menu -> Objects.equals(menu.getId(), menuId))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴 입니다."));
    }

}
