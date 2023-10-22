package kitchenpos.domain;

import java.util.List;
import org.springframework.data.relational.core.mapping.MappedCollection;

public class Menus {

    @MappedCollection(idColumn = "id")
    private List<Menu> menus;

    public Menus() {
    }

    public Menus(final List<Menu> menus) {
        this.menus = menus;
    }

    public List<Menu> getMenus() {
        return menus;
    }
}
