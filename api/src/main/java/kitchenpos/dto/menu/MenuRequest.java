package kitchenpos.dto.menu;

import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRequest {
    @NotBlank(message = "메뉴명이 null이거나 비어있습니다.")
    private String name;
    @NotNull(message = "메뉴의 가격이 null입니다.")
    @DecimalMin(value = "0", message = "메뉴의 가격이 음수입니다.")
    private BigDecimal price;
    @NotNull(message = "메뉴 생성 시 메뉴 그룹의 아이디가 null입니다.")
    private Long menuGroupId;
    @NotEmpty(message = "메뉴 생성 시 메뉴 상품이 비어있습니다.")
    private List<MenuProductRequest> menuProducts;

    private MenuRequest() {
    }

    private MenuRequest(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static MenuRequest of(String name, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        return new MenuRequest(
                name,
                price,
                menuGroupId,
                menuProducts
        );
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductRequest> getMenuProducts() {
        return menuProducts;
    }

    public List<MenuProduct> toMenuProducts(Menu menu) {
        return menuProducts.stream()
                .map(menuProductRequest -> menuProductRequest.toMenuProduct(menu))
                .collect(Collectors.toList());
    }

    public Menu toMenu() {
        return new Menu(
                name,
                price
        );
    }
}
