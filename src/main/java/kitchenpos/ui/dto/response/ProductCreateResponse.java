package kitchenpos.ui.dto.response;

import java.math.BigDecimal;

public class ProductCreateResponse {

    private Long id;

    private String name;

    private BigDecimal bigDecimal;

    private ProductCreateResponse() {
    }

    public ProductCreateResponse(final Long id, final String name, final BigDecimal bigDecimal) {
        this.id = id;
        this.name = name;
        this.bigDecimal = bigDecimal;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBigDecimal() {
        return bigDecimal;
    }
}
