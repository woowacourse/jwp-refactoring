package kitchenpos.application;

import kitchenpos.application.fixture.TableGroupServiceFixture;
import kitchenpos.domain.TableGroup;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TableGroupServiceTest extends TableGroupServiceFixture {

    @Autowired
    private TableGroupService tableGroupService;

    @Nested
    class 단체_테이블_등록 {

        @Test
        void 단체_테이블을_등록할_수_있다() {
            단체_테이블을_등록할_수_있다_픽스처_생성();

            final TableGroup actual = tableGroupService.create(생성할_테이블그룹_요청_dto);

            assertThat(actual).isEqualTo(생성한_테이블그룹);
        }

        @Test
        void 주문_테이블_아이디가_입력되지_않은_경우_예외가_발생한다() {
            주문_테이블_아이디가_입력되지_않은_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableGroupService.create(주문_테이블이_없는_테이블그룹_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블_아이디가_1개인_경우_예외가_발생한다() {
            주문_테이블_아이디가_1개인_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableGroupService.create(주문_테이블이_없는_테이블그룹_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 주문_테이블이_사용가능한_테이블인_경우_예외가_발생한다() {
            주문_테이블이_사용가능한_테이블인_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableGroupService.create(사용가능한_테이블을_포함한_테이블그룹_요청_dto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 단체_테이블_삭제 {

        @Test
        void 단체_테이블을_삭제할_수_있다() {
            단체_테이블을_삭제할_수_있다_픽스처_생성();

            tableGroupService.ungroup(삭제할_테이블_그룹.getId());

            SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(삭제할_주문테이블에_포함된_주문_테이블_리스트.get(0).getTableGroup()).isNull();
                softAssertions.assertThat(삭제할_주문테이블에_포함된_주문_테이블_리스트.get(0).isEmpty()).isFalse();
                softAssertions.assertThat(삭제할_주문테이블에_포함된_주문_테이블_리스트.get(1).getTableGroup()).isNull();
                softAssertions.assertThat(삭제할_주문테이블에_포함된_주문_테이블_리스트.get(1).isEmpty()).isFalse();
            });
        }

        @Test
        void 단체_테이블에_포함된_주문_테이블_중_주문_상태가_조리_또는_식사인_경우_예외가_발생한다() {
            단체_테이블에_포함된_주문_테이블_중_주문_상태가_조리_또는_식사인_경우_예외가_발생한다_픽스처_생성();

            assertThatThrownBy(() -> tableGroupService.ungroup(식사중인_단체_테이블.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
