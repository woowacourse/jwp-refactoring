package acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.List;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuGroupAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void getMenuGroups() {
        // given
        long menuGroupId1 = 메뉴_그룹_생성("추천 메뉴");
        long menuGroupId2 = 메뉴_그룹_생성("호호 메뉴");
        long menuGroupId3 = 메뉴_그룹_생성("베루스 메뉴");
        long menuGroupId4 = 메뉴_그룹_생성("라라 메뉴");

        // when
        List<MenuGroup> menuGroups = 메뉴_그룹_조회();

        // then
        assertThat(menuGroups)
                .extracting(MenuGroup::getId, MenuGroup::getName)
                .containsExactlyInAnyOrder(
                        tuple(menuGroupId1, "추천 메뉴"),
                        tuple(menuGroupId2, "호호 메뉴"),
                        tuple(menuGroupId3, "베루스 메뉴"),
                        tuple(menuGroupId4, "라라 메뉴")
                );
    }
}
