package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MenuProducts {

    private final List<MenuProduct> values = new ArrayList<>();

    public MenuProducts(final List<MenuProduct> values) {
        this.values.addAll(values);
    }

    public void addValues(final List<MenuProduct> menuProducts) {
        values.addAll(menuProducts);
    }

    public List<MenuProduct> getValues() {
        return Collections.unmodifiableList(values);
    }
}
