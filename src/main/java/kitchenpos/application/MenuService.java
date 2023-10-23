package kitchenpos.application;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequset;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long create(final MenuRequset requset) {
        final BigDecimal price = requset.getPrice();

        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("메뉴의 가격이 올바르지 않습니다.");
        }

        if (!menuGroupRepository.existsById(requset.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }

        final List<MenuProductRequest> menuProductRequests = requset.getMenuProductRequests();

        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final Product product = productRepository.findById(menuProductRequest.getProductId())
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            sum = sum.add(product.getPrice().multiply(BigDecimal.valueOf(menuProductRequest.getQuantity())));
        }

        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격들의 합보다 큽니다.");
        }

        final Menu menu = new Menu(requset.getName(), requset.getPrice(), requset.getMenuGroupId());
        final Menu savedMenu = menuRepository.save(menu);

        for (final MenuProductRequest menuProductRequest : menuProductRequests) {
            final MenuProduct menuProduct = new MenuProduct(
                    savedMenu,
                    menuProductRequest.getProductId(),
                    menuProductRequest.getQuantity()
            );
            MenuProduct savedMenuProduct = menuProductRepository.save(menuProduct);
            savedMenu.addMenuProduct(savedMenuProduct);
        }

        return savedMenu.getId();
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(Collectors.toUnmodifiableList());
    }
}
