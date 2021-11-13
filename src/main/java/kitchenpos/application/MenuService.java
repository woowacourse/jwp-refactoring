package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuRequest;
import kitchenpos.dto.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class MenuService {

    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final ProductRepository productRepository;
    private final MenuProductRepository menuProductRepository;

    public MenuService(MenuGroupRepository menuGroupRepository, MenuRepository menuRepository, ProductRepository productRepository, MenuProductRepository menuProductRepository) {
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.productRepository = productRepository;
        this.menuProductRepository = menuProductRepository;
    }

    public MenuResponse create(final MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProductRequests().stream()
                .map(it -> {
                    Product product = productRepository.findById(it.getProductId()).orElseThrow(IllegalArgumentException::new);
                    return menuProductRepository.save(new MenuProduct(it.getSeq(), product, it.getQuantity()));
                }).collect(Collectors.toList());

        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts);
        menuRepository.save(menu);

        return MenuResponse.of(menu);
    }

    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return MenuResponse.listOf(menus);
    }
}
