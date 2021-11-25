package kitchenpos.menu.ui.request;

import java.math.BigDecimal;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class MenuUpdateRequest {

    @NotBlank(message = "메뉴 이름이 null이거나 비어있습니다.")
    private String name;

    @NotNull(message = "메뉴의 가격이 null입니다.")
    @Min(value = 0, message = "메뉴의 가격이 0보다 작습니다.")
    private BigDecimal price;

    protected MenuUpdateRequest() {
    }

    public MenuUpdateRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
