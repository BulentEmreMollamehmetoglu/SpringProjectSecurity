package com.project.questapp.services;


import com.project.questapp.entities.Like;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.LikeRepository;
import com.project.questapp.requests.LikeCreateRequest;
import com.project.questapp.responses.LikeResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;
    private UserService userService;
    private PostService postService;

    public LikeService(LikeRepository likeRepository,UserService userService,PostService postService) {
        this.likeRepository = likeRepository;
        this.userService = userService;
        this.postService = postService;
    }

    public List<LikeResponse> getAllLikesWithParam(Optional<Long> userId, Optional<Long> postId) {
        List<Like> list;
        // her şeyi like repsonse yazmak yerine likeresponse içerisinde like a mapping işlemi yapıyoruz.
        // instead of writing everything in likeResponse we have just mapped like object to likeResponse
        if(userId.isPresent() && postId.isPresent()){
            list = this.likeRepository.findByUserIdAndPostId(userId.get(),postId.get());
        }
        else if(userId.isPresent()){
            list = this.likeRepository.findByUserId(userId.get());
        }
        else if(postId.isPresent()){
            list = this.likeRepository.findByPostId(postId.get());
        }else{
            list = this.likeRepository.findAll();
        }
        return list.stream().map(like -> new LikeResponse(like)).collect(Collectors.toList());
    }

    public Like getOneLikeById(Long likeId) {
        return this.likeRepository.findById(likeId).orElse(null);
    }

    public Like createOneLike(LikeCreateRequest request) {
        User user = this.userService.getOneUserById(request.getUserId());
        Post post = this.postService.getOnePostById(request.getPostId());
        if(user != null & post != null){
            Like likeToSave = new Like();
            likeToSave.setId(request.getId());
            likeToSave.setPost(post);
            likeToSave.setUser(user);
            return this.likeRepository.save(likeToSave);
        }else{
            return null;
        }
    }

    public void deleteOneLikeById(Long likeId) {
        this.likeRepository.deleteById(likeId);
    }
}
