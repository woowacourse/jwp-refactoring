package kitchenpos.application;

import static kitchenpos.fixture.MenuFixture.메뉴;
import static kitchenpos.fixture.MenuGroupFixture.메뉴그룹_두마리메뉴;
import static kitchenpos.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.fixture.OrderFixture.주문;
import static kitchenpos.fixture.OrderLineItemFixture.주문상품;
import static kitchenpos.fixture.OrderTableFixture.비지않은_테이블;
import static kitchenpos.fixture.OrderTableFixture.빈테이블;
import static kitchenpos.fixture.ProductFixture.후라이드_16000;
import static kitchenpos.fixture.TableGroupFixture.테이블그룹;
import static kitchenpos.fixture.TableGroupFixture.테이블그룹_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.EnumSource.Mode;

@SuppressWarnings("NonAsciiCharacters")
class TableGroupServiceTest extends ServiceTest {

    @Nested
    class 테이블_그룹_생성 {

        @Test
        void 테이블_그룹을_생성할_수_있다() {
            // given
            final var 테이블1 = orderTableDao.save(빈테이블());
            final var 테이블2 = orderTableDao.save(빈테이블());
            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId(), 테이블2.getId()));

            // when
            final var response = tableGroupService.create(request);

            // then
            assertThat(tableGroupDao.findById(response.getId())).isPresent();
        }

        @Test
        void 묶으려는_테이블이_2개미만이면_생성할_수_없다() {
            // given
            final var 테이블1 = orderTableDao.save(빈테이블());

            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 존재하지_않는_테이블이면_생성할_수_없다() {
            // given
            final var wrongTableId = 999L;
            final var request = 테이블그룹_생성_요청(List.of(wrongTableId));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 빈테이블이_아니면_생성할_수_없다() {
            // given
            final var 테이블1 = orderTableDao.save(비지않은_테이블());
            final var 테이블2 = orderTableDao.save(빈테이블());

            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId(), 테이블2.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 이미_테이블_그룹을_가진_테이블이면_생성할_수_없다() {
            // given
            final var 테이블1 = orderTableDao.save(빈테이블());
            final var 테이블2 = orderTableDao.save(빈테이블());
            final var 테이블3 = orderTableDao.save(빈테이블());

            final var 테이블그룹1 = tableGroupDao.save(테이블그룹(List.of(테이블1, 테이블2)));
            테이블1.setTableGroupId(테이블그룹1.getId());
            테이블1.setEmpty(false);
            orderTableDao.save(테이블1);
            테이블2.setTableGroupId(테이블그룹1.getId());
            테이블2.setEmpty(false);
            orderTableDao.save(테이블2);

            final var request = 테이블그룹_생성_요청(List.of(테이블1.getId(), 테이블3.getId()));

            // when & then
            assertThatThrownBy(() -> tableGroupService.create(request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class 테이블_그룹_삭제 {

        @Test
        void 테이블_그룹을_삭제할_수_있다() {
            // given
            final var 테이블1 = orderTableDao.save(빈테이블());
            final var 테이블2 = orderTableDao.save(빈테이블());

            final var 테이블그룹 = tableGroupDao.save(테이블그룹(List.of(테이블1, 테이블2)));
            테이블1.setTableGroupId(테이블그룹.getId());
            테이블1.setEmpty(false);
            orderTableDao.save(테이블1);
            테이블2.setTableGroupId(테이블그룹.getId());
            테이블2.setEmpty(false);
            orderTableDao.save(테이블2);

            // when
            tableGroupService.ungroup(테이블그룹.getId());

            // then
            assertThat(orderTableDao.findAllByTableGroupId(테이블그룹.getId())).isEmpty();
        }

        @ParameterizedTest
        @EnumSource(mode = Mode.EXCLUDE, names = "COMPLETION")
        void 주문상태가_COMPLETION이_아니면_삭제할_수_없다(OrderStatus orderStatus) {
            // given
            final var 두마리메뉴 = menuGroupRepository.save(메뉴그룹_두마리메뉴);

            final var 후라이드 = productRepository.save(후라이드_16000);

            final var 후라이드메뉴 = 메뉴("싼후라이드", 10000, 두마리메뉴);
            후라이드메뉴.addMenuProducts(List.of(메뉴상품(후라이드, 1)));
            menuRepository.save(후라이드메뉴);

            final var 테이블1 = orderTableDao.save(빈테이블());
            final var 테이블2 = orderTableDao.save(빈테이블());

            final var 테이블그룹 = tableGroupDao.save(테이블그룹(List.of(테이블1, 테이블2)));
            테이블1.setTableGroupId(테이블그룹.getId());
            테이블1.setEmpty(false);
            orderTableDao.save(테이블1);
            테이블2.setTableGroupId(테이블그룹.getId());
            테이블2.setEmpty(false);
            orderTableDao.save(테이블2);

            final var order = orderDao.save(주문(테이블1.getId(), orderStatus.name()));
            final var 주문상품 = orderLineItemDao.save(주문상품(order.getId(), 후라이드메뉴.getId(), 1));
            order.setOrderLineItems(List.of(주문상품));

            // when & then
            assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹.getId()))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
