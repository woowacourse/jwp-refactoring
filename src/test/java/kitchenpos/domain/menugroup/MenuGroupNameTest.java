package kitchenpos.domain.menugroup;

import kitchenpos.DomainTest;
import kitchenpos.menugroup.domain.MenuGroupName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DomainTest
class MenuGroupNameTest {
    @ParameterizedTest
    @ValueSource(ints = {0, 1, 255})
    void 메뉴_그룹_이름은_255자_이하이다(int length) {
        final MenuGroupName menuGroupName = new MenuGroupName("메".repeat(length));
        assertThat(menuGroupName.getName()).isEqualTo("메".repeat(length));
    }

    @Test
    void 메뉴_그룹_이름이_256자_이상이면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuGroupName("메".repeat(256)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 메뉴_그룹_이름이_없으면_예외가_발생한다() {
        assertThatThrownBy(() -> new MenuGroupName(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
