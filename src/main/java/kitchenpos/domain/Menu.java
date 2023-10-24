package kitchenpos.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import kitchenpos.domain.exception.MenuException.InvalidMenuNameException;
import org.springframework.lang.NonNull;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @NonNull
    private String name;
    @Embedded
    private Price price;
    @ManyToOne
    private MenuGroup menuGroup;

    protected Menu() {
    }

    private Menu(final String name,
                 final Price price,
                 final MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final MenuGroup menuGroup) {
        validateName(name);

        return new Menu(name, Price.from(price), menuGroup);
    }

    private static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidMenuNameException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }
}
