package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kitchenpos.domain.MenuGroup;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Nested
    class CreateTest {

        @DisplayName("메뉴 그룹을 생성하면 ID가 할당된 MenuGroup객체가 반환된다")
        @Test
        void create() {
            MenuGroup menuGroup = new MenuGroup("치킨 + 콜라 세트");

            MenuGroup actual = menuGroupService.create(menuGroup);
            assertThat(actual).isNotNull();
        }
    }

    @Nested
    class ListTest {

        @DisplayName("존재하는 모든 메뉴 그룹 목록을 조회한다")
        @Test
        void list() {
            List<MenuGroup> actual = menuGroupService.list();
            assertThat(actual).hasSize(4);
        }
    }
}
