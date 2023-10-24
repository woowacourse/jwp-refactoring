package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class CreateMenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<CreateMenuProductResponse> menuProducts;

    public CreateMenuResponse(final Menu menu) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroupId = menu.getMenuGroupId();
        this.menuProducts = convertCreateMenuProductResponses(menu);
    }

    private List<CreateMenuProductResponse> convertCreateMenuProductResponses(final Menu menu) {
        return menu.getMenuProducts()
                   .stream()
                   .map(menuProduct -> new CreateMenuProductResponse(menu, menuProduct))
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

    public List<CreateMenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}
