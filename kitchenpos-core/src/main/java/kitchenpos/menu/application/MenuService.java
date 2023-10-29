package kitchenpos.menu.application;

import kitchenpos.common.dto.request.CreateMenuRequest;
import kitchenpos.menu.application.event.CreateMenuProductEvent;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.product.domain.repository.MenuProductRepository;
import kitchenpos.product.domain.repository.ProductRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuService {

    private final ApplicationEventPublisher menuProductEventPublisher;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final ApplicationEventPublisher menuProductEventPublisher,
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ProductRepository productRepository
    ) {
        this.menuProductEventPublisher = menuProductEventPublisher;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public Menu create(final CreateMenuRequest menuRequest) {
        final MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                                                       .orElseThrow(() -> new IllegalArgumentException(
                                                               "존재하지 않는 메뉴 그룹입니다."
                                                       ));
        final Menu menu = Menu.of(menuGroup, menuRequest.getName(), menuRequest.getPrice());
        final Menu savedMenu = menuRepository.save(menu);
        menuProductEventPublisher.publishEvent(new CreateMenuProductEvent(menuRequest.getMenuProducts(), savedMenu));
        return savedMenu;
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
