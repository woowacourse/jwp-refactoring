package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.domain.exception.MenuException.InvalidMenuNameException;
import kitchenpos.product.domain.Price;
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
    @Column
    private Long menuGroupId;
    @Column
    private boolean deleted = false;

    protected Menu() {
    }

    private Menu(final String name,
                 final Price price,
                 final Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(final String name,
                          final BigDecimal price,
                          final Long menuGroupId) {
        validateName(name);

        return new Menu(name, Price.from(price), menuGroupId);
    }

    private static void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new InvalidMenuNameException();
        }
    }

    public void delete() {
        deleted = true;
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
        return menuGroupId;
    }
}
