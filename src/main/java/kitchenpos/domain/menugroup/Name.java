package kitchenpos.domain.menugroup;

import kitchenpos.exception.ExceptionInformation;
import kitchenpos.exception.KitchenposException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Name {
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 20;

    @Column(nullable = false)
    private String name;

    protected Name(){
    }

    public Name(final String name) {
        this.name = name;
    }

    public static Name create(final String name) {
        validateNotNull(name);
        validateBlank(name);
        validateLength(name);
        return new Name(name);
    }

    private static void validateNotNull(final String name) {
        if(Objects.isNull(name)){
            throw new KitchenposException(ExceptionInformation.MENU_GROUP_NAME_IS_NULL);
        }
    }

    private static void validateBlank(final String name) {
        if (name.isBlank()) {
            throw new KitchenposException(ExceptionInformation.MENU_GROUP_NAME_LENGTH_OUT_OF_BOUNCE);
        }
    }

    private static void validateLength(final String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new KitchenposException(ExceptionInformation.MENU_GROUP_NAME_LENGTH_OUT_OF_BOUNCE);
        }
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Name otherName = (Name) o;
        return Objects.equals(this.name, otherName.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
