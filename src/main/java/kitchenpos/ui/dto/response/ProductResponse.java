package kitchenpos.ui.dto.response;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;

    private String name;

    private BigDecimal bigDecimal;

    private ProductResponse() {
    }

    public ProductResponse(final Long id, final String name, final BigDecimal bigDecimal) {
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
