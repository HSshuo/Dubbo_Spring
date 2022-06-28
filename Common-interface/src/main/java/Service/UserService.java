package Service;

import bean.UserAddress;
import java.util.List;

/**
 * @author SHshuo
 * @data 2022/6/24--17:34
 */
public interface UserService {
    /***
     * 按照用户id返回所有的信息
     */
    public List<UserAddress> getUserAddressList(String userId);
}
