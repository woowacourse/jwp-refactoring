package kitchenpos.application;

import static kitchenpos.fixtures.domain.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixtures.domain.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import kitchenpos.repository.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {
        @DisplayName("메뉴 그룹을 생성한다.")
        @Test
        void Should_CreateMenuGroup() {
            // given
            MenuGroup menuGroup = createMenuGroupRequest();

            // when
            MenuGroup actual = menuGroupService.create(menuGroup);

            // then
            assertAll(() -> {
                assertThat(actual.getId()).isNotNull();
                assertThat(actual.getName()).isEqualTo(menuGroup.getName());
            });
        }
    }

    @DisplayName("list 메소드는")
    @Nested
    class ListMethod {
        @DisplayName("생성된 메뉴 그룹 목록을 조회한다.")
        @Test
        void Should_ReturnMenuGroupList() {
            // given
            MenuGroup menuGroup1 = createMenuGroup("분식");
            MenuGroup menuGroup2 = createMenuGroup("한식");
            MenuGroup menuGroup3 = createMenuGroup("중식");

            menuGroupRepository.save(menuGroup1);
            menuGroupRepository.save(menuGroup2);
            menuGroupRepository.save(menuGroup3);

            // when
            List<MenuGroup> actual = menuGroupService.list();

            // then
            assertThat(actual).hasSize(3);
        }
    }
}
