package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.*;
import kitchenpos.dto.request.menu.CreateMenuRequest;
import kitchenpos.dto.response.menu.MenuResponse;

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
    public MenuResponse create(final CreateMenuRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                                                       .orElseThrow(() -> new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다."));

        final Menu menu = new Menu(request.getName(), request.getPrice(), menuGroup);
        final List<MenuProduct> menuProducts = getMenuProducts(menu, request);
        menu.addMenuProducts(menuProducts);

        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private List<MenuProduct> getMenuProducts(Menu menu, CreateMenuRequest request) {
        return request.getMenuProducts()
                      .stream()
                      .map(item -> {
                          Product product = productRepository.findById(item.getProductId())
                                                             .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
                          return new MenuProduct(menu, product, item.getQuantity());
                      })
                      .collect(Collectors.toList());
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll()
                             .stream()
                             .map(MenuResponse::from)
                             .collect(Collectors.toList());
    }
}
