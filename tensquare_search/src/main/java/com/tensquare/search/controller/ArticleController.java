package com.tensquare.search.controller;

import com.sun.org.apache.regexp.internal.RE;
import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping
    public Result add(@RequestBody Article article){
        articleService.add(article);
        return new Result(true, StatusCode.OK,"添加成功");
    }

    @GetMapping("/{key}/{page}/{size}")
    public Result search(@PathVariable String key, @PathVariable int page, @PathVariable int size){
        Page<Article> pageData = articleService.search(key,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<Article>(pageData.getTotalElements(),pageData.getContent()));
    }
}
