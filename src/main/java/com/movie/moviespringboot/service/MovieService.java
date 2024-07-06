package com.movie.moviespringboot.service;

import com.movie.moviespringboot.entity.Episode;
import com.movie.moviespringboot.entity.Movie;
import com.movie.moviespringboot.exception.BadRequestException;
import com.movie.moviespringboot.exception.ResourceNotFoundException;
import com.movie.moviespringboot.repository.EpisodeRepository;
import com.movie.moviespringboot.repository.MovieRepository;
import com.movie.moviespringboot.model.request.UpsertMovieRequest;
import com.movie.moviespringboot.utils.StringUtils;
import com.movie.moviespringboot.utils.Validate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    @Autowired
    private final MovieRepository movieRepository;
    @Autowired
    private final DirectorService directorService;
    @Autowired
    private final ActorService actorService;
    @Autowired
    private final GenreService genreService;
    @Autowired
    private final EpisodeRepository episodeRepository;
    @Autowired
    private final EpisodeService episodeService;

    // Get all movies - Controller
    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    // Get movie by id - Controller
    public Movie getMovieById(Integer id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can't find movie has id is: "+id));
    }

    // Create Movie - Resource
    public Movie createMovie(UpsertMovieRequest request) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, request.getReleaseYear());
        Date date = calendar.getTime();

        if (request.getReleaseYear() < 1895) {
            throw new BadRequestException("Year release must be after or in 1895");
        }

        if(date.before(new Date()) || date.equals(new Date())){
            Movie movie = Movie.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .status(request.getStatus())
                    .type(request.getType())
                    .releaseYear(request.getReleaseYear())
                    .poster(StringUtils.generateLinkImage(request.getTitle()))
                    .directors(directorService.getListDirectorsByRequest(request.getDirectorIds()))
                    .actors(actorService.getListActorsByRequest(request.getActorIds()))
                    .genres(genreService.getListGenresByRequest(request.getGenreIds()))
                    .build();
            movieRepository.save(movie);

            episodeService.createFirstEpisode(movie.getId());

            return movie;
        }
        else {
            throw new RuntimeException("Year release must be before or in "+(new Date().getYear()+1900));
        }
    }

    // Update Movie - Resource
    public Movie updateMovie(Integer id, UpsertMovieRequest request) {

        // Find movie by id
        Movie movie = getMovieById(id);

        if (movie.getPoster().equals(StringUtils.generateLinkImage(movie.getTitle())) || movie.getPoster().isEmpty()) {
            movie.setPoster(StringUtils.generateLinkImage(request.getTitle()));
        }

        if (request.getReleaseYear() < 1895) {
            throw new BadRequestException("Year release must be after or in 1895");
        }

        // Update movie
        movie.setTitle(request.getTitle());
        movie.setDescription(request.getDescription());
        movie.setStatus(request.getStatus());
        movie.setType(movie.getType());

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, request.getReleaseYear());
        Date date = calendar.getTime();
        if(date.before(new Date()) || date.equals(new Date())){
            movie.setReleaseYear(request.getReleaseYear());
        }
        else {
            throw new RuntimeException("Year release must be before or in "+(new Date().getYear()+1900));
        }

        movie.setDirectors(directorService.getListDirectorsByRequest(request.getDirectorIds()));
        movie.setActors(actorService.getListActorsByRequest(request.getActorIds()));
        movie.setGenres(genreService.getListGenresByRequest(request.getGenreIds()));

        // Save movie after update
        return movieRepository.save(movie);
    }

    // Delete Movie - Resource
    public void deleteMovie(Integer id) {

        // Find movie by id
        Movie movie = getMovieById(id);

        // Check 2 conditions:
        // Movie has poster
        // Movie's poster isn't equal with "generateLinkImage(movie.getTitle())"
        // => delete image poster in "image_uploads" file
        if( movie.getPoster() != null && !movie.getPoster().equals(StringUtils.generateLinkImage(movie.getTitle())) ) {
            FileService.deleteFile(movie.getPoster());
        }

        List<Episode> episodeList = episodeRepository.findByMovie_Id(id);
        // Delete episode videoURL
        for (Episode episode : episodeList){
            if(episode.getVideoURL() != null){
                episodeService.deleteVideo(episode.getId());
            }
        }

        // Delete all episode
        episodeRepository.deleteAll(episodeList);

        // Delete movie
        movieRepository.delete(movie);
    }

    // Upload poster - Resource
    public String uploadPoster(Integer id, MultipartFile file) {

        // Check movie is existed or not
        Movie movie = getMovieById(id);

        // Upload file into
        String filePath = FileService.uploadFile(file);

        // Update path poster for movie
        movie.setPoster(filePath);
        movieRepository.save(movie);

        return filePath;
    }

    // Delete poster - Resource
    public void deletePoster(Integer id) {
        // Check condition: movie want to delete poster is existed
        Movie movie = getMovieById(id);

        // Check condition: default movie's poster can't be deleted
        if (movie.getPoster().equals(StringUtils.generateLinkImage(movie.getTitle()))) {
            throw new RuntimeException("Can not delete default movie's poster");
        }

        FileService.deleteFile(movie.getPoster());
        movie.setPoster(StringUtils.generateLinkImage(movie.getTitle()));
        movieRepository.save(movie);
    }
}
