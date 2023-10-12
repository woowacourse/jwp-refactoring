package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static kitchenpos.fixtures.domain.MenuGroupFixture.createMenuGroup;
import static kitchenpos.fixtures.domain.MenuGroupFixture.createMenuGroupRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("create 메소드는")
    @Nested
    class CreateMethod {

        @DisplayName("메뉴 그룹을 생성한다.")
        @Test
        void Should_CreateMenuGroup() {
            // given
            final MenuGroup menuGroup = createMenuGroupRequest();

            // when
            final MenuGroup actual = menuGroupService.create(menuGroup);

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
            final MenuGroup menuGroup1 = createMenuGroup("분식");
            final MenuGroup menuGroup2 = createMenuGroup("한식");
            final MenuGroup menuGroup3 = createMenuGroup("중식");

            menuGroupDao.save(menuGroup1);
            menuGroupDao.save(menuGroup2);
            menuGroupDao.save(menuGroup3);

            // when
            final List<MenuGroup> actual = menuGroupService.list();

            // then
            assertThat(actual).hasSize(3);}
    }
}
