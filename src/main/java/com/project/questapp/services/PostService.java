package com.project.questapp.services;


import com.project.questapp.entities.Like;
import com.project.questapp.entities.Post;
import com.project.questapp.entities.User;
import com.project.questapp.repos.PostRepository;
import com.project.questapp.requests.PostCreateRequest;
import com.project.questapp.requests.PostUpdateRequest;
import com.project.questapp.responses.LikeResponse;
import com.project.questapp.responses.PostResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;
    private UserService userService;
    private LikeService likeService; // constructor ile bağlayınca birbirini çağırıyor

    public PostService(PostRepository postRepository,UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;

    }
    @Autowired
    public void setLikeService(LikeService likeService){
        this.likeService = likeService;
    }

    public List<PostResponse> getAllPosts(Optional<Long> userId) { // optional methods --> ispresent, get
        List<Post> list; // we are getting the post lists from db and mapping to the postResponse object and that way we can not get the password
        if(userId.isPresent()){
            list = postRepository.findByUserId(userId.get());
            //return list.stream().map(p -> new PostResponse(p,likes)).collect(Collectors.toList());
        }else{
            list = postRepository.findAll();}
            return list.stream().map(p-> {
            List<LikeResponse> likes = likeService.getAllLikesWithParam(userId,Optional.of(p.getId()));
            return new PostResponse(p,likes);}).collect(Collectors.toList());
        }



    public Post getOnePostById(Long postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
        User user = userService.getOneUserById(newPostRequest.getUserId());
        if(user == null){
            return null;
        }
        else{

            Post toSave = new Post();
            toSave.setId(newPostRequest.getId());
            toSave.setText(newPostRequest.getText());
            toSave.setTitle(newPostRequest.getTitle());
            toSave.setUser(user);
            return postRepository.save(toSave);
        }
    }

    public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
        Optional<Post> post = this.postRepository.findById(postId);
        if(post.isPresent()){
             Post toUpdate = post.get();
             toUpdate.setText(updatePost.getText());
             toUpdate.setTitle(updatePost.getTitle());
             this.postRepository.save(toUpdate);
             return toUpdate;
        }else{
            return null;
        }
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    public Post createOnePost2(Post post) {
        return this.postRepository.save(post);
    }

    public List<Post> getAllPosts2() {
        return this.postRepository.findAll();
    }
}
