package kitchenpos.application;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.dto.MenuRequest;
import kitchenpos.exception.menu.NoSuchMenuGroupException;
import kitchenpos.exception.product.NoSuchProductException;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuProductRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Menu create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(NoSuchMenuGroupException::new);

        List<MenuProduct> menuProducts = menuRequest.getMenuProductRequests().stream()
                .map(menuProductRequest -> {
                    Product product = productRepository.findById(menuProductRequest.getProductId())
                            .orElseThrow(NoSuchProductException::new);
                    return new MenuProduct(product, menuProductRequest.getQuantity());
                })
                .collect(Collectors.toList());

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup, menuProducts);

        // todo 영속성 전이
        menuProductRepository.saveAll(menuProducts);

        return menuRepository.save(menu);
    }

    public List<Menu> list() {
        final List<Menu> menus = menuRepository.findAll();

        // todo Fetch Join으로 초기화 및 Response 객체 생성

        return menus;
    }
}
