package com.desarrollox.backend_stranger_drug.api_notificaciones.models;

import java.time.LocalDateTime;
import com.desarrollox.backend_stranger_drug.api_compras.models.Purchase;
import com.desarrollox.backend_stranger_drug.api_registro.models.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "message", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "purchase_id")
    private Purchase purchase;
    
    @ManyToOne
    @JoinColumn(name = "sender_user_id")
    private User senderUser;

    @ManyToOne
    @JoinColumn(name = "receiver_user_id")
    private User receiverUser;

    public Notification(String message, Purchase purchase, User userSend, User userReceive) {
        this.message = message;
        this.purchase = purchase;
        this.senderUser = userSend;
        this.receiverUser = userReceive;
    }
}