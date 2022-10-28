package kitchenpos.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.ui.dto.MenuCreateRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import kitchenpos.ui.dto.MenuProductCreateRequest;
import kitchenpos.ui.dto.MenuProductResponse;
import kitchenpos.ui.dto.MenuResponse;
import kitchenpos.ui.dto.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
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

        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);
        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, new ArrayList<>());

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductCreateRequest menuProductCreateRequest : request.getMenuProductCreateRequests()) {
            final Long productId = menuProductCreateRequest.getProductId();
            final Product product = productRepository.findById(productId)
                    .orElseThrow(IllegalArgumentException::new);
            final MenuProduct menuProduct = new MenuProduct(null, product, menuProductCreateRequest.getQuantity());
            menuProduct.mapMenu(menu);
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }

        final Menu savedMenu = menuRepository.save(menu);

        return generateMenuResponse(savedMenu);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuService::generateMenuResponse)
                .collect(Collectors.toList());
    }

    private static MenuResponse generateMenuResponse(final Menu it) {
        return new MenuResponse(
                it.getId(),
                it.getName(),
                it.getPrice(),
                new MenuGroupResponse(it.getMenuGroup().getId(), it.getMenuGroup().getName()),
                generateMenuProductResponses(it)
        );
    }

    private static List<MenuProductResponse> generateMenuProductResponses(final Menu savedMenu) {
        return savedMenu.getMenuProducts()
                .stream()
                .map(it -> new MenuProductResponse(
                        it.getSeq(),
                        new ProductResponse(
                                it.getProduct().getId(),
                                it.getProduct().getName(),
                                it.getProduct().getPrice()),
                        it.getQuantity()))
                .collect(Collectors.toList());
    }
}
