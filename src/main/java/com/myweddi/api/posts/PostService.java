package com.myweddi.api.posts;

import com.myweddi.conf.Global;
import com.myweddi.db.CommentRepository;
import com.myweddi.db.LikeRepository;
import com.myweddi.db.PhotoRepository;
import com.myweddi.db.PostRepository;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.Comment;
import com.myweddi.model.Like;
import com.myweddi.model.Photo;
import com.myweddi.model.Post;
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
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
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

    private static final int PAGE_SIZE = 20;


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

    public PostView getPost(Long postid){
        Post post = this.postRepository.findById(postid).get();
        User user = getUser(post.getUserid());
        PostView postView = new PostView(post, user);
        postView.setPhotos(this.photoRepository.findByPostid(post.getId()));

        List<Comment> comments = this.commentRepository.findAllByPostidOrderByCreationdateAsc(post.getId());
        postView.setComments(getCommentViewList(comments, post));
        postView.covert();

        List<Like> likes = likeRepository.findAllByPostid(postid);
        postView.setLikeNumber(likes.size());

        boolean isLiked = likes.stream().anyMatch(i -> i.getUserid().equals(user.getId()));
        postView.setLiked(isLiked);

        return postView;
    }

    public ListWrapper<PostView>  getPostFromPage(int page){
        page--;
        List<Post> posts = this.postRepository.findAllByOrderByCreationdateDesc(PageRequest.of(page, PAGE_SIZE));

        List<PostView> postViews = new ArrayList<>();
        for(Post p : posts){
//            User user = getUser(p.getUserid());
//            PostView pv = new PostView(p, user);
//            pv.setPhotos(this.photoRepository.findByPostid(p.getId()));
//
//            List<Comment> comments = this.commentRepository.findAllByPostidOrderByCreationdateAsc(p.getId());
//            pv.setComments(getCommentViewList(comments, p));
//
//            postViews.add(pv);
            postViews.add(getPost(p.getId()));
        }

        postViews.forEach(i -> i.covert());
        return new ListWrapper<>(postViews);
    }

    private List<CommentView> getCommentViewList(List<Comment> comments, Post post){
        List<CommentView> commentViewsList = new ArrayList<>();
        for(Comment c : comments){
            User user = getUser(c.getUserid());
            CommentView cv = new CommentView(c, user);
            commentViewsList.add(cv);
        }
        commentViewsList.forEach(i -> i.covert());
        return commentViewsList;
    }

    private User getUser(Long id){
        UserAuth ua = this.userAuthRepository.findById(id).get();
        User user = null;

        if(ua.getRole().equals("HOST")){
            user = new User(this.hostRepository.findById(ua.getId()).get());
        }else if(ua.getRole().equals("GUEST")){
            user = new User(this.guestRepository.findById(ua.getId()).get());
        }
        return user;
    }

    public Long newPostInDb(Post post) {
        LocalDateTime now = LocalDateTime.now(Global.zid);
        post.setCreationdate(now.truncatedTo(ChronoUnit.SECONDS));
        this.postRepository.save(post);
        return post.getId();
    }

    public void savePostFiles(Long postid, Long userid, List<String> imagesStringBytes) {
        if(this.postRepository.existsById(postid)) {
            List<MultipartFile> multipartFiles = convertToMultipartFile(imagesStringBytes);
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

    public List<MultipartFile> convertToMultipartFile(List<String> imagesStringBytes){
        List<MultipartFile> mFiles = new ArrayList<>();
        for(String sImage : imagesStringBytes){
            byte[] imgbyte = Base64.getDecoder().decode(sImage);
            mFiles.add(new CustomMultipartFile(imgbyte));
        }
        return mFiles;
    }

    public boolean deletePost(Principal principal, Long postid){
        UserAuth user = userAuthRepository.findByUsername(principal.getName());
        Post post = postRepository.findById(postid).get();

        if(!user.getId().equals(post.getUserid()))
            return false;
        postRepository.deleteById(postid);
        commentRepository.deleteByPostid(postid);

        List<Photo> postPhotos = photoRepository.findByPostid(postid);
        for(Photo p : postPhotos){
            try {
                fileService.deleteFile(p.getRealPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            photoRepository.deleteById(p.getId());
        }
        return true;
    }

    public boolean changePostStar(Long post, Long userid){
        if(likeRepository.existsByPostidAndUserid(post, userid)){
            likeRepository.deleteByPostidAndUserid(post, userid);
            return false;
        }else {
            likeRepository.save(new Like(post, userid));
            return true;
        }
    }
}
