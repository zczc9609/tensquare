package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/spit")
public class SpitController {
    @Autowired
    private SpitService spitService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findById(id));
    }

    @PostMapping
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @PutMapping("/{id}")
    public Result update(@PathVariable String id, @RequestBody Spit spit){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true, StatusCode.OK,"修改成功");
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable String id){
        spitService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }

    @GetMapping("/comment/{parentid}/{page}/{size}")
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> spitPage = spitService.findByParentid(parentid, page, size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Spit>(spitPage.getTotalElements(),spitPage.getContent()));
    }

    //点赞功能，且一个用户不能重复点赞(把点过赞的用户id存到缓存中)
    @PutMapping("/thumbup/{spitId}")
    public Result thumbup(@PathVariable String spitId) {
        //目前未做认证，先写死userid
        String userid = "111";
        if(redisTemplate.opsForValue().get("thumbup_"+userid)!=null){
            //说明已经点过赞了
            return new Result(false,StatusCode.REPERROR,"不能重复点赞");
        }
        spitService.thumbup(spitId);
        redisTemplate.opsForValue().set("thumbup_"+userid,1);
        return new Result(true, StatusCode.OK,"点赞成功");
    }
}
