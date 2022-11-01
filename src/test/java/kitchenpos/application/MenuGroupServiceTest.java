package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import javax.transaction.Transactional;
import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.request.MenuGroupCreateRequest;
import kitchenpos.fixtures.MenuGroupFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Test
    @DisplayName("메뉴 그룹을 생성한다")
    void create() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final MenuGroupCreateRequest menuGroupCreateRequest = new MenuGroupCreateRequest(menuGroup.getName());

        // when
        final MenuGroup actual = menuGroupService.create(menuGroupCreateRequest);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo("두마리 치킨 메뉴")
        );
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회한다")
    void list() {
        // given
        final MenuGroup menuGroup = MenuGroupFixtures.TWO_CHICKEN_GROUP.create();
        final Long savedId = menuGroupRepository.save(menuGroup)
                .getId();

        // when
        final List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(menuGroups).extracting("id")
                        .contains(savedId),
                () -> assertThat(menuGroups).hasSizeGreaterThanOrEqualTo(1)
        );
    }
}
