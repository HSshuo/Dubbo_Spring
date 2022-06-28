package bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author SHshuo
 * @data 2022/6/24--17:35
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAddress implements Serializable {

    private Integer id;
    private String userAdress;
    private String userId;
    private String consignee;
    private String phoneNum;
    private String isDefault;
}
