package kitchenpos.application;

import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.ui.request.MenuCreateRequest;
import kitchenpos.ui.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public Long create(final MenuCreateRequest request) {
        final List<MenuProduct> menuProducts = request.getMenuProducts().stream()
                .map(it -> new MenuProduct(
                                productRepository.findById(it.getProductId()).get(),
                                it.getQuantity()
                        )
                ).collect(Collectors.toList());

        final Menu savedMenu = menuRepository.save(
                new Menu(
                        request.getName(),
                        request.getPrice(),
                        menuGroupRepository.findById(request.getMenuGroupId()).get(),
                        menuProducts
                )
        );
        return savedMenu.getId();
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }
}
