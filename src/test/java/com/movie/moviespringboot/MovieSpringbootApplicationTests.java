package com.movie.moviespringboot;

import com.github.javafaker.Faker;
import com.github.slugify.Slugify;
import com.movie.moviespringboot.entity.*;
import com.movie.moviespringboot.model.enums.MovieType;
import com.movie.moviespringboot.model.enums.UserRole;
import com.movie.moviespringboot.repository.*;
import com.movie.moviespringboot.utils.FileUtils;
import com.movie.moviespringboot.utils.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootTest
class MovieSpringbootApplicationTests {

	@Autowired
	private GenreRepository genreRepository;

	@Autowired
	private ActorRepository actorRepository;

	@Autowired
	private DirectorRepository directorRepository;

	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BlogRepository blogRepository;

	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EpisodeRepository episodeRepository;


	// fake genre value and save to genre repository
	@Test
	void save_all_genre() {
		// Fake data using Faker
		Faker faker = new Faker();

		// create 10 genres using for loop
		for (int i = 0; i < 10; i++) {
			Genre genre = Genre.builder()
					.name(faker.name().username())
					.build();
			genreRepository.save(genre); // save to database
		}
	}

	// fake actor value and save to actor repository
	@Test
	void save_all_actor(){
		Faker faker = new Faker(); // Faker data
		for (int i = 0; i < 50; i++) {
			String name = faker.name().fullName();
			Actor actor = Actor.builder()
					.name(name)
					.birthday(faker.date().birthday())
					.description(faker.lorem().paragraph())
					.avatar(StringUtils.generateLinkImage(name))
					.build();
			actorRepository.save(actor); // save to database
		}
	}

	// fake director value and save to director repository
	@Test
	void save_all_director(){
		Faker faker = new Faker(); // Faker data
		for (int i = 0; i < 50; i++) {
			String name = faker.name().fullName();
			Director director = Director.builder()
					.name(name)
					.birthday(faker.date().birthday())
					.description(faker.lorem().paragraph())
					.avatar(StringUtils.generateLinkImage(name))
					.build();
			directorRepository.save(director); // save to database
		}
	}

	// fake movie value and save to movie repository
	@Test
	void save_all_movie() {
		List<Genre> genreList = genreRepository.findAll();
		List<Actor> actorList = actorRepository.findAll();
		List<Director> directorList = directorRepository.findAll();

		Faker faker = new Faker(); // Faker data
		Slugify slugify = Slugify.builder().build(); // Generate slug


		for (int i = 0; i < 100; i++) {
			// Random 1 -> 4 genre
			List<Genre> rdGenreList = new ArrayList<>();
			for (int j = 0; j < faker.number().numberBetween(1,5); j++) {
				Genre rdGenre = genreList.get(faker.number().numberBetween(0,genreList.size()));
				if(!rdGenreList.contains(rdGenre)) {
					rdGenreList.add(rdGenre);
				}
			}

			// Random 5 -> 10 actor
			List<Actor> rdActorList = new ArrayList<>();
			for (int j = 0; j < faker.number().numberBetween(5,11); j++) {
				Actor rdActor = actorList.get(faker.number().numberBetween(0,actorList.size()));
				if(!rdActorList.contains(rdActor)) {
					rdActorList.add(rdActor);
				}
			}

			// Random 1 -> 3 director
			List<Director> rdDirectorList = new ArrayList<>();
			for (int j = 0; j < faker.number().numberBetween(1,4); j++) {
				Director rdDirector = directorList.get(faker.number().numberBetween(0,directorList.size()));
				if(!rdDirectorList.contains(rdDirector)) {
					rdDirectorList.add(rdDirector);
				}
			}

			String title = faker.book().title();

			// save status value to build date created, date updated, date published
			boolean status = faker.bool().bool();

			Date createdAt = faker.date().birthday();
			Date publishedAt = null;

			// check condition for date create, update and publish
            if(status) {
                publishedAt = createdAt;
			}

            Movie movie = Movie.builder()
					// title
					.title(title)
					// slug
					.slug(slugify.slugify(title))
					// description
					.description(faker.lorem().paragraph())
					// poster: using logo
					.poster(StringUtils.generateLinkImage(title))
					// type: random 1 of 3 type movies
					.type(MovieType.values()[faker.number().numberBetween(0,2)])
					// year release
					.releaseYear(faker.number().numberBetween(2018,2023))
					// status: public or not (true or false)
					.status(status)
					// rating form 1 to 10
					.rating(faker.number().numberBetween(1,10))
					// viewer
					.view(faker.number().numberBetween(100,1000))
					.createdAt(createdAt)
					.updatedAt(createdAt)
					.publishedAt(publishedAt)
					.genres(rdGenreList)
					.actors(rdActorList)
					.directors(rdDirectorList)
					.build();
			movieRepository.save(movie); // Save to database
		}
	}

	// fake episode and save to episode repository
	@Test
	void save_all_episode() {
		Random random = new Random();
		List<Movie> movieList = movieRepository.findAll();

		for(Movie movie : movieList) {
			// Check what type of movie to create 1 or more than 1 episode
			// If movie type is series movie => create 5 -> 10 episodes using for loop
			if(movie.getType().equals(MovieType.PHIM_BO)) {
				for(int i = 0; i < random.nextInt(5) + 5; i++) {
					Episode episode = Episode.builder()
							.name("Episode " + (i + 1))
							.displayOrder(i + 1)
							.status(true)
							.createdAt(new Date())
							.updatedAt(new Date())
							.publishedAt(new Date())
							.movie(movie)
							.build();
					episodeRepository.save(episode);
				}
			} else {
				// If movie type is Single Movie or Theater Movie => create only 1 episode
				Episode episode = Episode.builder()
						.name("Episode full")
						.displayOrder(1)
						.status(true)
						.createdAt(new Date())
						.updatedAt(new Date())
						.publishedAt(new Date())
						.movie(movie)
						.build();
				episodeRepository.save(episode);
			}
		}
	}

	// fake user value and save to user repository
	@Test
	void save_all_user() {
		// Fake data using Faker
		Faker faker = new Faker();

		for (int i = 0; i < 20; i++) {
			String name = faker.name().fullName();
			User user = User.builder()
					.name(name)
					.email(faker.internet().emailAddress())
					.password(passwordEncoder.encode("123"))
					.avatar(StringUtils.generateLinkImage(name))
					.role(i == 0 || i == 1 ? UserRole.ADMIN : UserRole.USER)
					.build();

			userRepository.save(user); // Save to database
		}
	}

	// fake blog value and save to blog repository
	@Test
	void save_all_blog() {
		List<User> userList = userRepository.findByRole(UserRole.ADMIN);

		Faker faker = new Faker();
		Random random = new Random();
		Slugify slugify = Slugify.builder().build();

		for (int i = 0; i < 20; i++) {
			// Random 1 user
			User rdUser = userList.get(random.nextInt(userList.size()));

			boolean status = faker.bool().bool();
			Date createdAt = new Date();
			Date publishedAt = null;

			if (status) {
				publishedAt = createdAt;
			}

			// create blog
			String title = faker.book().title();
			Blog blog = Blog.builder()
					.title(title)
					.slug(slugify.slugify(title))
					.description(faker.lorem().paragraph())
					.content(faker.lorem().paragraph())
					.thumbnail(StringUtils.generateLinkImage(title))
					.status(status)
					.createdAt(createdAt)
					.updatedAt(createdAt)
					.publishedAt(publishedAt)
					.user(rdUser)
					.build();
			blogRepository.save(blog);
		}
	}

	// fake review value and save to review repository
	@Test
	void save_all_review() {
		List<User> userList = userRepository.findByRole(UserRole.USER);
		List<Movie> movieList = movieRepository.findAll();

		Faker faker = new Faker(); // Fake data
		Random random = new Random();

		for (Movie movie : movieList){
			// Each movie has 5 -> 10 review
			// random.nextInt(5) + 5: random number from 0 -> 5 then plus 5
			for (int i = 0; i < random.nextInt(5) + 5; i++) {
				// Random 1 user
				User rdUser = userList.get(random.nextInt(userList.size()));

				// Create review
				Review review = Review.builder()
						.content(faker.lorem().paragraph())
						.rating(random.nextInt(10) + 1)
						.createdAt(new Date())
						.updatedAt(new Date())
						.movie(movie)
						.user(rdUser)
						.build();
				reviewRepository.save(review);
			}
		}
	}

	// check movie value
	@Test
	void test_method() {
		List<Movie> movies = movieRepository.findAll();
		System.out.println(movies.size());

		// Select
		List<Movie> movies1 = movieRepository.findAllById(List.of(1,2,3,1000));
		System.out.println(movies1.size());

		Movie movie = movieRepository.findById(1).orElse(null);
		System.out.println(movie);

		// Update
        assert movie != null;
        movie.setTitle("John Wick");
		movieRepository.save(movie);

		// Delete
		movieRepository.delete(movie); // delete by object
		movieRepository.deleteById(2); // delete by id

	}
}
