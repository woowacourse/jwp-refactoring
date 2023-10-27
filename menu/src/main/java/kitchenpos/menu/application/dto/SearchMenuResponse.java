package kitchenpos.menu.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;

public class SearchMenuResponse {

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

    public SearchMenuResponse(
            Long id,
            String name,
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

    public static SearchMenuResponse from(Menu menu) {
        return new SearchMenuResponse(
                menu.id(),
                menu.name(),
                menu.price().value(),
                MenuGroupResponse.from(menu.menuGroup()),
                menu.menuProducts().items().stream()
                        .map(MenuProductResponse::from)
                        .collect(Collectors.toList())
        );
    }
}
