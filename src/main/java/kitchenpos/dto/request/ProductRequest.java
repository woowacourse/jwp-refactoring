package kitchenpos.dto.request;


import java.math.BigDecimal;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

public class ProductRequest {

    @NotNull(message = "이름을 입력해 주세요")
    private final String name;

    @NotNull(message = "금액을 입력해주세요.")
    @DecimalMin(value = "0", message = "금액이 0원보다 작을 수 없습니다.")
    private final BigDecimal price;

    public ProductRequest(final String name, final BigDecimal price) {
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
