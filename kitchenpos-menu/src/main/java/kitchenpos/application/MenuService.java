package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuResponse;
import kitchenpos.repository.MenuGroupRepository;
import kitchenpos.repository.MenuRepository;
import kitchenpos.event.ProductQuantityEventDto;
import kitchenpos.event.ValidateExistProductEvent;
import kitchenpos.event.ValidateSamePriceWithMenuEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ApplicationEventPublisher publisher;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ApplicationEventPublisher publisher
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.publisher = publisher;
    }

    public MenuResponse create(final MenuCreateRequest request) {
        final MenuGroup findMenuGroup = findMenuGroup(request.getMenuGroupId());

        final List<MenuProduct> convertMenuProducts = request.getMenuProducts().stream()
                .map(this::convertMenuProduct)
                .collect(Collectors.toList());

        final List<ProductQuantityEventDto> productQuantityEventDtos = convertMenuProducts.stream()
                .map(mp -> new ProductQuantityEventDto(mp.getProductId(), mp.getQuantity()))
                .collect(Collectors.toList());
        publisher.publishEvent(new ValidateSamePriceWithMenuEvent(request.getPrice(), productQuantityEventDtos));

        final Menu menu = new Menu(request.getName(), request.getPrice(), findMenuGroup, convertMenuProducts);
        menuRepository.save(menu);

        return MenuResponse.from(menu);
    }

    private MenuGroup findMenuGroup(final Long menuGroupId) {
        return menuGroupRepository.findById(menuGroupId)
                .orElseThrow(() -> new IllegalArgumentException("[ERROR] 존재하지 않는 메뉴 그룹입니다."));
    }

    private MenuProduct convertMenuProduct(final MenuProductRequest request) {
        final Long productId = request.getProductId();
        publisher.publishEvent(new ValidateExistProductEvent(productId));
        return new MenuProduct(productId, request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
