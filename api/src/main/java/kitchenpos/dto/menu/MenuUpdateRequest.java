package kitchenpos.dto.menu;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MenuUpdateRequest {
    @NotBlank(message = "메뉴명이 null이거나 비어있습니다.")
    private String name;
    @NotNull(message = "메뉴의 가격이 null입니다.")
    @DecimalMin(value = "0", message = "메뉴의 가격이 음수입니다.")
    private BigDecimal price;

    private MenuUpdateRequest() {

    }

    private MenuUpdateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static MenuUpdateRequest of(String name, BigDecimal price) {
        return new MenuUpdateRequest(name, price);
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
