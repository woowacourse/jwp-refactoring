package kitchenpos.menu.application;

import kitchenpos.product.domain.ProductEventDto;
import kitchenpos.menu.application.dto.MenuCreateRequest;
import kitchenpos.menu.application.dto.MenuCreateRequest.MenuProductRequest;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProductRepository;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.product.domain.Price;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class MenuMapper {

    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductRepository menuProductRepository;
    private final ApplicationEventPublisher publisher;

    public MenuMapper(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final MenuProductRepository menuProductRepository,
            final ApplicationEventPublisher publisher) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductRepository = menuProductRepository;
        this.publisher = publisher;
    }

    public Menu toDomain(MenuCreateRequest request) {
        final MenuGroup menuGroup = menuGroupRepository.getById(request.getMenuGroupId());
        final Menu menu = menuRepository.save(new Menu(request.getName(), Price.of(request.getPrice()), menuGroup));

        for (MenuProductRequest menuProduct : request.getMenuProducts()) {
            ProductEventDto productEventDto = new ProductEventDto();
            productEventDto.setId(menuProduct.getProductId());

            publisher.publishEvent(productEventDto);

            menuProductRepository.save(
                    new MenuProduct(
                            menu,
                            productEventDto.getName(),
                            Price.of(productEventDto.getPrice()),
                            menuProduct.getQuantity()
                    )
            );
        }
        return menu;
    }
}
