package kitchenpos.dto.response;

import java.math.BigDecimal;

public class MenuResponse {

    private Long id;
    private MenuGroupDto menuGroupDto;
    private BigDecimal price;
    private String name;

    public MenuResponse() {
    }

    public MenuResponse(final Long id, final MenuGroupDto menuGroupDto, final BigDecimal price, final String name) {
        this.id = id;
        this.menuGroupDto = menuGroupDto;
        this.price = price;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public MenuGroupDto getMenuGroupDto() {
        return menuGroupDto;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }
}
