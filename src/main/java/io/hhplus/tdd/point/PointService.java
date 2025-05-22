package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.springframework.stereotype.Service;

@Service
public class PointService {
    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    public UserPoint chargePoint(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint updatedUserPoint = userPoint.charge(amount);
        return userPointTable.insertOrUpdate(id, updatedUserPoint.point());
    }

    public UserPoint usePoint(long id, long amount) {
        UserPoint userPoint = userPointTable.selectById(id);
        UserPoint updatedUserPoint = userPoint.use(amount);
        return userPointTable.insertOrUpdate(id, updatedUserPoint.point());
    }

    public UserPoint selectPoint(long id) {
        return userPointTable.selectById(id);
    }
}
