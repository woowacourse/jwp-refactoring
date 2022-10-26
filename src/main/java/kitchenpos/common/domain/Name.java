package kitchenpos.common.domain;

public class Name {

    private final String value;

    private Name(String value) {
        this.value = value;
    }

    public static Name of(String name) {
        return new Name(name);
    }

    public String getValue() {
        return value;
    }
}
