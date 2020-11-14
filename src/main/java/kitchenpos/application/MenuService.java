package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuCreateRequest;
import kitchenpos.dto.request.MenuProductCreateRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.exception.MenuGroupNotFoundException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;
    private final MenuPriceValidateStrategy menuPriceValidateStrategy;

    public MenuService(
        final MenuRepository menuRepository,
        final MenuGroupRepository menuGroupRepository,
        final MenuProductRepository menuProductRepository,
        final ProductRepository productRepository,
        final MenuPriceValidateStrategy menuPriceValidateStrategy
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
        this.menuPriceValidateStrategy = menuPriceValidateStrategy;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        Menu menu = request.toEntity();
        List<MenuProductCreateRequest> menuProductRequests = request.getMenuProducts();
        final BigDecimal price = menu.getPrice();

        if (!menuGroupRepository.existsById(menu.getMenuGroupId())) {
            throw new MenuGroupNotFoundException(menu.getMenuGroupId());
        }

        List<Product> products = productRepository.findAllById(request.getProductIds());
        Map<Long, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getId, p -> p));

        menuPriceValidateStrategy.validate(products, menuProductRequests, price);

        final Menu savedMenu = menuRepository.save(menu);
        final Long menuId = savedMenu.getId();

        List<MenuProduct> menuProducts = menuProductRequests.stream()
            .map(p -> p.toEntity(menuId))
            .collect(Collectors.toList());

        menuProductRepository.saveAll(menuProducts);

        return MenuResponse.of(savedMenu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
