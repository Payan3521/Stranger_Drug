# Stranger_Drug

1. Api de registro
2. Api de modelos
-- listo

    3. Api secciones
    4. Api de login
    5. Api de notificaciones
    6. Api de compras
    - Implementacion JWT

7. Api de pagos
8. Api de videos
- Dockerizar para despliegue
- Despligue en aws


Cambiar de String videoUrl a Video video en Purchase
mirar si poner void en deletes controller
poner estado en clase de dominio de purchase
Validaciones en metodos de objetos relacionados:

    @Override
    public List<Purchase> findByBuyerUserId(Long buyerId) {
       return repositoryPurchase.findByBuyerUser(buyerId);
    }

