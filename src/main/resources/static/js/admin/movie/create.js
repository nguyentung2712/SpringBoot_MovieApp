$('#form-create-movie').validate({

    // required data for movie
    rules: {
        title: {
            required: true
        },
        description: {
            required: true
        },
        director: {
            required: true
        },
        actor: {
            required: true
        },
        genre: {
            required: true
        },
        releaseYear: {
            required: true
        },
        status: {
            required: true
        },
        type: {
            required: true
        }
    },

    // messages announce when movie's data is empty
    messages: {
        title: {
            required: "Title is empty!"
        },
        description: {
            required: "Description is empty!"
        },
        director: {
            required: "Director is empty!"
        },
        actor: {
            required: "Actor is empty!"
        },
        genre: {
            required: "Genre is empty!"
        },
        releaseYear: {
            required: "Release Year is empty!"
        },
        status: {
            required: "Status is empty!"
        },
        type: {
            required: "Movie type is empty!"
        }
    },
    errorElement: 'span',
    errorPlacement: function (error, element) {
        error.addClass('invalid-feedback');
        element.closest('.form-group').append(error);
    },
    highlight: function (element, errorClass, validClass) {
        $(element).addClass('is-invalid');
    },
    unhighlight: function (element, errorClass, validClass) {
        $(element).removeClass('is-invalid');
    }
});

// Create movie
const titleEl = document.getElementById('title');
const descriptionEl = document.getElementById('description');
const directorEl = document.getElementById('director');
const actorEl = document.getElementById('actor');
const genreEl = document.getElementById('genre');
const releaseYearEl = document.getElementById('releaseYear');
const statusEl = document.getElementById('status');
const typeEl = document.getElementById('type');

const btnCreate = document.getElementById('btn-create');
btnCreate.addEventListener('click', function () {

    // if form-create-movie is not filled full => return
    if (!$('#form-create-movie').valid()) {
        return;
    }

    let selectedDirector = [];
    for(var i=0; i<directorEl.length; i++) {
        if(directorEl[i].selected) {
            selectedDirector.push(directorEl[i].value);
        }
    }

    let selectedActor = [];
    for(var i=0; i<actorEl.length; i++) {
        if(actorEl[i].selected) {
            selectedActor.push(actorEl[i].value);
        }
    }

    let selectedGenre = [];
    for(var i=0; i<genreEl.length; i++) {
        if(genreEl[i].selected) {
            selectedGenre.push(genreEl[i].value);
        }
    }

    // Send movie's data to database
    const data = {
        title: titleEl.value,
        description: descriptionEl.value,
        directorIds: selectedDirector,
        actorIds: selectedActor,
        genreIds: selectedGenre,
        releaseYear: releaseYearEl.value,
        status: statusEl.value,
        type: typeEl.value
    }

    // Using axios to send data to server
    axios.post('/api/admin/movies/create-movie', data)
        // announce after send data
            .then(function (response) {

                // announce when create success
                toastr.success('Create movie success')

                // go to location path
                setTimeout(function () { window.location.href = `/admin/movies/${response.data.id}/detail`}, 500) })

            .catch(function (error) {

                // send error announce when create fail
                console.log(error)
                toastr.error(error.response.data.message)
            })
})