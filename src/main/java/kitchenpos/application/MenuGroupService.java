package kitchenpos.application;

import kitchenpos.dao.JpaMenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {
    private final JpaMenuGroupRepository jpaMenuGroupRepository;

    public MenuGroupService(final JpaMenuGroupRepository jpaMenuGroupRepository) {
        this.jpaMenuGroupRepository = jpaMenuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroup menuGroup) {
        return jpaMenuGroupRepository.save(menuGroup);
    }

    public List<MenuGroup> list() {
        return jpaMenuGroupRepository.findAll();
    }
}
