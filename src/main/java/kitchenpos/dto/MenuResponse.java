package kitchenpos.dto;

import kitchenpos.domain.menu.Menu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuGroupResponse menuGroup;
    private List<ProductResponse> products;

    public MenuResponse(Menu menu, List<ProductResponse> products) {
        this.id = menu.getId();
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.menuGroup = new MenuGroupResponse(menu.getMenuGroup());
        this.products = products;
    }
}
