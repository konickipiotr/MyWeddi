package com.myweddi.api.posts;

import com.myweddi.model.Comment;
import com.myweddi.model.Posttype;
import com.myweddi.model.WeddiLike;
import com.myweddi.model.Post;
import com.myweddi.utils.ListWrapper;
import com.myweddi.view.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
        return new ResponseEntity<ListWrapper<PostView>>(postService.getLastWeddingPosts(page, principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/public/{page}")
    public ResponseEntity<ListWrapper<PostView>> getLastWatchedPostList(@PathVariable("page") int page, Principal principal) {
        return postService.getLastPublicPosts(page, principal.getName());
    }

    @GetMapping("{weddingid}/post/{postid}")
    public ResponseEntity<PostView> getPostView(@PathVariable("weddingid") Long weddingid, @PathVariable("postid") Long postid, Principal principal) {
        return postService.getPost(postid, principal.getName());
    }

    @PostMapping
    public ResponseEntity<Long> newPostInDb(@RequestBody Post post, Principal principal) {
        Long postid = postService.newPostInDb(post, principal.getName());
        return new ResponseEntity<Long>(postid, HttpStatus.CREATED);
    }

    @PostMapping(path = "/{userid}/{postid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> savePostFiles(@PathVariable("userid") Long userid, @PathVariable("postid") Long postid, @RequestBody List<String> images) {
        postService.savePostFiles(postid, userid, images);
        return new ResponseEntity<Long>(postid, HttpStatus.CREATED);
    }

    @PostMapping("/addcomment")
    public void addComment(@RequestBody Comment comment, Principal principal) {
        this.commentService.addComment(comment, principal.getName());
    }

    @DeleteMapping("/deletecomment")
    public ResponseEntity deleteComment(@RequestBody Long commentid, Principal principal){
        return commentService.deleteComment(commentid, principal.getName());
    }

    @DeleteMapping("/deletepost")
    public ResponseEntity deletePost(@RequestBody Long postid, Principal principal){
        if(postService.deletePost(principal.getName(), postid))
            return new ResponseEntity(HttpStatus.OK);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/changestar")
    public ResponseEntity<Boolean> isLiked(@RequestBody WeddiLike weddiLike){
        return new ResponseEntity<Boolean>(postService.changePostStar(weddiLike), HttpStatus.OK);
    }
}
