package edu.ap.softwareproject.api.dto;

import edu.ap.softwareproject.api.entity.OrderRequestProduct;

import java.util.List;
import java.util.function.Function;

public class OrderRequestProductMapper implements Function<OrderDetailsProductDTO, OrderRequestProduct> {

  @Override
  public OrderRequestProduct apply(OrderDetailsProductDTO orderDetailsProductDTO) {
    return new OrderRequestProduct(
            orderDetailsProductDTO.getHsCode(),
            orderDetailsProductDTO.getName(),
            orderDetailsProductDTO.getQuantity(),
            orderDetailsProductDTO.getWeight(),
            orderDetailsProductDTO.getContainerNumber(),
            orderDetailsProductDTO.getContainerSize(),
            orderDetailsProductDTO.getContainerType()
    );
  }
  public List<OrderRequestProduct> applyList(List<OrderDetailsProductDTO> orderDetailsProduct) {
    return orderDetailsProduct.stream().map(
            (orderDetails) -> apply(orderDetails)
    ).toList();
  }
}
