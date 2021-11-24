package kitchenpos.menugroup.domain;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Component
public class RegistrationInGroupEventHandler {

    private final MenuGroupRepository menuGroupRepository;

    public RegistrationInGroupEventHandler(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Async
    @EventListener
    public void handle(final RegistrationInGroupEvent event) {
        validate(event);
    }

    public void validate(final RegistrationInGroupEvent event) {
        Long menuGroupId = event.getMenuGroupId();
        if (!menuGroupRepository.existsById(menuGroupId)) {
            throw new IllegalArgumentException(
                String.format("존재하지 않는 메뉴 그룹입니다. (id: %d)", menuGroupId)
            );
        }
    }
}
