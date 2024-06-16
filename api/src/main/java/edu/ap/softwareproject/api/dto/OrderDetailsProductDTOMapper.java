package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.OrderRequestProduct;

import java.util.List;
import java.util.function.Function;

public class OrderDetailsProductDTOMapper implements Function<OrderRequestProduct, OrderDetailsProductDTO> {

  @Override
  public OrderDetailsProductDTO apply(OrderRequestProduct orderRequestProduct) {
    return new OrderDetailsProductDTO(
            orderRequestProduct.getId(),
            orderRequestProduct.getHsCode(),
            orderRequestProduct.getName(),
            orderRequestProduct.getQuantity(),
            orderRequestProduct.getWeight(),
            orderRequestProduct.getContainerNumber(),
            orderRequestProduct.getContainerSize(),
            orderRequestProduct.getContainerType()
    );
  }
  public List<OrderDetailsProductDTO> applyList(List<OrderRequestProduct> orderRequestProduct) {
    return orderRequestProduct.stream().map(
            (orderDetails) -> apply(orderDetails)
    ).toList();
  }
}
