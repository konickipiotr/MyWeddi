package com.myweddi.api.posts;

import com.myweddi.conf.Global;
import com.myweddi.db.CommentRepository;
import com.myweddi.db.LikeRepository;
import com.myweddi.db.PhotoRepository;
import com.myweddi.db.PostRepository;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.exception.ForbiddenException;
import com.myweddi.exception.NotFoundException;
import com.myweddi.model.*;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.*;
import com.myweddi.view.CommentView;
import com.myweddi.view.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PostService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;
    private FileService fileService;
    private PhotoRepository photoRepository;
    private LikeRepository likeRepository;

    private static final int PAGE_SIZE = 10;


    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository, HostRepository hostRepository, GuestRepository guestRepository, UserAuthRepository userAuthRepository, FileService fileService, PhotoRepository photoRepository, LikeRepository likeRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
        this.fileService = fileService;
        this.photoRepository = photoRepository;
        this.likeRepository = likeRepository;
    }

    public ResponseEntity<PostView> getPost(Long postid, String username){
        Optional<Post> oPost = this.postRepository.findById(postid);
        if(oPost.isEmpty())
            throw new NotFoundException();
        Post post = oPost.get();
        User currentUser = getUser(this.userAuthRepository.findByUsername(username).getId());
        checkAccess(post, currentUser);

        PostView postView = prepareSinglePost(post, currentUser);
        return new ResponseEntity<PostView>(postView, HttpStatus.OK);
    }

    private void checkAccess(Post post, User currentUser){
        if(post.getPosttype().equals(Posttype.LOCAL)){
            if(!post.getWeddingid().equals(currentUser.getWeddingid()))
                throw new ForbiddenException();
        }else{
            if(currentUser.getRole().equals("GUEST"))
                throw new ForbiddenException();
        }
    }


    public ResponseEntity<ListWrapper<PostView>> getLastPublicPosts(int page, String username){

        User currentUser = getUser(this.userAuthRepository.findByUsername(username).getId());
        if(currentUser.getRole().equals("GUEST"))
            throw new ForbiddenException();

        List<Post> posts = this.postRepository.findByPosttypeOrderByCreationdateDesc(Posttype.PUBLIC, PageRequest.of(0, PAGE_SIZE * page));

        List<PostView> postViews = new ArrayList<>();
        for(Post post : posts)
            postViews.add(prepareSinglePost(post, currentUser));

        postViews.forEach(i -> i.covert());
        return new ResponseEntity<ListWrapper<PostView>>(new ListWrapper<PostView>(postViews), HttpStatus.OK);
    }

    public ListWrapper<PostView> getLastWeddingPosts(int page, String username){

        User currentUser = getUser(this.userAuthRepository.findByUsername(username).getId());

        List<Post> posts = this.postRepository.findByWeddingidOrderByCreationdateDesc(currentUser.getWeddingid(), PageRequest.of(0, PAGE_SIZE * page));

        List<PostView> postViews = new ArrayList<>();
        for(Post post : posts)
            postViews.add(prepareSinglePost(post, currentUser));

        postViews.forEach(i -> i.covert());
        return new ListWrapper<>(postViews);
    }

    private PostView prepareSinglePost(Post post, User currentUser){

        User postUser = getUser(post.getUserid());
        PostView postView = new PostView(post, postUser);
        Long postid = post.getId();
        postView.setPhotos(this.photoRepository.findByPostid(postid));

        List<Comment> comments = this.commentRepository.findAllByPostidOrderByCreationdateAsc(postid);
        postView.setComments(getCommentViewList(comments, post, currentUser));
        postView.covert();

        List<WeddiLike> weddiLikes = likeRepository.findByPostid(postid);
        postView.setLikeNumber(weddiLikes.size());

        boolean isLiked = false;
        for(WeddiLike l : weddiLikes){
            if(l.getUserid().equals(currentUser.getId()))
                isLiked = true;
        }
        postView.setWeddiLike(isLiked);
        if(currentUser.getRole().equals("HOST"))
            postView.setMyPost(true);
        else
            postView.setMyPost(post.getUserid().equals(currentUser.getId()));
        return postView;
    }

    private List<CommentView> getCommentViewList(List<Comment> comments, Post post, User currentUser){
        List<CommentView> commentViewsList = new ArrayList<>();
        for(Comment c : comments){

            CommentView cv;
            if(c.getUserid().equals(Global.ACCOUNT_REMOVED)){
                cv = new CommentView(c);
            }else {
                User user = getUser(c.getUserid());
                cv = new CommentView(c, user);
            }

            if(currentUser.getRole().equals("HOST"))
                cv.setMyComment(true);
            else
                cv.setMyComment(currentUser.getId().equals(c.getUserid()));
            commentViewsList.add(cv);
        }
        commentViewsList.forEach(i -> i.covert());
        return commentViewsList;
    }

    private User getUser(Long id){
        UserAuth ua = this.userAuthRepository.findById(id).get();
        User user = null;

        if(ua.getRole().equals("HOST")){
            user = new User(this.hostRepository.findById(ua.getId()).get(), ua.getStatus());

        }else if(ua.getRole().equals("GUEST")){
            user = new User(this.guestRepository.findById(ua.getId()).get(), ua.getStatus());
        }
        user.setRole(ua.getRole());
        return user;
    }

    public Long newPostInDb(Post post, String username) {
        User currentUser = getUser(this.userAuthRepository.findByUsername(username).getId());
        post.setUserid(currentUser.getId());
        if(post.getPosttype().equals(Posttype.LOCAL))
            post.setWeddingid(currentUser.getWeddingid());
        else
            post.setWeddingid(0l);
        LocalDateTime now = LocalDateTime.now(Global.zid);
        post.setCreationdate(now.truncatedTo(ChronoUnit.SECONDS));

        this.postRepository.save(post);
        return post.getId();
    }

    public void savePostFiles(Long postid, Long userid, List<String> imagesStringBytes) {
        if(this.postRepository.existsById(postid)) {
            List<MultipartFile> multipartFiles = fileService.convertToMultipartFiles(imagesStringBytes);
            List<FileNameStruct> fileNameStructs = fileService.uploadPhotos(multipartFiles, PhotoCat.POST);
            if (fileNameStructs == null || fileNameStructs.isEmpty())
                throw new FailedSaveFileException();

            for(FileNameStruct fns : fileNameStructs){
                Photo photo = new Photo(postid, userid);
                photo.setRealPath(fns.realPath);
                photo.setWebAppPath(fns.webAppPath);
                this.photoRepository.save(photo);
            }
        }
    }

    public boolean deletePost(String username, Long postid){

        Optional<Post> oPost = this.postRepository.findById(postid);
        if(oPost.isEmpty())
            throw new NotFoundException();
        Post post = oPost.get();
        User currentUser = getUser(this.userAuthRepository.findByUsername(username).getId());
        checkAccess(post, currentUser);

        postRepository.deleteById(postid);
        commentRepository.deleteByPostid(postid);

        List<Photo> postPhotos = photoRepository.findByPostid(postid);
        for(Photo p : postPhotos){
            if(p.getRealPath() != null)
                fileService.deleteFile(p.getRealPath());
            photoRepository.deleteById(p.getId());
        }
        return true;
    }

    public boolean changePostStar(WeddiLike weddiLike){
        if(likeRepository.existsByPostidAndUserid(weddiLike.getPostid(), weddiLike.getUserid())){
            likeRepository.deleteByPostidAndUserid(weddiLike.getPostid(), weddiLike.getUserid());
            return false;
        }else {
            likeRepository.save(weddiLike);
            return true;
        }
    }
}
