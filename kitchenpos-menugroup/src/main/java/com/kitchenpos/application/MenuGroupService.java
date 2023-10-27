package com.kitchenpos.application;

import com.kitchenpos.application.dto.MenuGroupCreateRequest;
import com.kitchenpos.domain.MenuGroup;
import com.kitchenpos.domain.MenuGroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MenuGroupService {

    private final MenuGroupRepository menuGroupRepository;

    public MenuGroupService(final MenuGroupRepository menuGroupRepository) {
        this.menuGroupRepository = menuGroupRepository;
    }

    @Transactional
    public MenuGroup create(final MenuGroupCreateRequest req) {
        MenuGroup menuGroup = new MenuGroup(null, req.getName());

        return menuGroupRepository.save(menuGroup);
    }

    @Transactional(readOnly = true)
    public List<MenuGroup> list() {
        return menuGroupRepository.findAll();
    }
}
