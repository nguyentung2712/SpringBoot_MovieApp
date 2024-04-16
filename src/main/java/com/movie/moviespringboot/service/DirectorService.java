package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Director;
import com.movie.moviespringboot.exception.BadRequestException;
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

    // Create Director
    public Director createDirector(UpsertDirectorRequest request) {
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

        Director director = Director.builder()
                .name(request.getName())
                .description(request.getDescription())
                .birthday(request.getBirthday())
                .avatar(StringUtils.generateLinkImage(request.getName()))
                .build();
        return directorRepository.save(director);
    }

    // Update director
    public Director updateDirector(Integer id, UpsertDirectorRequest request) {
        // Check condition: director want to update is existed
        Director director = getDirectorById(id);

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
        if (director.getAvatar().equals(StringUtils.generateLinkImage(director.getName())) || director.getAvatar().isEmpty()) {
            director.setAvatar(StringUtils.generateLinkImage(request.getName()));
        }

        director.setName(request.getName());
        director.setBirthday(request.getBirthday());
        director.setDescription(request.getDescription());

        return directorRepository.save(director);
    }

    // Delete Director
    public void deleteDirector(Integer id) {
        // Check condition: director want to delete is existed
        Director director = getDirectorById(id);

        // Check condition: director doesn't exist in any movie
        if(movieRepository.findAll().stream().noneMatch(movie -> movie.getDirectors().stream().anyMatch(directorMatch -> directorMatch.equals(director)))){
            // Check 2 conditions:
            // - director has avatar
            // - director's avatar isn't equal with "generateLinkImage(director.getName())"
            // => delete image avatar in "image_uploads" file
            if (director.getAvatar() != null && !director.getAvatar().equals(StringUtils.generateLinkImage(director.getName())) ) {
                FileService.deleteFile(director.getAvatar());
            }
            directorRepository.delete(director);
        }
        else {
            throw new RuntimeException("This director still existed in some movies, Can't delete this director");
        }

    }

    // Upload avatar
    public String uploadAvatar(Integer id, MultipartFile file) {
        // Check condition: director want to upload avatar is existed
        Director director = getDirectorById(id);

        // Upload file into
        String filePath = FileService.uploadFile(file);

        // Update path avatar for director
        director.setAvatar(filePath);
        directorRepository.save(director);

        return filePath;
    }

    // Delete avatar
    public void deleteAvatar(Integer id) {
        // Check condition: director want to delete avatar is existed
        Director director = getDirectorById(id);

        // Check condition: director's avatar is different with default avatar
        if(director.getAvatar().equals(StringUtils.generateLinkImage(director.getName()))) {
            throw new BadRequestException("Can not delete default avatar");
        }

        FileService.deleteFile(director.getAvatar());
        director.setAvatar(StringUtils.generateLinkImage(director.getName()));
        directorRepository.save(director);
    }

    // Get list directors by request
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
