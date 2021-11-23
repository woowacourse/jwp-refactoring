package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Name {

    @Column(name = "name")
    private String value;

    protected Name() {
    }

    public Name(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
