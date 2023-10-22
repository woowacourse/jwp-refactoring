package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuProductDto;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final MenuProductRepository menuProductRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }
        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        final BigDecimal productsPrice = getProductsPrice(menu, request.getMenuProducts());
        menu.validateOverPrice(productsPrice);
        menuRepository.save(menu);
        addMenuProduct(menu, request.getMenuProducts());
        return MenuResponse.from(menu);
    }

    private BigDecimal getProductsPrice(final Menu menu, final List<MenuProductDto> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
            .map(MenuProductDto::getProductId)
            .collect(Collectors.toUnmodifiableList());

        final List<Product> products = productRepository.findAllById(productIds);

        if (menuProducts.size() != products.size()) {
            throw new IllegalArgumentException();
        }

        return products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal::multiply)
            .get();
    }

    private void addMenuProduct(final Menu menu, final List<MenuProductDto> menuProductDtos) {
        final List<MenuProduct> menuProducts = menuProductDtos.stream()
            .map(menuProductDto -> MenuProduct.forSave(menuProductDto.getProductId(), menuProductDto.getQuantity()))
            .collect(Collectors.toUnmodifiableList());

        for (MenuProduct menuProduct : menuProducts) {
            menuProductRepository.save(menuProduct);
            menu.addMenuProduct(menuProduct);
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toUnmodifiableList());
    }
}
