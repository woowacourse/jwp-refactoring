package kitchenpos.application;

import static kitchenpos.support.MenuFixture.메뉴_생성;
import static kitchenpos.support.MenuGroupFixture.메뉴_그룹;
import static kitchenpos.support.OrderFixture.주문_생성;
import static kitchenpos.support.OrderTableFixture.비어있는_주문_테이블;
import static kitchenpos.support.OrderTableFixture.비어있지_않은_주문_테이블;
import static kitchenpos.support.OrderTableFixture.주문_테이블_생성;
import static kitchenpos.support.ProductFixture.상품;
import static kitchenpos.support.TableGroupFixture.테이블_그룹_구성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kitchenpos.menu.application.dto.MenuResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.application.dto.OrderTableRequestDto;
import kitchenpos.table.application.dto.OrderTableResponse;
import kitchenpos.table.application.dto.UpdateEmptyRequestDto;
import kitchenpos.table.application.dto.UpdateNumberOfGuestRequestDto;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;


class TableServiceTest extends ServiceTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("테이블을 생성한다.")
    void create() {
        final OrderTableRequestDto orderTableRequestDto = 주문_테이블_생성(null, 2, true);

        final OrderTableResponse actual = tableService.create(orderTableRequestDto);

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

        final List<OrderTableResponse> actual = tableService.list();

        assertThat(actual).hasSize(3);
    }

    @Nested
    @DisplayName("테이블 상태 변환 테스트")
    class changeEmpty {

        @Test
        @DisplayName("테이블 상태를 empty를 false로 바꿔준다.")
        void changeEmpty() {
            final OrderTableResponse savedTable = 주문_테이블_등록(비어있는_주문_테이블);

            final UpdateEmptyRequestDto orderTableUpdateEmptyRequestDto =
                    new UpdateEmptyRequestDto(false);
            final OrderTableResponse actual = tableService.changeEmpty(savedTable.getId(), orderTableUpdateEmptyRequestDto);

            assertThat(actual.isEmpty()).isFalse();
        }

        @Test
        @DisplayName("등록되지 않은 테이블의 상태를 반환하려 시도하면 예외를 발생시킨다.")
        void changeEmpty_notExistTable() {
            final Long notExistOrderTableId = 0L;
            final UpdateEmptyRequestDto orderTableUpdateEmptyRequestDto =
                    new UpdateEmptyRequestDto(false);
            assertThatThrownBy(() -> tableService.changeEmpty(notExistOrderTableId, orderTableUpdateEmptyRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("그룹테이블이 지정되어있으면 예외를 발생시킨다.")
        void changeEmpty_existTableGroupId() {
            final OrderTableResponse savedOrderTable1 = 주문_테이블_등록(비어있는_주문_테이블);
            final OrderTableResponse savedOrderTable2 = 주문_테이블_등록(비어있는_주문_테이블);
            테이블_그룹_등록(테이블_그룹_구성(savedOrderTable1, savedOrderTable2));

            final UpdateEmptyRequestDto orderTableUpdateEmptyRequestDto =
                    new UpdateEmptyRequestDto(false);
            assertThatThrownBy(() -> tableService.changeEmpty(savedOrderTable1.getId(), orderTableUpdateEmptyRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {"COOKING", "MEAL"})
        @DisplayName("테이블 상태가 조리중이거나 식사중이면 예외를 발생시킨다.")
        void changeEmpty_cookingOrMeal(final String orderStatus) {

            final OrderTableResponse savedTable = 주문_테이블_등록(비어있지_않은_주문_테이블);
            orderRepository.save(new Order(savedTable.getId(), orderStatus, LocalDateTime.now(), null));


            final UpdateEmptyRequestDto orderTableUpdateEmptyRequestDto = new UpdateEmptyRequestDto(false);
            assertThatThrownBy(() -> tableService.changeEmpty(savedTable.getId(), orderTableUpdateEmptyRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    @DisplayName("손님 수 변경 테스트.")
    class changeNumberOfGuests {

        @Test
        @DisplayName("테이블 손님 수를 변경한다.")
        void changeNumberOfGuests() {
            final OrderTableResponse savedTable = 주문_테이블_등록(비어있지_않은_주문_테이블);

            final UpdateNumberOfGuestRequestDto updateNumberOfGuestRequestDto = new UpdateNumberOfGuestRequestDto(3);
            final OrderTableResponse actual = tableService.changeNumberOfGuests(savedTable.getId(), updateNumberOfGuestRequestDto);

            assertThat(actual.getNumberOfGuests()).isEqualTo(3);
        }

        @Test
        @DisplayName("변경하려는 테이블 손님 수가 음수인 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_negativeGuest() {
            final OrderTableResponse savedTable = 주문_테이블_등록(비어있지_않은_주문_테이블);

            final UpdateNumberOfGuestRequestDto updateNumberOfGuestRequestDto = new UpdateNumberOfGuestRequestDto(-3);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedTable.getId(), updateNumberOfGuestRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("존재하지 않는 테이블의 손님 수를 변경하려 하는 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_notExistOrderTable() {
            final Long notExistTableId = 0L;

            final UpdateNumberOfGuestRequestDto updateNumberOfGuestRequestDto = new UpdateNumberOfGuestRequestDto(4);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(notExistTableId, updateNumberOfGuestRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("테이블이 비어있는데 손님 수를 변경하려는 경우 예외를 발생시킨다.")
        void changeNumberOfGuests_EmptyTable() {
            final OrderTableRequestDto orderTableRequest = 비어있는_주문_테이블;
            final OrderTableResponse savedOrderTable = 주문_테이블_등록(orderTableRequest);

            final UpdateNumberOfGuestRequestDto updateNumberOfGuestRequestDto = new UpdateNumberOfGuestRequestDto(4);
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(savedOrderTable.getId(), updateNumberOfGuestRequestDto))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
