package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.service.LabelService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin
@RefreshScope
@RequestMapping("/label")
public class UserController {
    @Autowired
    private LabelService labelService;

    @Value("${ip}")
    private String ip;

    @GetMapping
    public Result findAll() {
        return new Result(true, StatusCode.OK,"查询成功！",labelService.findAll());
    }

    @GetMapping(value = "/{id}")
    public Result findById(@PathVariable("id") String id) {
        return new Result(true, StatusCode.OK,"查询成功！",labelService.findById(id));
    }

    @PostMapping
    public Result add(@RequestBody Label label){
        labelService.add(label);
        return new Result(true, StatusCode.OK,"添加成功！");
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable("id") String id,@RequestBody Label label){
        label.setId(id);
        labelService.update(label);
        return new Result(true, StatusCode.OK,"修改成功！");
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable("id") String id){
        labelService.deleteById(id);
        return new Result(true, StatusCode.OK,"删除成功！");
    }

    //条件查询
    @PostMapping(value = "/search")
    public Result findSearch(@RequestBody Label label) {
        return new Result(true, StatusCode.OK,"查询成功！",labelService.findSearch(label));
    }

    //分页查询+条件查询
    @PostMapping(value = "/search/{page}/{size}")
    public Result pageSearch(@RequestBody Label label,@PathVariable int page,@PathVariable int size) {
        Page<Label> pageData = labelService.pageQuery(label,page,size);
        return new Result(true, StatusCode.OK,"查询成功！",new PageResult<Label>(pageData.getTotalElements(),pageData.getContent()));
    }
}
