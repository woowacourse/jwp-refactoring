package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.*;
import kitchenpos.dto.request.CreateMenuRequest;
import kitchenpos.dto.response.MenuResponse;

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

        validatesProductExist(request);
        final Menu menu = request.toEntity(menuGroup);
        final Menu savedMenu = menuRepository.save(menu);
        return MenuResponse.from(savedMenu);
    }

    private void validatesProductExist(CreateMenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts();
        for (MenuProduct menuProduct : menuProducts) {
            productRepository.findById(menuProduct.getProduct().getId())
                             .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        }
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                    .map(MenuResponse::from)
                    .collect(Collectors.toList());
    }
}
