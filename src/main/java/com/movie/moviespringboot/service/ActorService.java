package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Actor;
import com.movie.moviespringboot.exception.BadRequestException;
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
        // Check condition: birthday reasonable
        if (request.getBirthday().getYear() == new Date().getYear()) {
            if ((request.getBirthday().getMonth() == new Date().getMonth()) && (request.getBirthday().getDate() > new Date().getDate())) {
                throw new BadRequestException("Birthday must be before or on "+ new Date().getDate());
            }
            if (request.getBirthday().getMonth() > new Date().getMonth()) {
                throw new BadRequestException("Birthday month must be before or in "+ (new Date().getMonth() + 1));
            }
        }

        if (request.getBirthday().getYear() > (new Date().getYear())) {
            throw new BadRequestException("Birthday year must be before or in "+ (new Date().getYear() + 1900));
        }

        Actor actor = Actor.builder()
                .name(request.getName())
                .description(request.getDescription())
                .birthday(request.getBirthday())
                .avatar(StringUtils.generateLinkImage(request.getName()))
                .build();
        return actorRepository.save(actor);
    }

    // Update actor
    public Actor updateActor(Integer id, UpsertActorRequest request) {
        // Check condition: actor want to update info is existed
        Actor actor = getActorById(id);

        // Check condition: birthday reasonable
        if (request.getBirthday().getYear() == new Date().getYear()) {
            if ((request.getBirthday().getMonth() == new Date().getMonth()) && (request.getBirthday().getDate() > new Date().getDate())) {
                throw new BadRequestException("Birthday must be before or on "+ new Date().getDate());
            }
            if (request.getBirthday().getMonth() > new Date().getMonth()) {
                throw new BadRequestException("Birthday month must be before or in "+ (new Date().getMonth() + 1));
            }
        }

        if (request.getBirthday().getYear() > (new Date().getYear())) {
            throw new BadRequestException("Birthday year must be before or in "+ (new Date().getYear() + 1900));
        }

        // Check condition: change name => change avatar if avatar as default
        if (actor.getAvatar().equals(StringUtils.generateLinkImage(actor.getName())) || actor.getAvatar().isEmpty()) {
            actor.setAvatar(StringUtils.generateLinkImage(request.getName()));
        }

        actor.setName(request.getName());
        actor.setDescription(request.getDescription());
        actor.setBirthday(request.getBirthday());

        return actorRepository.save(actor);
    }

    // Delete actor
    public void deleteActor(Integer id) {
        // Check condition: actor want to delete is existed
        Actor actor = getActorById(id);

        // Check condition: actor doesn't exist in any movie
        if(movieRepository.findAll().stream().noneMatch(movie -> movie.getActors().stream().anyMatch(actorMatch -> actorMatch.equals(actor)))){
            // Check 2 conditions:
            // - actor has avatar
            // - actor's avatar isn't equal with "generateLinkImage(actor.getName())"
            // => delete image avatar in "image_uploads" file
            if (actor.getAvatar() != null && !actor.getAvatar().equals(StringUtils.generateLinkImage(actor.getName())) ) {
                FileService.deleteFile(actor.getAvatar());
            }
            actorRepository.delete(actor);
        }
        else {
            throw new BadRequestException("This actor still existed in some movies, Can't delete this actor");
        }
    }

    // Upload avatar
    public String uploadAvatar(Integer id, MultipartFile file) {
        // Check condition: actor want to upload avatar is existed
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
        // Check condition: actor want to delete avatar is existed
        Actor actor = getActorById(id);

        // Check condition: default actor's avatar can't be deleted
        if(actor.getAvatar().equals(StringUtils.generateLinkImage(actor.getName()))){
            throw new BadRequestException("Can not delete default avatar");
        }

        FileService.deleteFile(actor.getAvatar());
        actor.setAvatar(StringUtils.generateLinkImage(actor.getName()));
        actorRepository.save(actor);
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
