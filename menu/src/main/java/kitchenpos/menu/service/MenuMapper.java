package kitchenpos.menu.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuMapper {

    private final ProductService productService;

    public MenuMapper(ProductService productService) {
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public Menu toEntity(MenuDto menuDto) {
        Long id = menuDto.getId();
        BigDecimal menuPrice = menuDto.getPrice();
        Long menuGroupId = menuDto.getMenuGroupId();
        String name = menuDto.getName();
        List<MenuProduct> menuProducts = toMenuProducts(menuDto.getMenuProductDtos());

        return new Menu.Builder()
            .id(id)
            .name(name)
            .menuGroupId(menuGroupId)
            .menuProducts(menuProducts)
            .price(menuPrice)
            .build();
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductDto> menuProductDtos) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductDto menuProductDto : menuProductDtos) {
            Product product = productService.getById(menuProductDto.getProductId());

            MenuProduct menuProduct = new MenuProduct.Builder()
                .name(product.getName())
                .price(product.getPrice())
                .quantity(menuProductDto.getQuantity())
                .build();
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }
}
