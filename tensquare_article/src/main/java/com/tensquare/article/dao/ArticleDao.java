package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{
//    1、在@Query注解中编写JPQL实现DELETE和UPDATE操作的时候必须加上@modifying注解，以通知Spring Data 这是一个DELETE或UPDATE操作。
//    2、UPDATE或者DELETE操作需要使用事务，此时需要 定义Service层，在Service层的方法上添加事务操作。
    @Modifying
    @Query(value = "UPDATE tb_article SET state=1 WHERE id=?", nativeQuery = true)
    public void setState(String id);

    @Modifying
    @Query(value = "UPDATE tb_article SET thumbup=thumbup+1 WHERE id=?", nativeQuery = true)
    public void setThumbup(String id);
}
