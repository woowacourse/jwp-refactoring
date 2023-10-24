package kitchenpos.menu.application;

import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.vo.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Long create(final MenuCreateRequest request) {
        final Price price = new Price(request.getPrice());

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        validatePriceLessThenProductsSum(request.getMenuProductCreates(), price);

        final Menu savedMenu = menuRepository.save(new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), Collections.emptyList()));

        for (final MenuCreateRequest.MenuProductCreate menuProductCreate : request.getMenuProductCreates()) {
            final Product product = productRepository.findById(menuProductCreate.getProductId()).orElseThrow(IllegalAccessError::new);
            savedMenu.addMenuProduct(new MenuProduct(product.getId(), menuProductCreate.getQuantity()));
        }

        return savedMenu.getId();
    }

    private void validatePriceLessThenProductsSum(final List<MenuCreateRequest.MenuProductCreate> menuProductCreates, final Price price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuCreateRequest.MenuProductCreate menuProductCreate : menuProductCreates) {
            final Product product = productRepository.findById(menuProductCreate.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductCreate.getQuantity())));
        }
        if (price.compareTo(new Price(sum)) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        final List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            menuResponses.add(MenuResponse.from(menu));
        }

        return menuResponses;
    }
}
