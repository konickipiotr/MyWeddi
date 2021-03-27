package com.myweddi.api.posts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myweddi.model.Comment;
import com.myweddi.model.Post;
import com.myweddi.utils.CustomMultipartFile;
import com.myweddi.utils.ListWrapper;
import com.myweddi.view.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/post")
public class PostAPIController {

    @Autowired
    private PostService postService;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/{weddingid}/{page}")
    public ResponseEntity<ListWrapper<PostView>> getLastPostList(@PathVariable("weddingid") Long weddingid, @PathVariable("page") int page){
        return  new ResponseEntity<ListWrapper<PostView>>(postService.getPostFromPage(page), HttpStatus.OK);
    }

//    @GetMapping(value = "/{weddingid}/{page}", produces = { "application/json" })
//    public ListWrapper<PostView> getLastPostList(@PathVariable("weddingid") Long weddingid, @PathVariable("page") int page){
//        ListWrapper<PostView> body = new ResponseEntity<ListWrapper<PostView>>(postService.getPostFromPage(page), HttpStatus.OK).getBody();
//        System.out.println(body);
//        return body;
//    }

    @PostMapping
    public ResponseEntity<Long> newPostInDb(@RequestBody Post post){
        Long postid = postService.newPostInDb(post);
        return  new ResponseEntity<Long>(postid, HttpStatus.CREATED);
    }

    @PostMapping(path = "/{userid}/{postid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> savePostFiles(@PathVariable("userid") Long userid, @PathVariable("postid") Long postid, @RequestBody String images){
        images = images.substring(12, images.length() - 3);
        byte[] imgbyte = Base64.getDecoder().decode(images);
        MultipartFile multipartFile = new CustomMultipartFile(imgbyte);
        postService.savePostFiles(postid, userid, multipartFile);
        return  new ResponseEntity<Long>(postid, HttpStatus.CREATED);
    }

    @PostMapping("/addcomment")
    public void addComment(@RequestBody Comment comment){
        this.postService.addComment(comment);

    }
}
