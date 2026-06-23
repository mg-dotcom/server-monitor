package com.servermonitor.monitor.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(
        name = "servers_operators",
        uniqueConstraints = @UniqueConstraint(columnNames = {"server_id", "operator_id"})
)
public class ServerOperator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private Server server;

    @ManyToOne
    @JoinColumn(name = "operator_id", nullable = false)
    private User operator;
}
