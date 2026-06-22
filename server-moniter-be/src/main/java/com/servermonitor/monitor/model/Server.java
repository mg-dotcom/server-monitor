package com.servermonitor.monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "servers")
public class Server {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    @Column(unique = true)
    private String endpoint;
    private Boolean isMonitored;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)

    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(
            mappedBy = "server",
            cascade = CascadeType.REMOVE
    )
    private List<Log> logs;
//    ฝั่งที่มี mappedBy = แค่อ่านย้อนกลับ ไม่มี column ใน DB

    @JsonIgnore
    @OneToMany(mappedBy = "server", cascade = CascadeType.REMOVE)
    @Builder.Default
    private List<ServerOperator> serverOperators = new ArrayList<>();
}
