package kitchenpos.domain.common;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.domain.exception.InvalidNameException;
import org.springframework.util.StringUtils;

@Embeddable
public class Name {

    private static final int MAXIMUM_NAME_LENGTH = 255;

    @Column
    private String name;

    protected Name() {
    }

    public Name(final String name) {
        validateName(name);

        this.name = name;
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name) || name.length() > MAXIMUM_NAME_LENGTH) {
            throw new InvalidNameException();
        }
    }

    public String getName() {
        return name;
    }
}
