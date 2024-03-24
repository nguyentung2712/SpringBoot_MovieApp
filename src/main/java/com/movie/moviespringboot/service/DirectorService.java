package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Director;
import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.repository.DirectorRepository;
import com.movie.moviespringboot.model.request.UpsertDirectorRequest;
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
public class DirectorService {
    private final DirectorRepository directorRepository;
    private final MovieRepository movieRepository;

    // Get all directors
    public List<Director> getAllDirectors() {
        return directorRepository.findAll();
    }

    // Get director by id
    public Director getDirectorById(Integer id) {
        return directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find director has id is: "+id));
    }

    // Create Director - Resource
    public Director createDirector(UpsertDirectorRequest request) {
        if(request.getBirthday().before(new Date())){
            Director director = Director.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .birthday(request.getBirthday())
                    .avatar(StringUtils.generateLinkImage(request.getName()))
                    .build();
            return directorRepository.save(director);
        }
        else {
            throw new RuntimeException("Birthdate must be before "+new Date());
        }
    }

    // Update Director
    public Director updateDirector(Integer id, UpsertDirectorRequest request) {

        // Find director by id
        Director director = getDirectorById(id);

        // Update director
        director.setName(request.getName());
        director.setDescription(request.getDescription());
        if(request.getBirthday().before(new Date())){
            director.setBirthday(request.getBirthday());
        }
        else {
            throw new RuntimeException("Birthdate must be before "+new Date());
        }

        return directorRepository.save(director);
    }

    // Delete Director
    public void deleteDirector(Integer id) {

        // Find director by id
        Director director = getDirectorById(id);

        // Check condition that director doesn't exist in any movie
        if(movieRepository.findAll().stream().noneMatch(movie -> movie.getDirectors().stream().anyMatch(directorMatch -> directorMatch.equals(director)))){
            // Check 2 conditions:
            // Director has avatar
            // Director's avatar isn't equal with "generateLinkImage(director.getName())"
            // => delete image avatar in "image_uploads" file
            if (director.getAvatar() != null && !director.getAvatar().equals(StringUtils.generateLinkImage(director.getName())) ) {
                FileService.deleteFile(director.getAvatar());
            }
            // Delete Director
            directorRepository.delete(director);
        }
        else {
            throw new RuntimeException("Can not delete this director");
        }

    }

    // Upload avatar - Resource
    public String uploadAvatar(Integer id, MultipartFile file) {
        // Check director is existed or not
        Director director = getDirectorById(id);

        // Upload file into
        String filePath = FileService.uploadFile(file);

        // Update path avatar for director
        director.setAvatar(filePath);
        directorRepository.save(director);

        return filePath;
    }

    // Delete avatar - Resource
    public void deleteAvatar(Integer id) {
        // Check director is existed or not
        Director director = directorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find director has id is: "+id));

        // If director's avatar is not equal with default avatar => delete
        if(!director.getAvatar().equals(StringUtils.generateLinkImage(director.getAvatar()))){
            FileService.deleteFile(director.getAvatar());
            director.setAvatar(StringUtils.generateLinkImage(director.getName()));
            directorRepository.save(director);
        } else {
            throw new RuntimeException("Can not delete this avatar");
        }
    }

    // Get list directors by request - MovieService
    public List<Director> getListDirectorsByRequest(List<Integer> directorIds) {
        // Create list directors were chosen by request
        List<Director> directorList = new ArrayList<>();

        // Get director id by request using for each
        for ( int id : directorIds){
            // each every id will match with 1 id of director in directors list in database
            directorList.addAll(directorRepository.findAll()
                    .stream()
                    // check condition then add to directors list was create
                    .filter(director -> director.getId() == id).toList());
        }

        return directorList;
    }
}
