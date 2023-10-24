package kitchenpos.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductDto {
    private Long id;
    private String name;
    private BigDecimal price;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ProductDto productDto = (ProductDto) object;
        return Objects.equals(id, productDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
