package kitchenpos.application.menu.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;

public class CreateMenuResponse {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("menuGroup")
    private MenuGroupResponse menuGroupResponse;
    @JsonProperty("menuProducts")
    private List<MenuProductResponse> menuProductResponses;

    public CreateMenuResponse(
            Long id, String name,
            BigDecimal price,
            MenuGroupResponse menuGroupResponse,
            List<MenuProductResponse> menuProductResponses
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupResponse = menuGroupResponse;
        this.menuProductResponses = menuProductResponses;
    }

    public static CreateMenuResponse from(Menu menu) {
        return new CreateMenuResponse(
                menu.id(),
                menu.name(),
                menu.price().value(),
                MenuGroupResponse.from(menu.menuGroup()),
                menu.menuProducts().items().stream()
                        .map(MenuProductResponse::from)
                        .collect(Collectors.toList())
        );
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }

    public MenuGroupResponse menuGroupResponse() {
        return menuGroupResponse;
    }

    public List<MenuProductResponse> menuProductResponses() {
        return menuProductResponses;
    }
}
