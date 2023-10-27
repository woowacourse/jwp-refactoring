package kitchenpos.menu.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menu.ProductIdAndQuantity;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.exception.MenuGroupNotFoundException;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuValidator validator;
    private final ApplicationEventPublisher publisher;
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;

    public MenuService(MenuValidator validator, ApplicationEventPublisher publisher,
            MenuRepository menuRepository,
            MenuGroupRepository menuGroupRepository) {
        this.validator = validator;
        this.publisher = publisher;
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
    }

    public MenuResponse create(MenuRequest request) {
        validator.validateMenuCreationPrice(request);
        MenuGroup menuGroup = menuGroupRepository.findById(request.getMenuGroupId())
                .orElseThrow(MenuGroupNotFoundException::new);
        Menu menu = menuRepository.save(
                Menu.of(request.getName(), request.getPrice(), menuGroup.getId(),
                        request.getMenuProductDtos().stream()
                                .map(dto -> new ProductIdAndQuantity(dto.getProductId(),
                                        dto.getQuantity())).collect(toList())));
        return MenuResponse.from(menu);
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        List<Menu> menus = menuRepository.findAll();
        return menus.stream()
                .map(MenuResponse::from)
                .collect(toList());
    }
}
