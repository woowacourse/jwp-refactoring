package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.CreateMenuDto;

public class CreateMenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<CreateMenuProductResponse> menuProducts;

    public CreateMenuResponse(final CreateMenuDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.menuGroupId = dto.getMenuGroupId();
        this.menuProducts = convertCreateMenuProductResponses(dto);
    }

    private List<CreateMenuProductResponse> convertCreateMenuProductResponses(final CreateMenuDto createMenuDto) {
        return createMenuDto.getMenuProducts()
                            .stream()
                            .map(createMenuProductDto ->
                                    new CreateMenuProductResponse(createMenuDto, createMenuProductDto)
                            )
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
