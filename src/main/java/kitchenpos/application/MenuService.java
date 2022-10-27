package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import kitchenpos.ui.dto.request.MenuCreateRequest;
import kitchenpos.ui.dto.request.MenuProductRequest;
import kitchenpos.ui.dto.response.MenuResponse;
import kitchenpos.ui.dto.response.MenuResponse.MenuProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(final MenuRepository menuRepository,
                       final MenuGroupRepository menuGroupRepository,
                       final ProductRepository productRepository) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {
        final BigDecimal price = request.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }

        if (!menuGroupRepository.existsById(request.getMenuGroupId())) {
            throw new IllegalArgumentException();
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProduct : request.getMenuProducts()) {
            final Product product = productRepository.findById(menuProduct.getProductId())
                    .orElseThrow(IllegalArgumentException::new);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(it -> new MenuProduct(it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(), menuProducts);
        final Menu savedMenu = menuRepository.save(menu);

        final List<MenuProductResponse> menuProductResponses = savedMenu.getMenuProducts()
                .stream()
                .map(it -> new MenuProductResponse(it.getSeq(), savedMenu.getId(), it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuResponse(
                savedMenu.getId(),
                savedMenu.getName(),
                savedMenu.getPrice(),
                savedMenu.getMenuGroupId(),
                menuProductResponses
        );
    }

    public List<MenuResponse> list() {
        final Iterable<Menu> menus = menuRepository.findAll();

        final List<MenuResponse> menuResponses = new ArrayList<>();
        for (final Menu menu : menus) {
            final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                    .stream()
                    .map(it -> new MenuProductResponse(it.getSeq(), menu.getId(), it.getProductId(),
                            it.getQuantity()))
                    .collect(Collectors.toList());
            final MenuResponse menuResponse = new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                    menu.getMenuGroupId(), menuProductResponses);
            menuResponses.add(menuResponse);
        }
        return menuResponses;
    }
}
