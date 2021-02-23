package fm.douban.service;

import fm.douban.model.User;
import fm.douban.param.UserQueryParam;
import org.springframework.data.domain.Page;

/**
 * Created by pawncs on 2021/2/18.
 */
public interface UserService {
    User add(User user);
    User get(String id);
    User getUserByLoginName(String name);
    //条件查询支持分页
    Page<User> list(UserQueryParam param);
    //修改
    boolean modify(User user);
    boolean delete(String id);
}
