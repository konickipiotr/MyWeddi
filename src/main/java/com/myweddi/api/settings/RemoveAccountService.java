package com.myweddi.api.settings;

import com.myweddi.conf.Global;
import com.myweddi.db.*;
import com.myweddi.exception.ForbiddenException;
import com.myweddi.model.*;
import com.myweddi.modules.gift.model.GeneralGiftRepository;
import com.myweddi.modules.gift.model.Gift;
import com.myweddi.modules.gift.model.GiftRepository;
import com.myweddi.modules.info.WeddingInfo;
import com.myweddi.modules.info.WeddingInfoRepository;
import com.myweddi.user.Guest;
import com.myweddi.user.Host;
import com.myweddi.user.UserAuth;
import com.myweddi.user.reposiotry.*;
import com.myweddi.utils.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class RemoveAccountService {

    private UserAuthRepository userAuthRepository;
    private GuestRepository guestRepository;
    private HostRepository hostRepository;

    private WeddingInfoRepository weddingInfoRepository;
    private GeneralGiftRepository generalGiftRepository;

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    private TablePlaceRepository tablePlaceRepository;
    private TablesRepository tablesRepository;
    private GiftRepository giftRepository;
    private LikeRepository likeRepository;
    private PhotoRepository photoRepository;

    private FileService fileService;
    private ActivationRepository activationRepository;
    private OneTimeRepository oneTimeRepository;
    private PasswordRestRepository passwordRestRepository;

    @Autowired
    public RemoveAccountService(UserAuthRepository userAuthRepository, GuestRepository guestRepository, HostRepository hostRepository, WeddingInfoRepository weddingInfoRepository, GeneralGiftRepository generalGiftRepository, CommentRepository commentRepository, PostRepository postRepository, TablePlaceRepository tablePlaceRepository, TablesRepository tablesRepository, GiftRepository giftRepository, LikeRepository likeRepository, PhotoRepository photoRepository, FileService fileService, ActivationRepository activationRepository, OneTimeRepository oneTimeRepository, PasswordRestRepository passwordRestRepository) {
        this.userAuthRepository = userAuthRepository;
        this.guestRepository = guestRepository;
        this.hostRepository = hostRepository;
        this.weddingInfoRepository = weddingInfoRepository;
        this.generalGiftRepository = generalGiftRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.tablePlaceRepository = tablePlaceRepository;
        this.tablesRepository = tablesRepository;
        this.giftRepository = giftRepository;
        this.likeRepository = likeRepository;
        this.photoRepository = photoRepository;
        this.fileService = fileService;
        this.activationRepository = activationRepository;
        this.oneTimeRepository = oneTimeRepository;
        this.passwordRestRepository = passwordRestRepository;
    }

    public ResponseEntity<Void> removeHostWeddingAccount(Long userid, Long weddingid, String username){
        if(!userid.equals(weddingid))
            throw new ForbiddenException();

        Optional<UserAuth> oUser = this.userAuthRepository.findById(userid);
        UserAuth userAuth = this.userAuthRepository.findByUsername(username);
        if(oUser.isPresent()){
            UserAuth ua = oUser.get();
            if(userAuth != null && ua.getId().equals(userAuth.getId())){
                this.userAuthRepository.deleteById(userid);
            }else {
                throw new ForbiddenException();
            }
        }

        deletePostsAndComments(weddingid);
        deleteTables(weddingid);
        deleteGifts(weddingid);
        deleteWeddingInfo(weddingid);

        if(this.activationRepository.existsById(weddingid))
            this.activationRepository.deleteById(weddingid);

        if(this.oneTimeRepository.existsById(weddingid))
            this.oneTimeRepository.deleteById(weddingid);

        if(this.passwordRestRepository.existsById(weddingid))
            this.passwordRestRepository.deleteById(weddingid);

        if(this.likeRepository.existsByUserid(weddingid))
            this.likeRepository.deleteByUserid(weddingid);

        Optional<Host> oHost = this.hostRepository.findById(weddingid);
        if(oHost.isPresent()){
            Host host = oHost.get();
            fileService.deleteFile(host.getRealPath());
            this.hostRepository.deleteById(userid);
        }

        List<Guest> guests = this.guestRepository.findByWeddingid(weddingid);
        for(Guest guest: guests){
            Optional<UserAuth> oGuestUser = this.userAuthRepository.findById(guest.getId());
            if(oGuestUser.isPresent()){
                UserAuth guestAuth = oGuestUser.get();
                removeGuestAccount(guestAuth.getId(), guestAuth.getUsername());
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private void deleteGifts(Long weddingid){

        this.generalGiftRepository.deleteById(weddingid);
        this.giftRepository.deleteByWeddingid(weddingid);

    }

    private void deleteWeddingInfo(Long weddingid){
        Optional<WeddingInfo> oWedding = this.weddingInfoRepository.findById(weddingid);
        if(oWedding.isPresent()){
            WeddingInfo weddingInfo = oWedding.get();
            fileService.deleteFile(weddingInfo.getwRealPath());
            fileService.deleteFile(weddingInfo.getChRealPath());
            this.weddingInfoRepository.deleteById(weddingid);
        }

    }

    private void deletePostsAndComments(Long weddingid){
        List<Post> posts = this.postRepository.findByWeddingid(weddingid);

        for(Post post : posts){
            this.commentRepository.deleteByPostid(post.getId());
            List<Photo> photos = this.photoRepository.findByPostid(post.getId());
            for(Photo p: photos){
                fileService.deleteFile(p.getRealPath());
            }
            this.photoRepository.deleteAll(photos);
        }
        this.postRepository.deleteAll(posts);
    }

    private void deleteTables(Long weddingid){
        if(this.tablePlaceRepository.existsByWeddingid(weddingid))
            this.tablePlaceRepository.deleteByWeddingid(weddingid);

        if(this.tablesRepository.existsById(weddingid));
            this.tablesRepository.deleteById(weddingid);
    }

    public ResponseEntity<Void> removeGuestAccount(Long userid, String username){

        Optional<UserAuth> oUser = this.userAuthRepository.findById(userid);
        UserAuth userAuth = this.userAuthRepository.findByUsername(username);
        if(oUser.isPresent()){
            UserAuth ua = oUser.get();
            if(userAuth != null && ua.getId().equals(userAuth.getId())){
                this.userAuthRepository.deleteById(userid);
            }else {
                throw new ForbiddenException();
            }
        }

        removeGuestFromComment(userid);
        removeGuestFromPosts(userid);
        removeGuestFromTables(userid);
        removeGuestFromGift(userid);
        removeGuestFromPhoto(userid);

        if(this.activationRepository.existsById(userid))
            this.activationRepository.deleteById(userid);

        if(this.oneTimeRepository.existsById(userid))
            this.oneTimeRepository.deleteById(userid);

        if(this.passwordRestRepository.existsById(userid))
            this.passwordRestRepository.deleteById(userid);

        if(this.likeRepository.existsByUserid(userid))
            this.likeRepository.deleteByUserid(userid);

        Optional<Guest> oGuest = this.guestRepository.findById(userid);
        if(oGuest.isPresent()){
            Guest guest = oGuest.get();
            fileService.deleteFile(guest.getRealPath());
            this.guestRepository.deleteById(userid);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    private void removeGuestFromGift(Long userid){
        Optional<Gift> oGift = this.giftRepository.findByUserid(userid);
        if(oGift.isPresent()){
            Gift gift = oGift.get();
            gift.setUserid(null);
            this.giftRepository.save(gift);
        }
    }

    private void removeGuestFromTables(Long userid){
        if(this.tablePlaceRepository.existsByUserid(userid)) {
            TablePlace tablePlace = this.tablePlaceRepository.findByUserid(userid);
            Long weeddingid = tablePlace.getWeddingid();
            tablePlace.setUserid(-1l);
            this.tablePlaceRepository.save(tablePlace);

            Optional<Tables> oTable = this.tablesRepository.findById(weeddingid);
            if(oTable.isPresent()){
                Tables tables = oTable.get();
                tables.removeGuest();
                this.tablesRepository.save(tables);
            }
        }
    }

    private void removeGuestFromPhoto(Long userid){
        List<Photo> photos = this.photoRepository.findByUserid(userid);
        photos.forEach(i -> i.setUserid(Global.ACCOUNT_REMOVED));
        this.photoRepository.saveAll(photos);
    }

    private void removeGuestFromComment(Long userid){
        List<Comment> comments = this.commentRepository.findByUserid(userid);
        comments.forEach(i -> i.setUserid(Global.ACCOUNT_REMOVED));
        this.commentRepository.saveAll(comments);
    }

    private void removeGuestFromPosts(Long userid){
        List<Post> posts = this.postRepository.findByUserid(userid);
        posts.forEach(i -> i.setUserid(Global.ACCOUNT_REMOVED));
        this.postRepository.saveAll(posts);
    }
}
