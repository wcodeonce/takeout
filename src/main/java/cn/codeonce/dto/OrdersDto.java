package cn.codeonce.dto;

import cn.codeonce.pojo.OrderDetail;
import cn.codeonce.pojo.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;

}
