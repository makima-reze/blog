package com.makima.blog.controller;

import com.makima.blog.dto.*;
import com.makima.blog.service.ArticleService;
import com.makima.blog.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

/**
 * @author dai17
 * @create 2022-12-18 18:36
 */
@RestController
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping("/home/articles")
    public Result<List<ArticleHomeDTO>> listArticles(){
        return Result.ok(articleService.listArticles());
    }

    @GetMapping("/articles/archives")
    public Result<PageResult<ArchiveDTO>> listArchives() {
        return Result.ok(articleService.listArchives());
    }

    @GetMapping("/articles/{articleId}")
    public Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId){
        return Result.ok(articleService.getArticleById(articleId));
    }

    @PostMapping("/articles")
    public Result<?> saveOrUpdateArticle(@Valid @RequestBody ArticleVO articleVO){
        articleService.saveOrUpdateArticle(articleVO);
        return Result.ok();
    }

    @PostMapping("/articles/images")
    public Result<String> saveArticleImages(MultipartFile file) throws IOException {
//        //获取文件名
//        String fileName = file.getOriginalFilename();
//        //获取文件后缀名。也可以在这里添加判断语句，规定特定格式的图片才能上传，否则拒绝保存。
//        String suffixName = fileName.substring(fileName.lastIndexOf("."));
//        //为了避免发生图片替换，这里使用了文件名重新生成
//        fileName = UUID.randomUUID()+suffixName;
//        String path = ResourceUtils.getURL("classpath:").getPath()+"public/img/";
//        file.transferTo(new File(path+fileName));
//        return Result.ok("http://114.132.77.182:8080/img/"+fileName);
////        return Result.ok("http://localhost:8080/img/"+fileName);
        if(!file.isEmpty()){
//            String uploadPath = "C:\\uploadFile";
//            String uploadPath = "E:\\2023holiday\\java\\demo\\demo5-blog-fangdemo3\\bushu\\upload";
            String uploadPath = "/usr/local/upload";
            // 如果目录不存在则创建
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }
            String OriginalFilename = file.getOriginalFilename();//获取原文件名
            String suffixName = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));//获取文件后缀名
            //重新随机生成名字
            String filename = UUID.randomUUID().toString() +suffixName;
            File localFile = new File(uploadPath+"/"+filename);
            try {
                file.transferTo(localFile); //把上传的文件保存至本地
                /**
                 * 这里应该把filename保存到数据库,供前端访问时使用
                 */
                return Result.ok("http://114.132.77.182:8080/img/"+filename);//上传成功，返回保存的文件地址
            }catch (IOException e){
                e.printStackTrace();
                System.out.println("上传失败");
                return Result.fail();
            }
        }else{
            System.out.println("文件为空");
            return Result.fail();
        }

    }

    @GetMapping("/list/articles")
    public Result<PageResult<ArticleBackDTO>> listArticleBacks(ConditionVO condition){
        return Result.ok(articleService.listArticleBacks(condition));
    }

    @GetMapping("/edit/{articleId}")
    public Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return Result.ok(articleService.getArticleBackById(articleId));
    }


    @DeleteMapping("/articles")
    public Result<?> deleteArticle(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticles(articleIdList);
        return Result.ok();
    }

    @PutMapping("/articles")
    public Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return Result.ok();
    }

    @GetMapping("/articles/search")
    public Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition){
        return Result.ok(articleService.listArticleBySearch(condition));
    }

    @GetMapping("/articles/condition")
    public Result<ArticlePreviewListDTO> listArticlesByCondition(ConditionVO condition) {
        return Result.ok(articleService.listArticlesByCondition(condition));
    }



//    @PostMapping("/articles/{articleId}/like")
//    public Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId) {
//        articleService.saveArticleLike(articleId);
//        return Result.ok();
//    }


}
