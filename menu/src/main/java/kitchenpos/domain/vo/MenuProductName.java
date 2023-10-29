package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuProductName {

    private static final int MAXIMUM_NAME_LENGTH = 255;

    @Column(nullable = false)
    private String name;

    protected MenuProductName() {
    }

    public MenuProductName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("상품명이 존재하지 않거나 공백입니다.");
        }
        if (name.length() > MAXIMUM_NAME_LENGTH) {
            throw new IllegalArgumentException("상품명 길이가 유효하지 않습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
