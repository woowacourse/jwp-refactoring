package kitchenpos.application;

import static kitchenpos.support.fixture.domain.MenuGroupFixture.KOREAN;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.NestedApplicationTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.application.dto.MenuGroupResponse;
import kitchenpos.menu.domain.dao.JdbcTemplateMenuGroupDao;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.support.fixture.dto.MenuGroupDtoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private JdbcTemplateMenuGroupDao jdbcTemplateMenuGroupDao;

    @NestedApplicationTest
    @DisplayName("create 메서드는")
    class Create {

        @Test
        @DisplayName("메뉴 그룹을 생성한다.")
        void success() {
            MenuGroup menuGroup = KOREAN.getMenuGroup();

            MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroup.getName());

            assertThat(menuGroupResponse).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(MenuGroupDtoFixture.메뉴_그룹_생성_응답(menuGroup));
        }
    }

    @NestedApplicationTest
    @DisplayName("list 메서드는")
    class ListTest {

        @BeforeEach
        void setUp() {
            jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
            jdbcTemplateMenuGroupDao.save(KOREAN.getMenuGroup());
        }

        @Test
        @DisplayName("메뉴 그룹 전체 목록을 조회한다.")
        void success() {
            List<MenuGroupResponse> responses = menuGroupService.list();

            assertThat(responses).hasSize(2);
        }
    }
}
