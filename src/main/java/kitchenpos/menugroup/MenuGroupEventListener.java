package kitchenpos.menugroup;

import kitchenpos.menu.event.MenuCreatedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class MenuGroupEventListener {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupEventListener(MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onMenuCreated(MenuCreatedEvent menuCreatedEvent) {
        if (!menuGroupRepository.existsById(menuCreatedEvent.getMenuGroupId())) {
            throw new IllegalArgumentException("메뉴 그룹이 존재하지 않습니다.");
        }
    }
}
