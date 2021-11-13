package kitchenpos.application;

import kitchenpos.domain.*;
import kitchenpos.dto.MenuProductRequest;
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
        List<MenuProduct> menuProducts = saveMenuProducts(request.getMenuProductRequests());
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(IllegalArgumentException::new);
        Menu menu = menuRepository.save(new Menu(request.getName(), request.getPrice(), menuGroup, menuProducts));
        return MenuResponse.of(menu);
    }

    private List<MenuProduct> saveMenuProducts(List<MenuProductRequest> requests) {
        return requests.stream()
                .map(it -> {
                    Product product = productRepository.findById(it.getProductId()).orElseThrow(IllegalArgumentException::new);
                    return menuProductRepository.save(new MenuProduct(it.getSeq(), product, it.getQuantity()));
                }).collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return MenuResponse.listOf(menuRepository.findAll());
    }
}
