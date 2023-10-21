package kitchenpos.domain.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.relational.core.mapping.Column;

public class Name {
    @JsonProperty("name")
    @Column("NAME")
    private final String value;

    public Name(final String value) {
        this.value = value;
    }
}
