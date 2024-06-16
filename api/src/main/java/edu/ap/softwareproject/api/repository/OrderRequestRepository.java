package edu.ap.softwareproject.api.repository;

import java.util.Optional;
import java.util.List;

import edu.ap.softwareproject.api.entity.OrderRequest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRequestRepository extends CrudRepository<OrderRequest, String> {
    Optional<List<OrderRequest>> findByCustomerCode(
            @Param("customerCode") String customerCode);

    Optional<OrderRequest> findByCustomerReferenceNumber(
            @Param("customerReferenceNumber") String customerReferenceNumber);

    Optional<List<OrderRequest>> findByTransportType(@Param("transportType") String transportType);

    Optional<List<OrderRequest>> findByPortCode(@Param("portCode") String portCode);

    Optional<List<OrderRequest>> findByCargoType(@Param("cargoType") String cargoType);

    Optional<Boolean> deleteByCustomerReferenceNumber(@Param("customerReferenceNumber") String customerReferenceNumber);
}
