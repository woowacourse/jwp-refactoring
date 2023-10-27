package kitchenpos.menu.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.dto.ProductQuantityDto;
import kitchenpos.product.dto.ValidateExistProductDto;
import kitchenpos.product.dto.ValidateSamePriceWithMenuDto;
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

        final List<ProductQuantityDto> productQuantityDtos = convertMenuProducts.stream()
                .map(mp -> new ProductQuantityDto(mp.getProductId(), mp.getQuantity()))
                .collect(Collectors.toList());
        publisher.publishEvent(new ValidateSamePriceWithMenuDto(request.getPrice(), productQuantityDtos));

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
        publisher.publishEvent(new ValidateExistProductDto(productId));
        return new MenuProduct(productId, request.getQuantity());
    }

    @Transactional(readOnly = true)
    public List<MenuResponse> list() {
        return menuRepository.findAll().stream()
                .map(MenuResponse::from)
                .collect(Collectors.toList());
    }
}
