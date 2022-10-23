package kitchenpos.application;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹을_등록한다;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import kitchenpos.application.support.IntegrationTest;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService sut;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroup() {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("떡잎 유치원");

        final MenuGroup actual = sut.create(menuGroup);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void getMenuGroups() {
        final MenuGroup 떡잎_유치원 = 메뉴_그룹을_등록한다("떡잎 유치원");
        final MenuGroup 꽃잎_유치원 = 메뉴_그룹을_등록한다("꽃잎 유치원");
        final MenuGroup 솔잎_유치원 = 메뉴_그룹을_등록한다("솔잎 유치원");

        sut.create(떡잎_유치원);
        sut.create(꽃잎_유치원);
        sut.create(솔잎_유치원);

        assertThat(sut.list())
                .hasSize(3)
                .extracting("name")
                .containsExactly(떡잎_유치원.getName(), 꽃잎_유치원.getName(), 솔잎_유치원.getName());
    }
}
