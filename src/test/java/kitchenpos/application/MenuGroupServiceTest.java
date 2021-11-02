package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

@DisplayName("MenuGroup Service 테스트")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("MenuGroup을 저장할 때")
    @Nested
    class Save {

        @DisplayName("정상적인 MenuGroup은 저장에 성공한다.")
        @Test
        void success() {
            // given
            MenuGroup menuGroup = MenuGroup을_생성한다("인기 메뉴");

            // when
            MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

            // then
            assertThat(menuGroupDao.findById(savedMenuGroup.getId())).isPresent();
            assertThat(savedMenuGroup).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(savedMenuGroup);
        }

        @DisplayName("name이 Null인 경우 예외가 발생한다.")
        @Test
        void nameNullException() {
            // given
            MenuGroup menuGroup = MenuGroup을_생성한다(null);

            // when, then
            assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isExactlyInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @DisplayName("모든 MenuGroup을 조회한다.")
    @Test
    void findAll() {
        // given
        List<MenuGroup> beforeSavedMenuGroups = menuGroupDao.findAll();

        beforeSavedMenuGroups.add(menuGroupDao.save(MenuGroup을_생성한다("인기 메뉴")));
        beforeSavedMenuGroups.add(menuGroupDao.save(MenuGroup을_생성한다("추천 메뉴")));
        beforeSavedMenuGroups.add(menuGroupDao.save(MenuGroup을_생성한다("주는대로 먹어")));

        // when
        List<MenuGroup> afterSavedMenuGroups = menuGroupService.list();

        // then
        assertThat(afterSavedMenuGroups).hasSize(beforeSavedMenuGroups.size());
        assertThat(afterSavedMenuGroups).usingRecursiveComparison()
            .isEqualTo(beforeSavedMenuGroups);
    }

    private MenuGroup MenuGroup을_생성한다(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        return menuGroup;
    }
}