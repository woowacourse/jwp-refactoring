package kitchenpos.application;

import static kitchenpos.support.MenuFixture.메뉴_생성;
import static kitchenpos.support.MenuGroupFixture.메뉴_그룹1;
import static kitchenpos.support.OrderFixture.주문_생성;
import static kitchenpos.support.OrderTableFixture.비어있는_주문_테이블;
import static kitchenpos.support.OrderTableFixture.비어있지_않은_주문_테이블;
import static kitchenpos.support.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.support.ProductFixture.상품1;
import static kitchenpos.support.TableGroupFixture.테이블_그룹_구성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        final OrderTable orderTable = 주문_테이블_생성(null, 2, true);

        final OrderTable actual = tableService.create(orderTable);

        assertAll(
                () -> assertThat(actual.isEmpty()).isTrue(),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("테이블 목록을 반환한다.")
    void list() {
        주문_테이블_등록(주문_테이블_생성(null, 2, true));
        주문_테이블_등록(주문_테이블_생성(null, 2, true));
        주문_테이블_등록(주문_테이블_생성(null, 2, true));

        final List<OrderTable> actual = tableService.list();

        assertThat(actual).hasSize(3);
    }

    @Nested
    @DisplayName("테이블 상태 변환 테스트")
    class changeEmpty {

        @Test
        @DisplayName("테이블 상태를 empty를 true로 바꿔준다.")
        void changeEmpty() {
            final OrderTable savedTable = 주문_테이블_등록(비어있는_주문_테이블);

            final OrderTable actual = tableService.changeEmpty(savedTable.getId(), 비어있는_주문_테이블);

            assertThat(actual.isEmpty()).isTrue();
        }

        @Test
        @DisplayName("등록되지 않은 테이블의 상태를 반환하려 시도하면 예외를 발생시킨다.")
        void changeEmpty_notExistTable() {
            final Long notExistOrderTableId = 0L;

            assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, 비어있는_주문_테이블))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹테이블이 지정되어있으면 예외를 발생시킨다.")
        void changeEmpty_existTableGroupId() {
            final OrderTable savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTable savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            테이블_그룹_등록(테이블_그룹_구성(savedOrderTable1, savedOrderTable2));

            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), savedOrderTable1))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("테이블 상태가 조리중이거나 식사중이면 예외를 발생시킨다.")
        void changeEmpty_cookingOrMeal(final String orderStatus) {
            final int lessThanSingleProductPrice = 9000;
            final Product savedProduct = 상품_등록(상품1);
            final MenuGroup savedMenuGroup = 메뉴_그룹_등록(메뉴_그룹1);
            final Menu savedMenu = 메뉴_등록(메뉴_생성(
                    "메뉴이름",
                    BigDecimal.valueOf(lessThanSingleProductPrice),
                    savedMenuGroup.getId(),
                    savedProduct));

            final OrderTable savedTable = 주문_테이블_등록(비어있지_않은_주문_테이블);
            주문_등록(주문_생성(savedTable.getId(), savedMenu, orderStatus));

            assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), savedTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("손님 수 변경 테스트.")
    class changeNumberOfGuests {

        @Test
        @DisplayName("테이블 손님 수를 변경한다.")
        void changeNumberOfGuests() {
            final OrderTable savedTable = 주문_테이블_등록(비어있지_않은_주문_테이블);

            final OrderTable newOrderTable = new OrderTable(null, 3, false);
            final OrderTable actual = tableService.changeNumberOfGuests(savedTable.getId(), newOrderTable);

            assertThat(actual.getNumberOfGuests()).isEqualTo(3);
        }

        @Test
        @DisplayName("변경하려는 테이블 손님 수가 음수인 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_negativeGuest() {
            final OrderTable savedTable = 주문_테이블_등록(비어있지_않은_주문_테이블);

            final OrderTable newOrderTable = new OrderTable(1L, -3, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 테이블의 손님 수를 변경하려 하는 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_notExistOrderTable() {
            final Long notExistTableId = 0L;

            final OrderTable newOrderTable = new OrderTable(null, -3, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTableId, newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블이 비어있는데 손님 수를 변경하려는 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_EmptyTable() {
            final OrderTable orderTable = new OrderTable(null, 0, true);
            final OrderTable savedOrderTable = 주문_테이블_등록(orderTable);

            final OrderTable newOrderTable = new OrderTable(null, 2, false);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), newOrderTable))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
