package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class ReadMenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<ReadMenuProductResponse> menuProducts;

    public ReadMenuResponse(final Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroup().getId();
        this.menuProducts = convertReadMenuProductResponses(menu);
    }

    private List<ReadMenuProductResponse> convertReadMenuProductResponses(final Menu menu) {
        return menu.getMenuProducts()
                   .stream()
                   .map(menuProduct -> new ReadMenuProductResponse(menu, menuProduct))
                   .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<ReadMenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
