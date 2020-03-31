package com.tensquare.friend.controller;

import com.tensquare.friend.client.ClientService;
import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/friend")
public class FriendController {
    @Autowired
    private FriendService friendService;

    @Autowired
    private ClientService clientService;

    //新增好友或非好友
    @PutMapping("/like/{friendid}/{type}")
    public Result addFriend(@PathVariable String friendid, @PathVariable String type,
                            HttpServletRequest request){
        //判断是否登录，且拥有claims_user权限，然后获取用户id
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false, StatusCode.ERROR,"权限不足");
        }
        String userid = claims.getId();
        //判断是添加好友还是非好友，即type=1或2
        if(type!=null && !type.equals("")){
            if(type.equals("1")){
                //添加好友
                int flag = friendService.addFriend(userid,friendid);
                if(flag == 0){
                    return new Result(false, StatusCode.ERROR,"请不要重复添加好友");
                }
                if(flag == 1){
                    clientService.updatefanscountandfollowcount(userid,friendid,1);
                    return new Result(true, StatusCode.OK,"添加好友成功");
                }
                return new Result(false, StatusCode.ERROR,"参数异常");
            }
            else if(type.equals("2")){
                //添加非好友
                int flag = friendService.addNoFriend(userid,friendid);
                //flag=0,已经在非好友列表中
                //flag=1,添加成功
                if(flag == 0){
                    return new Result(false, StatusCode.ERROR,"请不要重复添加非好友");
                }
                if(flag == 1){
                    return new Result(true, StatusCode.OK,"添加非好友成功");
                }
                return new Result(false, StatusCode.ERROR,"参数异常");
            }
            return new Result(false, StatusCode.ERROR,"参数异常");
        }

        return new Result(false, StatusCode.ERROR,"参数异常");

    }

    //删除好友
    @DeleteMapping("/{friendid}")
    public Result deleteFriend(@PathVariable String friendid,HttpServletRequest request){
        //判断是否登录，且拥有claims_user权限，然后获取用户id
        Claims claims = (Claims) request.getAttribute("claims_user");
        if(claims == null){
            return new Result(false, StatusCode.ERROR,"权限不足");
        }
        String userid = claims.getId();
        friendService.deleteFriend(userid,friendid);
        clientService.updatefanscountandfollowcount(userid,friendid,-1);
        return new Result(true,StatusCode.OK,"删除成功");
    }
}
