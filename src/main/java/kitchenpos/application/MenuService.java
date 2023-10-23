package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.product.Products;
import kitchenpos.dto.menu.MenuProductDto;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
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
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
        final BigDecimal productsPrice = getProductsPrice(request.getProductIds());
        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId());
        menu.validateOverPrice(productsPrice);
        menuRepository.save(menu);
        addMenuProduct(menu, request.getMenuProducts());
        return MenuResponse.from(menu);
    }

    private BigDecimal getProductsPrice(final List<Long> productIds) {
        final Products products = new Products(productRepository.findAllById(productIds));
        products.validateProductsCnt(productIds.size());
        return products.getTotalPrice();
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
