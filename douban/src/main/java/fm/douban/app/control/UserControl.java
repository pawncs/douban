package fm.douban.app.control;

import fm.douban.model.User;
import fm.douban.model.UserLoginInfo;
import fm.douban.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pawncs on 2021/2/18.
 */
@Controller
public class UserControl {
    Logger logger;
    UserService userService;

    public UserControl(UserService userService) {
        this.userService = userService;
        this.logger = Logger.getLogger(UserControl.class);
    }

    //注册
    @GetMapping(path = "/sign")
    public String signPage(Model model){
        return "sign";
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public Map registerAction(@RequestParam String name,
                       @RequestParam String password,
                       @RequestParam String mobile,
                       HttpServletRequest request,
                       HttpServletResponse response){
        Map returnData = new HashMap();
        if(userService.getUserByLoginName(name) != null){
            returnData.put("code","400");
            returnData.put("message","已经存在该账号");
            return returnData;
        }
        HttpSession session = request.getSession();
        User user = new User();
        user.setMobile(mobile);
        user.setLoginName(name);
        user.setPassword(password);
        User newUser = userService.add(user);
        if(newUser != null && StringUtils.hasText(newUser.getId())){
            returnData.put("code","200");
            returnData.put("message","注册成功");
        }else {
            returnData.put("code","400");
            returnData.put("message","注册失败");
        }
        return returnData;
    }

    //登录
    @GetMapping(path = "/login")
    public String loginPage(Model model){
        return "login";
    }
    //登录操作
    @PostMapping(path = "/authenticate")
    @ResponseBody
    public Map login(@RequestParam String name, @RequestParam String password, HttpServletRequest request,HttpServletResponse response){
        Map returnData = new HashMap();
        // 根据登录名查询用户
        User regedUser = userService.getUserByLoginName(name);

        // 找不到此登录用户
        if (regedUser == null) {
            returnData.put("code", "400");
            returnData.put("message", "userName not correct");
            return returnData;
        }
        if (regedUser.getPassword().equals(password)) {
            UserLoginInfo userLoginInfo = new UserLoginInfo();
            userLoginInfo.setUserId(regedUser.getId());
            userLoginInfo.setUserName(regedUser.getLoginName());
            // 取得 HttpSession 对象
            HttpSession session = request.getSession();
            // 写入登录信息
            session.setAttribute("userLoginInfo", userLoginInfo);
            returnData.put("result", true);
            returnData.put("message", "login successfule");
        } else {
            returnData.put("code", "400");
            returnData.put("message", "userName or password not correct");
        }

        return returnData;
    }
}
