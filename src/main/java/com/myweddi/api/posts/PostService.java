package com.myweddi.api.posts;

import com.myweddi.conf.Global;
import com.myweddi.db.CommentRepository;
import com.myweddi.db.PhotoRepository;
import com.myweddi.db.PostRepository;
import com.myweddi.exception.FailedSaveFileException;
import com.myweddi.model.Comment;
import com.myweddi.model.Photo;
import com.myweddi.model.Post;
import com.myweddi.user.reposiotry.GuestRepository;
import com.myweddi.user.User;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.HostRepository;
import com.myweddi.user.reposiotry.UserAuthRepository;
import com.myweddi.utils.FileNameStruct;
import com.myweddi.utils.FileService;
import com.myweddi.utils.ListWrapper;
import com.myweddi.utils.PhotoCat;
import com.myweddi.view.CommentView;
import com.myweddi.view.PostView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private PostRepository postRepository;
    private CommentRepository commentRepository;
    private HostRepository hostRepository;
    private GuestRepository guestRepository;
    private UserAuthRepository userAuthRepository;
    private FileService fileService;
    private PhotoRepository photoRepository;

    private static final int PAGE_SIZE = 20;

    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository, HostRepository hostRepository, GuestRepository guestRepository, UserAuthRepository userAuthRepository, FileService fileService, PhotoRepository photoRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.hostRepository = hostRepository;
        this.guestRepository = guestRepository;
        this.userAuthRepository = userAuthRepository;
        this.fileService = fileService;
        this.photoRepository = photoRepository;
    }

    public ListWrapper<PostView>  getPostFromPage(int page){
        page--;
        List<Post> posts = this.postRepository.findAllByOrderByCreationdateDesc(PageRequest.of(page, PAGE_SIZE));

        List<PostView> postViews = new ArrayList<>();
        for(Post p : posts){
            User user = getUser(p.getUserid());
            PostView pv = new PostView(p, user);
            pv.setPhotos(this.photoRepository.findByPostid(p.getId()));

            List<Comment> comments = this.commentRepository.findAllByPostidOrderByCreationdateDesc(p.getId());
            pv.setComments(getCommentViewList(comments, p));

            postViews.add(pv);
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

    public void savePostFiles(Long postid, Long userid, MultipartFile images) {
        if(this.postRepository.existsById(postid)) {
            FileNameStruct fileNameStructure = fileService.uploadPhotos(images, PhotoCat.POST);
            if (fileNameStructure == null)
                throw new FailedSaveFileException();

            Photo photo = new Photo(postid, userid);
            photo.setRealPath(fileNameStructure.realPath);
            photo.setWebAppPath(fileNameStructure.webAppPath);

            this.photoRepository.save(photo);
        }
    }

    public void addComment(Comment comment){
        comment.setCreationdate(LocalDateTime.now(Global.zid));
        this.commentRepository.save(comment);
    }
}
