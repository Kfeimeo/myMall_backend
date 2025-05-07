package org.mynet.shoppingsite.service;

import org.mynet.shoppingsite.model.LoginLogoutRecord;
import org.mynet.shoppingsite.repository.LoginLogoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginLogoutService {

    private final LoginLogoutRepository repository;

    @Autowired
    public LoginLogoutService(LoginLogoutRepository repository) {
        this.repository = repository;
    }
    // 分页查询所有记录
    public List<LoginLogoutRecord> findAll() {
        return repository.findAll();
    }

    // 分页按用户ID查询
    public List<LoginLogoutRecord> findByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    // 分页按时间范围查询
    public List<LoginLogoutRecord> findByLoginTimeBetween(LocalDateTime start, LocalDateTime end ) {
        // 添加参数校验
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time must not be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        return repository.findByLoginTimeBetween(start, end);
    }

    // 分页按用户ID和时间范围查询
    public List<LoginLogoutRecord> findByUserIdAndLoginTimeBetween(
            Integer userId,
            LocalDateTime start,
            LocalDateTime end
            ) {
        // 添加参数校验
        if (userId == null) {
            throw new IllegalArgumentException("User ID must not be null");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time must not be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before end time");
        }
        return repository.findByUserIdAndLoginTimeBetween(userId, start, end);
    }

    // 获取会话统计信息
    public Map<String, Object> getSessionStatistics(LocalDateTime startTime, LocalDateTime endTime) {
        Map<String, Object> stats = new HashMap<>();

        List<LoginLogoutRecord> records;
        if (startTime != null && endTime != null) {
            records = repository.findByLoginTimeBetween(startTime, endTime);
        } else {
            records = repository.findAll();
        }

        // 计算平均会话时长
        double avgDuration = records.stream()
                .mapToLong(r -> java.time.Duration.between(r.getLoginTime(), r.getLogoutTime()).getSeconds())
                .average()
                .orElse(0);

        // 计算最长会话
        long maxDuration = records.stream()
                .mapToLong(r -> java.time.Duration.between(r.getLoginTime(), r.getLogoutTime()).getSeconds())
                .max()
                .orElse(0);

        // 计算活跃用户数
        long uniqueUsers = records.stream()
                .map(LoginLogoutRecord::getUserId)
                .distinct()
                .count();

        stats.put("totalRecords", records.size());
        stats.put("uniqueUsers", uniqueUsers);
        stats.put("avgSessionDuration", formatDuration(avgDuration));
        stats.put("maxSessionDuration", formatDuration(maxDuration));

        return stats;
    }

    private String formatDuration(double seconds) {
        long hours = (long) (seconds / 3600);
        long minutes = (long) ((seconds % 3600) / 60);
        return String.format("%dh %dm", hours, minutes);
    }
    // 保存登录登出记录
    public LoginLogoutRecord saveRecord(LoginLogoutRecord record) {
        return repository.save(record);
    }



    // 获取所有长时间会话记录(超过1小时)
    public List<LoginLogoutRecord> getLongSessions() {
        return repository.findLongSessions(3600L); // 3600秒=1小时
    }
}