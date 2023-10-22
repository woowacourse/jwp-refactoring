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
        final Menu menu = Menu.forSave(request.getName(), request.getPrice(), request.getMenuGroupId());
        validateMenuPrice(menu, request.getMenuProducts());
        final Menu savedMenu = menuRepository.save(menu);
        addMenuProduct(savedMenu, request.getMenuProducts());
        return MenuResponse.from(savedMenu);
    }

    private void validateMenuPrice(final Menu menu, final List<MenuProductDto> menuProducts) {
        final List<Long> productIds = menuProducts.stream()
            .map(MenuProductDto::getProductId)
            .collect(Collectors.toUnmodifiableList());

        final List<Product> products = productRepository.findAllById(productIds);

        if (menuProducts.size() != products.size()) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal::multiply)
            .get();

        menu.validateOverPrice(sum);
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
