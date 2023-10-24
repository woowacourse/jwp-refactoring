package kitchenpos.domain.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.relational.core.mapping.Column;

public class Name {
    public static final int MAX_NAME_LENGTH = 255;
    @JsonProperty("name")
    @Column("NAME")
    private final String value;

    public Name(final String value) {
        if (value.isEmpty() || value.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }
}
