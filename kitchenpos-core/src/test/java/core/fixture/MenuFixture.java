package core.fixture;

import core.application.dto.MenuRequest;
import core.domain.Menu;
import core.domain.MenuProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static core.application.dto.MenuRequest.MenuProductRequest;

public class MenuFixture {

    public static Menu menu(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
    }

    public static Menu menu(String name, Long price, Long menuGroupId) {
        return new Menu(null, name, BigDecimal.valueOf(price), menuGroupId, new ArrayList<>());
    }

    public static MenuRequest menuRequest(String name, Long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        List<MenuProductRequest> requests = menuProducts.stream()
                .map(it -> new MenuProductRequest(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());
        return new MenuRequest(name, BigDecimal.valueOf(price), menuGroupId, requests);
    }
}
