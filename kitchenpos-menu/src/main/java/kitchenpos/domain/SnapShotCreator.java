package kitchenpos.domain;

import java.util.List;

public interface SnapShotCreator {

    List<Menu> findAllByIds(final List<Long> menuIds);
}
