package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.OrderRequest;

import java.util.function.Function;

public class OrderRequestDTOMapper implements Function<OrderRequest, OrderRequestDTO> {
    @Override
    public OrderRequestDTO apply(OrderRequest orderRequest) {
        OrderDetailsProductDTOMapper mapper = new OrderDetailsProductDTOMapper();
        return new OrderRequestDTO(
                orderRequest.getCustomerCode(),
                orderRequest.getTransportType(),
                orderRequest.getCustomerReferenceNumber(),
                orderRequest.getPortCode(),
                orderRequest.getCargoType(),
                mapper.applyList(orderRequest.getProducts())
        );
    }
}
