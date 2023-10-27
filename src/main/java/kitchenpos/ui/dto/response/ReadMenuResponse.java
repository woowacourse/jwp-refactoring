package kitchenpos.ui.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.application.dto.ReadMenuDto;

public class ReadMenuResponse {

    private final Long id;
    private final String name;
    private final BigDecimal price;
    private final Long menuGroupId;
    private final List<ReadMenuProductResponse> menuProducts;

    public ReadMenuResponse(final ReadMenuDto dto) {
        this.id = dto.getId();
        this.name = dto.getName();
        this.price = dto.getPrice();
        this.menuGroupId = dto.getMenuGroupId();
        this.menuProducts = convertReadMenuProductResponses(dto);
    }

    private List<ReadMenuProductResponse> convertReadMenuProductResponses(final ReadMenuDto readMenuDto) {
        return readMenuDto.getMenuProducts()
                          .stream()
                          .map(readMenuProductDto -> new ReadMenuProductResponse(readMenuDto, readMenuProductDto))
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
