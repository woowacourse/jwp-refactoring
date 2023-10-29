package kitchenpos.Product.presentation.dto;

import java.math.BigDecimal;

public class ProductCreateRequest {

    private String name;
    private BigDecimal price;

    public ProductCreateRequest() {
    }

    public ProductCreateRequest(final String name,
                                final BigDecimal price) {
//        if (Objects.isNull(menuGroupId)) {
//            throw new IllegalArgumentException("메뉴그룹 아이디 값은 null이 될 수 없습니다.");
//        }
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
