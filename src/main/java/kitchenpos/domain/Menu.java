package kitchenpos.domain;

import java.math.BigDecimal;

public class Menu {

    private Long id;
    private String name;
    private Price price;
    private Long menuGroupId;

    private Menu() {
    }

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public boolean isPriceBiggerThen(Price price) {
        return this.price.isBiggerThen(price.getValue());
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
