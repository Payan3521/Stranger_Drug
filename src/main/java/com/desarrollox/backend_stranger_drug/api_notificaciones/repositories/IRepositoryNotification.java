package com.desarrollox.backend_stranger_drug.api_notificaciones.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.desarrollox.backend_stranger_drug.api_notificaciones.models.Notification;

@Repository
public interface IRepositoryNotification extends JpaRepository<Notification, Long>{

    List<Notification> findByReceiverUser(Long receiveUserId);

    @Query(value = "DELETE FROM notifications WHERE receiver_user_id = :receiverUser", nativeQuery = true)
    Void deleteAllByReceiverUserId(@Param("userId") Long receiverUser);
}
