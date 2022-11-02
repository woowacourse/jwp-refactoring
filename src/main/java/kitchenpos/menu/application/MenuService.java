package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.product.event.VerifiedMenuProductsEvent;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.dto.application.MenuProductDto;
import kitchenpos.menu.dto.request.CreateMenuRequest;
import kitchenpos.menu.dto.response.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher publisher;

    public MenuService(MenuRepository menuRepository, MenuGroupRepository menuGroupRepository,
        ApplicationEventPublisher publisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.publisher = publisher;
    }

    @Transactional
    public MenuResponse create(final CreateMenuRequest request) {
        validateMenuGroupById(request.getMenuGroupId());
        publisher.publishEvent(new VerifiedMenuProductsEvent(request.getPrice(), request.getMenuProducts()));

        List<MenuProductDto> menuProducts = request.getMenuProducts().stream()
            .map(it -> new MenuProductDto(it.getProductId(), it.getQuantity()))
            .collect(Collectors.toList());

        final Menu menu = menuRepository.save(new Menu(
            request.getName(),
            request.getPrice(),
            request.getMenuGroupId(),
            menuProducts
        ));

        return new MenuResponse(menu);
    }

    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
            .map(MenuResponse::new)
            .collect(Collectors.toList());
    }

    private void validateMenuGroupById(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 메뉴그룹입니다."));
    }

}
