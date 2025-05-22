package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.*;

class PointServiceTest {
    @Test
    @DisplayName("포인트 충전 시 DB조회 및 저장 호출을 검증한다")
    void chargePoint_VerifyProcess() {
        //given
        long id = 1L;
        long amount = 200L;

        // DB Mock
        UserPointTable mockRepository = mock(UserPointTable.class);
        PointHistoryTable mockHistRepository = mock(PointHistoryTable.class);

        // Domain Mock
        // 조회된 객체 생성
        UserPoint mockUserPoint = mock(UserPoint.class);
        // 포인트 충전 후 반환될 객체
        UserPoint mockUserPointAfter = mock(UserPoint.class);

        // service 설정
        PointService pointService = new PointService(mockRepository,mockHistRepository);

        // selectById 호출 -> mockUserPoint 반환
        when(mockRepository.selectById(id)).thenReturn(mockUserPoint);

        // charge 호출 -> mockUserPointAfter 반환
        when(mockUserPoint.charge(amount)).thenReturn(mockUserPointAfter);

        // insertOrUpdate 호출 -> mockUserPointAfter 반환
        when(mockRepository.insertOrUpdate(id, mockUserPointAfter.point()))
                .thenReturn(mockUserPointAfter);

        //when
        UserPoint result = pointService.chargePoint(id, amount);

        //then : 흐름 검증 (흐름 검증이라 값에 대한 검증은 하지 않음)
        // selectById 가 호출 됐는지
        verify(mockRepository).selectById(id);

        // use 가 호출 됐는지
        verify(mockUserPoint).charge(amount);

        // insertOrUpdate 가 호출 됐는지
        verify(mockRepository).insertOrUpdate(id, mockUserPointAfter.point());
    }

    @Test
    @DisplayName("포인트 충전값이 0보다 작을 때 예외 처리를 검증한다.")
    void chargePoint_VerifyException() {
        //given
        long id = 1L;
        long amount = -100L;

        //Mock 생성
        UserPoint mockUserPoint = mock(UserPoint.class);

        UserPointTable mockRepository = mock(UserPointTable.class);
        PointHistoryTable mockHistRepository = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockRepository,mockHistRepository);

        //selectById 호출 -> mockUserPoint 반환
        when(mockRepository.selectById(id)).thenReturn(mockUserPoint);

        //charge 호출 -> 예외 반환
        when(mockUserPoint.charge(amount)).thenThrow(new IllegalArgumentException("0이상 충전 가능"));

        //when

        //then
        assertThatThrownBy(() -> pointService.chargePoint(id,amount))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("0이상 충전 가능");
    }

    @Test
    @DisplayName("포인트 사용 시 DB 조회 및 저장 호출을 검증한다.")
    void usePoint_VerifyProcess() {
        //given
        long id = 1L;
        long amount = 50L;

        // DB Mock
        UserPointTable mockRepository = mock(UserPointTable.class);
        PointHistoryTable mockHistRepository = mock(PointHistoryTable.class);

        // Domain Mock
        // 조회된 객체를 가짜로 생성
        UserPoint mockUserPoint = mock(UserPoint.class);
        // 포인트 사용 후 반환될 객체
        UserPoint mockUserPointAfter = mock(UserPoint.class);

        // service 설정
        PointService pointService = new PointService(mockRepository,mockHistRepository);

        // selectById 호출 -> mockUserPoint 반환
        when(mockRepository.selectById(id)).thenReturn(mockUserPoint);

        // use 호출 -> mockUserPointAfter 반환
        when(mockUserPoint.use(amount)).thenReturn(mockUserPointAfter);

        // insertOrUpdate 호출 -> mockUserPointAfter 반환
        when(mockRepository.insertOrUpdate(id, mockUserPointAfter.point()))
                .thenReturn(mockUserPointAfter);

        //when
        UserPoint result = pointService.usePoint(id, amount);

        //then : 흐름 검증 (흐름 검증이라 값에 대한 검증은 하지 않음)
        // selectById 가 호출 됐는지
        verify(mockRepository).selectById(id);

        // use 가 호출 됐는지
        verify(mockUserPoint).use(amount);

        // insertOrUpdate 가 호출 됐는지
        verify(mockRepository).insertOrUpdate(id, mockUserPointAfter.point());
    }

    @Test
    @DisplayName("포인트가 0이거나 사용할 값보다 작을 때 예외 처리를 검증한다.")
    void usePoint_VerifyException() {
        //given
        long id = 1L;
        long amount = 300L;

        UserPoint mockUserPoint = mock(UserPoint.class);

        UserPointTable mockRepository = mock(UserPointTable.class);
        PointHistoryTable mockHistRepository = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockRepository,mockHistRepository);

        //selectById 호출 -> mockUserPoint 반환
        when(mockRepository.selectById(id)).thenReturn(mockUserPoint);

        //use 호출 -> 예외 반환
        when(mockUserPoint.use(amount)).thenThrow(new IllegalStateException("잔액이 부족합니다."));

        //when


        //then
        assertThatThrownBy(() -> pointService.usePoint(id,amount))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("잔액이 부족합니다.");

    }

    @Test
    @DisplayName("포인트 조회 시, 유효한 ID이면 저장된 값/존재하지 않는 ID 이면 0을 조회한다.")
    void selectPointById_ReturnPoint() {
        //given
        long id = 1L;
        long amount = 200L;

        UserPointTable mockRepository = mock(UserPointTable.class);
        PointHistoryTable mockHistRepository = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockRepository,mockHistRepository);

        //유효한 id 1L 의 포인트는 200L
        when(mockRepository.selectById(id)).thenReturn(new UserPoint(id, amount, System.currentTimeMillis()));

        //존재하지 않는 id 50L의 포인트는 0
        when(mockRepository.selectById(50L)).thenReturn(UserPoint.empty(50L));

        //when
        long selectedPoint = pointService.selectPoint(id).point();
        long initPoint = pointService.selectPoint(50L).point();

        //then
        // 유효한 id
        assertThat(selectedPoint).isEqualTo(amount);
        verify(mockRepository).selectById(id);

        // 존재하지 않는 id
        assertThat(initPoint).isEqualTo(0L);
        verify(mockRepository).selectById(50L);
    }
}