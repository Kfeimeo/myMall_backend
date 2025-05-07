package org.mynet.shoppingsite.repository;

import org.mynet.shoppingsite.model.LoginLogoutRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginLogoutRepository extends JpaRepository<LoginLogoutRecord, Long> {

    //找出所有
    List<LoginLogoutRecord> findAll();
    // 根据IP地址查询
    List<LoginLogoutRecord> findByIpAddress(String ipAddress);

    // 分页按用户ID查询
    List<LoginLogoutRecord> findByUserId(Integer userId);

    // 分页按时间范围查询
    List<LoginLogoutRecord> findByLoginTimeBetween(LocalDateTime start, LocalDateTime end);

    // 分页按用户ID和时间范围查询
    List<LoginLogoutRecord> findByUserIdAndLoginTimeBetween(Integer userId, LocalDateTime start, LocalDateTime end);

    @Query(
            value = """
        SELECT *
          FROM logging r
         WHERE TIMESTAMPDIFF(SECOND, r.login_time, r.logout_time) > :duration
      """,
            nativeQuery = true
    )
    List<LoginLogoutRecord> findLongSessions(@Param("duration") long duration);
}