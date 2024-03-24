package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Actor;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.repository.ActorRepository;
import com.movie.moviespringboot.model.request.UpsertActorRequest;
import com.movie.moviespringboot.repository.MovieRepository;
import com.movie.moviespringboot.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ActorService {
    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    // Get all actors
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    // Get actor by id
    public Actor getActorById(Integer id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find actor has id is: "+id));
    }

    // Create actor
    public Actor createActor(UpsertActorRequest request) {
        if(request.getBirthday().before(new Date())){
            Actor actor = Actor.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .birthday(request.getBirthday())
                    .avatar(StringUtils.generateLinkImage(request.getName()))
                    .build();
            return actorRepository.save(actor);
        }
        else {
            throw new RuntimeException("Birthdate must be before "+new Date());
        }
    }

    // Update Actor
    public Actor updateActor(Integer id, UpsertActorRequest request) {

        // Find actor by id
        Actor actor = getActorById(id);

        // Update actor
        actor.setName(request.getName());
        actor.setDescription(request.getDescription());
        if (request.getBirthday().before(new Date())){
            actor.setBirthday(request.getBirthday());
        }
        else {
            throw new RuntimeException("Birthdate must be before "+new Date());
        }
        return actorRepository.save(actor);
    }

    // Delete Actor
    public void deleteActor(Integer id) {

        Actor actor = getActorById(id);

        // Check condition that actor doesn't exist in any movie
        if(movieRepository.findAll().stream().noneMatch(movie -> movie.getActors().stream().anyMatch(actorMatch -> actorMatch.equals(actor)))){
            // Check 2 conditions:
            // Actor has avatar
            // Actor's avatar isn't equal with "generateLinkImage(actor.getName())"
            // => delete image avatar in "image_uploads" file
            if (actor.getAvatar() != null && !actor.getAvatar().equals(StringUtils.generateLinkImage(actor.getName())) ) {
                FileService.deleteFile(actor.getAvatar());
            }
            actorRepository.delete(actor);
        }
        else {
            throw new RuntimeException("This actor still existed in some movies, Can't delete this actor");
        }
    }

    // Upload avatar
    public String uploadAvatar(Integer id, MultipartFile file) {
        Actor actor = getActorById(id);

        // Upload file into
        String filePath = FileService.uploadFile(file);

        // Update path avatar for actor
        actor.setAvatar(filePath);
        actorRepository.save(actor);

        return filePath;
    }

    // Delete avatar
    public void deleteAvatar(Integer id) {
        Actor actor = getActorById(id);

        // If actor's avatar is not equal with default avatar => delete
        if(!actor.getAvatar().equals(StringUtils.generateLinkImage(actor.getAvatar()))){
            FileService.deleteFile(actor.getAvatar());
            actor.setAvatar(StringUtils.generateLinkImage(actor.getName()));
            actorRepository.save(actor);
        } else {
            throw new RuntimeException("Can not delete default avatar");
        }
    }

    // Get list actors by request
    public List<Actor> getListActorsByRequest(List<Integer> actorIds) {
        // Create list actors were chosen by request
        List<Actor> actorList = new ArrayList<>();

        // Get actor id by request using for each
        for (int id : actorIds){
            // each every id will match with 1 id of actor in actors list in database
            actorList.addAll(actorRepository.findAll()
                    .stream()
                    // check condition then add to actors list was create
                    .filter(actor -> actor.getId() == id).toList());
        }
        return actorList;
    }
}
