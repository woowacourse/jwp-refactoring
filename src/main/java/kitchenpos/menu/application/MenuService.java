package kitchenpos.menu.application;

import kitchenpos.common.event.ValidatorProductBeing;
import kitchenpos.common.event.ValidatorProductPrice;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuProductCreateRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher publisher;

    public MenuService(final MenuRepository menuRepository, final MenuGroupRepository menuGroupRepository, final ApplicationEventPublisher publisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public Menu create(final MenuCreateRequest req) {
        List<MenuProduct> menuProducts = req.toMenuProducts();
        List<Long> productIds = getRequestProductIds(req);

        MenuGroup menuGroup = findMenuGroup(req.getMenuGroupId());
        Menu menu = new Menu(req.getName(), req.getPrice(), menuGroup.getId(), menuProducts);

        publisher.publishEvent(new ValidatorProductBeing(productIds));
        publisher.publishEvent(new ValidatorProductPrice(productIds, menu.getPrice()));

        return menuRepository.save(menu);
    }

    private List<Long> getRequestProductIds(final MenuCreateRequest req) {
        return req.getMenuProducts()
                .stream()
                .map(MenuProductCreateRequest::getProductId)
                .collect(Collectors.toList());
    }

    private MenuGroup findMenuGroup(final Long id) {
        return menuGroupRepository.findById(id)
                .orElseThrow(MenuGroupNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<Menu> list() {
        return menuRepository.findAll();
    }
}
