package kitchenpos.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MenuName {

    private static final int MAXIMUM_NAME_LENGTH = 255;

    @Column(nullable = false)
    private String name;

    protected MenuName() {
    }

    public MenuName(String name) {
        validate(name);
        this.name = name;
    }

    private void validate(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("메뉴명이 존재하지 않거나 공백입니다.");
        }
        if (name.length() > MAXIMUM_NAME_LENGTH) {
            throw new IllegalArgumentException("메뉴명 길이가 유효하지 않습니다.");
        }
    }

    public String getName() {
        return name;
    }
}
