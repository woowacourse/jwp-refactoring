package kitchenpos.menu.ui.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import kitchenpos.menu.application.dto.CreateProductCommand;

public class CreateProductRequest {

    @JsonProperty("name")
    private final String name;
    @JsonProperty("price")
    private final BigDecimal price;

    public CreateProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public CreateProductCommand toCommand() {
        return new CreateProductCommand(name, price);
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price;
    }
}
