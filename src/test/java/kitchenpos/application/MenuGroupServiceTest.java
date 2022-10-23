package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@DisplayName("메뉴 그룹 서비스 테스트")
@SuppressWarnings("NonAsciiCharacters")
@Transactional
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    void 메뉴_그룹_정상_생성() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("양식");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        boolean actual = menuGroupDao.existsById(savedMenuGroup.getId());
        assertThat(actual).isTrue();
    }

    @Test
    void 메뉴_그룹_이름을_null_값으로_생성() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(null);

        // when & then
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
                .isInstanceOf(Exception.class);
    }
}
