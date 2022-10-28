package kitchenpos.domain;

import java.math.BigDecimal;
import kitchenpos.dto.request.MenuRequest;

public class Menu {

    private final Long id;
    private final String name;
    private final Price price;
    private final Long menuGroupId;

    public Menu(Long id, String name, Price price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, Price price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public static Menu from(MenuRequest menuRequest) {
        return new Menu(menuRequest.getName(),
                new Price(menuRequest.getPrice()),
                menuRequest.getMenuGroupId());
    }

    public void validatePrice(BigDecimal sum) {
        if (price.getValue().compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격은 상품 수량 * 상품 가격의 합보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }
}
