package kitchenpos.domain.menu;

import java.util.List;

public class Menus {

    private List<Menu> values;

    public Menus(List<Menu> values) {
        this.values = values;
    }

    public void validateDistinctSize(int size) {
        if (distinctSize() != size) {
            throw new IllegalArgumentException();
        }
    }

    private long distinctSize() {
        return values.stream().map(Menu::getId).distinct().count();
    }
}
