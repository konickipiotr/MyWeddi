package com.myweddi.api.posts;

import com.myweddi.model.Comment;
import com.myweddi.model.Like;
import com.myweddi.model.Post;
import com.myweddi.model.PostUserId;
import com.myweddi.utils.ListWrapper;
import com.myweddi.view.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api/post")
public class PostAPIController {

    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/{weddingid}/{page}")
    public ResponseEntity<ListWrapper<PostView>> getLastPostList(@PathVariable("weddingid") Long weddingid, @PathVariable("page") int page, Principal principal) {
        return new ResponseEntity<ListWrapper<PostView>>(postService.getPostFromPage(page, principal), HttpStatus.OK);
    }

    @GetMapping("{weddingid}/post/{postid}")
    public ResponseEntity<PostView> getPostView(@PathVariable("weddingid") Long weddingid, @PathVariable("postid") Long postid, Principal principal) {
        return new ResponseEntity<PostView>(postService.getPost(postid, principal), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> newPostInDb(@RequestBody Post post) {
        Long postid = postService.newPostInDb(post);
        return new ResponseEntity<Long>(postid, HttpStatus.CREATED);
    }

    @PostMapping(path = "/{userid}/{postid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> savePostFiles(@PathVariable("userid") Long userid, @PathVariable("postid") Long postid, @RequestBody List<String> images) {
        postService.savePostFiles(postid, userid, images);
        return new ResponseEntity<Long>(postid, HttpStatus.CREATED);
    }

    @PostMapping("/addcomment")
    public void addComment(@RequestBody Comment comment) {
        this.commentService.addComment(comment);
    }

    @DeleteMapping("/deletecomment/{commentid}")
    public ResponseEntity deleteComment(@PathVariable("commentid") Long comId, Principal principal){
        if(commentService.deleteComment(principal, comId))
            return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deletepost/{postid}")
    public ResponseEntity deletePost(@PathVariable("postid") Long postid, Principal principal){
        if(postService.deletePost(principal, postid))
            return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/changestar")
    public ResponseEntity<Boolean> isLiked(@RequestBody Like like){
        return new ResponseEntity<Boolean>(postService.changePostStar(like), HttpStatus.OK);
    }

}
