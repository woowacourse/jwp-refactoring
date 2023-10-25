package kitchenpos.menugroup.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidNameException;

@Embeddable
public class MenuGroupName {

    @Column(name = "name", nullable = false)
    private String value;

    protected MenuGroupName() {
    }

    public MenuGroupName(final String value) {
        validate(value);
        this.value = value.strip();
    }

    private void validate(final String value) {
        validateNotBlank(value);
        validateLength(value.strip());
    }

    private void validateNotBlank(final String value) {
        if (value == null || value.isBlank()) {
            throw new InvalidNameException("메뉴 그룹 이름은 공백일 수 없습니다.");
        }
    }

    private void validateLength(final String value) {
        if (value.length() > 255) {
            throw new InvalidNameException("메뉴 그룹 이름은 255자를 초과할 수 없습니다.");
        }
    }

    public String getValue() {
        return value;
    }
}
