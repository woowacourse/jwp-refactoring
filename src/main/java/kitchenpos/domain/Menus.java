package kitchenpos.domain;

import java.util.List;
import java.util.stream.Collectors;

public class Menus {

    private final List<Menu> menus;

    public Menus(List<Menu> menus) {
        this.menus = menus;
    }

    public boolean isSameSize(int size) {
        return menus.size() == size;
    }

    public List<Long> extractIds() {
        return menus.stream()
            .map(o -> o.getId())
            .collect(Collectors.toList());
    }
}
