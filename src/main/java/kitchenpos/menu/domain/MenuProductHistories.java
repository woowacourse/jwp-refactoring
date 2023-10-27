package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProductHistories {

    @JoinColumn(name = "menu_history_id", nullable = false)
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProductHistory> menuProductHistories;

    protected MenuProductHistories() {
    }

    public MenuProductHistories(final List<MenuProductHistory> menuProductHistories) {
        this.menuProductHistories = menuProductHistories;
    }

    public static MenuProductHistories from(final MenuProducts menuProducts) {
        return new MenuProductHistories(mapMenuProductHistories(menuProducts));
    }

    private static List<MenuProductHistory> mapMenuProductHistories(final MenuProducts menuProducts) {
        return menuProducts.getMenuProductItems()
                .stream()
                .map(MenuProductHistory::from)
                .collect(Collectors.toList());
    }

    public List<MenuProductHistory> getMenuProductHistories() {
        return menuProductHistories;
    }
}
