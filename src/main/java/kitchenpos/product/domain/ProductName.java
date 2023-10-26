package kitchenpos.product.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import org.springframework.util.StringUtils;

@Embeddable
public class ProductName {

    private static final int MAX_NAME_LENGTH = 255;

    @NotNull
    private String name;

    protected ProductName() {
    }

    private ProductName(String name) {
        validate(name);

        this.name = name;
    }

    private void validate(String name) {
        if (!StringUtils.hasText(name) || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("상품 이름은 1글자 이상, 255자 이하여야 합니다.");
        }
    }

    public static ProductName from(String name) {
        return new ProductName(name);
    }

    public String getName() {
        return name;
    }

}
