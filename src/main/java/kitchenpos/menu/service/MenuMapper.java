package kitchenpos.menu.service;

import static kitchenpos.exception.ExceptionType.MENU_GROUP_NOT_FOUND;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import kitchenpos.exception.CustomException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.product.domain.Product;
import kitchenpos.product.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final ProductService productService;
    private final MenuGroupRepository menuGroupRepository;

    public MenuMapper(ProductService productService, MenuGroupRepository menuGroupRepository) {
        this.productService = productService;
        this.menuGroupRepository = menuGroupRepository;
    }

    public Menu toEntity(MenuDto menuDto) {
        BigDecimal menuPrice = menuDto.getPrice();
        Long menuGroupId = menuDto.getMenuGroupId();
        String name = menuDto.getName();
        List<MenuProduct> menuProducts = toMenuProducts(menuDto.getMenuProductDtos());

        validateMenuGroupExists(menuGroupId);

        return new Menu.Builder()
            .name(name)
            .menuGroupId(menuGroupId)
            .menuProducts(menuProducts)
            .price(menuPrice)
            .build();
    }

    private void validateMenuGroupExists(Long menuGroupId) {
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new CustomException(MENU_GROUP_NOT_FOUND);
        }
    }

    private List<MenuProduct> toMenuProducts(List<MenuProductDto> menuProductDtos) {
        List<MenuProduct> menuProducts = new ArrayList<>();

        for (MenuProductDto menuProductDto : menuProductDtos) {
            Product product = productService.getById(menuProductDto.getProductId());
            MenuProduct menuProduct = new MenuProduct.Builder()
                .product(product)
                .quantity(menuProductDto.getQuantity())
                .build();
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }
}
