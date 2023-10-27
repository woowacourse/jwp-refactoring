package kitchenpos.application;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.repository.MenuGroupRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.dto.MenuGroupCreateRequest;
import kitchenpos.menugroup.application.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;
    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴를 생성한다.")
    void create() {
        // Given
        MenuGroupCreateRequest createRequest = new MenuGroupCreateRequest("추천메뉴");

        // When
        Long createdMenuGroupId = menuGroupService.create(createRequest);

        // Then
        assertThat(createdMenuGroupId).isNotNull();
        assertThat(menuGroupRepository.findById(createdMenuGroupId).get().getName()).isEqualTo(createRequest.getName());
    }

    @Test
    @DisplayName("메뉴를 모두 조회한다.")
    void findAll() {
        // Given
        final MenuGroup menuGroup1 = menuGroupRepository.save(MenuGroup.from("그룹1"));
        final MenuGroup menuGroup2 = menuGroupRepository.save(MenuGroup.from("그룹2"));

        // When
        List<MenuGroupResponse> menuGroups = menuGroupService.findAll();

        // Then
        assertThat(menuGroups).isNotEmpty();
        assertThat(menuGroups).hasSize(6);
        assertThat(menuGroups.get(4)).usingRecursiveComparison().isEqualTo(menuGroup1);
        assertThat(menuGroups.get(5)).usingRecursiveComparison().isEqualTo(menuGroup2);
    }
}
