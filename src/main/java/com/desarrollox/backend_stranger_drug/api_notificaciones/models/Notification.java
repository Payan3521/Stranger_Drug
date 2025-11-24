package com.desarrollox.backend_stranger_drug.api_notificaciones.models;

import java.time.LocalDateTime;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private LocalDateTime createdAt;
    
    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "purchase_id", nullable = false)
    private Purchase purchase;
    
    @Column(name = "sender_user_id", nullable = false)
    private User senderUser;

    @Column(name = "receiver_user_id",nullable = false)
    private User receiverUser;
}