package org.mynet.shoppingsite.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "logging")
public class LoginLogoutRecord {

    // Getter 和 Setter 方法
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long id;
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "login_time", nullable = false)
    private LocalDateTime loginTime;

    @Column(name = "logout_time", nullable = false)
    private LocalDateTime logoutTime;

    @Column(name = "ip", nullable = false, length = 255)
    private String ipAddress;


    public LoginLogoutRecord(Integer userId, LocalDateTime loginTime, LocalDateTime logoutTime, String ipAddress) {
        this.userId = userId;
        this.loginTime = loginTime;
        this.logoutTime = logoutTime;
        this.ipAddress = ipAddress;
    }

    // 计算会话持续时间的方法
    public String getSessionDuration() {
        if (loginTime == null || logoutTime == null) {
            return "N/A";
        }

        long seconds = java.time.Duration.between(loginTime, logoutTime).getSeconds();
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;

        return String.format("%dh %dm", hours, minutes);
    }

    @Override
    public String toString() {
        return "LoginLogoutRecord{" +
                "id=" + id +
                ", userId=" + userId +
                ", loginTime=" + loginTime +
                ", logoutTime=" + logoutTime +
                ", ipAddress='" + ipAddress + '\'' +
                '}';
    }
}