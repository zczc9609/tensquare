package com.tensquare.user.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.org.apache.regexp.internal.RE;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import com.tensquare.user.pojo.User;
import com.tensquare.user.service.UserService;

import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 * @author Administrator
 *
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private JwtUtil jwtUtil;

	//更新用户的关注数和好友的粉丝数，方法供其他服务调用
	@PostMapping("/{userid}/{friendid}/{x}")
	public void updatefanscountandfollowcount(@PathVariable String userid,@PathVariable String friendid,@PathVariable int x){
		userService.updatefanscountandfollowcount(x,userid,friendid);
	}

	//发送短信验证码
	@PostMapping("/sendsms/{mobile}")
	public Result sendSms(@PathVariable String mobile){
		userService.sendSms(mobile);
		return new Result(true,StatusCode.OK,"发送成功");
	}

	//用户注册
	@PostMapping("/register/{code}")
	public Result register(@PathVariable String code,@RequestBody User user){
		//获取缓存中的验证码
		String codeRedis = (String) redisTemplate.opsForValue().get("sms_code"+user.getMobile());
		if(codeRedis.isEmpty()){
			return new Result(false, StatusCode.ERROR, "请先接收验证码");
		}
		if(!codeRedis.equals(code)){
			return new Result(false, StatusCode.ERROR, "验证码错误");
		}
		userService.add(user);
		return new Result(true,StatusCode.OK,"注册成功");
	}

	//用户登录
	@PostMapping("/login")
	public Result userLogin(@RequestBody User user){
		User userLogin = userService.login(user.getMobile(),user.getPassword());
		if(userLogin==null){
			return new Result(false,StatusCode.LOGINERROR,"登录失败");
		}
		String token = jwtUtil.createJWT(userLogin.getId(), userLogin.getLoginname(), "user");
		Map<String,String> map = new HashMap<>();
		map.put("token",token);
		map.put("role","user");
		return new Result(true,StatusCode.OK,"登录成功",map);
	}
	
	/**
	 * 查询全部数据
	 * @return
	 */
	@RequestMapping(method= RequestMethod.GET)
	public Result findAll(){
		return new Result(true,StatusCode.OK,"查询成功",userService.findAll());
	}
	
	/**
	 * 根据ID查询
	 * @param id ID
	 * @return
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.GET)
	public Result findById(@PathVariable String id){
		return new Result(true,StatusCode.OK,"查询成功",userService.findById(id));
	}


	/**
	 * 分页+多条件查询
	 * @param searchMap 查询条件封装
	 * @param page 页码
	 * @param size 页大小
	 * @return 分页结果
	 */
	@RequestMapping(value="/search/{page}/{size}",method=RequestMethod.POST)
	public Result findSearch(@RequestBody Map searchMap , @PathVariable int page, @PathVariable int size){
		Page<User> pageList = userService.findSearch(searchMap, page, size);
		return  new Result(true,StatusCode.OK,"查询成功",  new PageResult<User>(pageList.getTotalElements(), pageList.getContent()) );
	}

	/**
     * 根据条件查询
     * @param searchMap
     * @return
     */
    @RequestMapping(value="/search",method = RequestMethod.POST)
    public Result findSearch( @RequestBody Map searchMap){
        return new Result(true,StatusCode.OK,"查询成功",userService.findSearch(searchMap));
    }
	
	/**
	 * 增加
	 * @param user
	 */
	@RequestMapping(method=RequestMethod.POST)
	public Result add(@RequestBody User user  ){
		userService.add(user);
		return new Result(true,StatusCode.OK,"增加成功");
	}
	
	/**
	 * 修改
	 * @param user
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.PUT)
	public Result update(@RequestBody User user, @PathVariable String id ){
		user.setId(id);
		userService.update(user);		
		return new Result(true,StatusCode.OK,"修改成功");
	}
	
	/**
	 * 删除	必须有admin角色
	 * @param id
	 */
	@RequestMapping(value="/{id}",method= RequestMethod.DELETE)
	public Result delete(@PathVariable String id, HttpServletRequest request){
		String token = (String) request.getAttribute("claims_admin");
		if(token == null || "".equals(token)){
			return new Result(false,StatusCode.ACCESSERROR,"权限不足！");
		}
		userService.deleteById(id);
		return new Result(true,StatusCode.OK,"删除成功");
	}
	
}
