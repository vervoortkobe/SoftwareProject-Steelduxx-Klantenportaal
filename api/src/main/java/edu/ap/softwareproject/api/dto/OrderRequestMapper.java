package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.OrderRequest;

import java.util.function.Function;

public class OrderRequestMapper implements Function<OrderRequestDTO, OrderRequest> {

    @Override
    public OrderRequest apply(OrderRequestDTO orderRequestDTO) {
        OrderRequestProductMapper mapper = new OrderRequestProductMapper();
        return new OrderRequest(
                orderRequestDTO.customerCode(),
                orderRequestDTO.transportType(),
                orderRequestDTO.customerReferenceNumber(),
                orderRequestDTO.portCode(),
                orderRequestDTO.cargoType(),
                mapper.applyList(orderRequestDTO.products())
        );
    }
}
