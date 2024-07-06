// Required and Messages
$('#form-update-movie').validate({
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
    messages: {
        title: {
            required: "Title is empty"
        },
        description: {
            required: "Description is empty"
        },
        director: {
            required: "Director is empty"
        },
        actor: {
            required: "Actor is empty"
        },
        genre: {
            required: "Genre is empty"
        },
        releaseYear: {
            required: "Release Year is empty"
        },
        status: {
            required: "Status is empty"
        },
        type: {
            required: "Movie type is empty"
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

// Year release drop down options
document.addEventListener('DOMContentLoaded', (event) => {
    const releaseYearEl = document.getElementById('releaseYear');
    const currentYear = new Date().getFullYear();
    const startYear = 1895;

    for (let year = currentYear; year >= startYear; year--) {
        let option = document.createElement('option');
        option.value = year;
        option.text = year;
        releaseYearEl.appendChild(option);
    }
});

// Update movie
const titleEl = document.getElementById('title');
const descriptionEl = document.getElementById('description');
const directorEl = document.getElementById('director');
const actorEl = document.getElementById('actor');
const genreEl = document.getElementById('genre');
const releaseYearEl = document.getElementById('releaseYear');
const statusEl = document.getElementById('status');
const typeEl = document.getElementById('type');
const btnUpdate = document.getElementById('btn-update');
btnUpdate.addEventListener('click', function () {
    if (!$('#form-update-movie').valid()) {
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

    // Send data to server
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
    axios.put(`/api/admin/movies/${movie.id}/update-movie`, data)
        .then(function (response) {
            toastr.success('Update success')
            setTimeout(function () { location.reload(); }, 500)
        })
        .catch(function (error) {
            console.log(error)
            toastr.error(error.response.data.message)
        })
})

// Delete movie
const btnDelete = document.getElementById('btn-delete')
btnDelete.addEventListener('click', function () {
    const isConfirm = confirm('Are you sure you want to delete this movie?')
    if (!isConfirm) {
        return
    }
    axios.delete(`/api/admin/movies/${movie.id}/delete-movie`, movie.id)
        .then(function (response) {
            toastr.success('Delete success')
            setTimeout(function () { window.location.href = '/admin/movies/homePage' }, 1500)
        })
        .catch(function (error) {
            console.log(error)
            toastr.error(error.response.data.message)
        })
})

// Upload poster
const imagePreview = document.getElementById('image-preview')
const imageInput = document.getElementById('image')
imageInput.addEventListener("change", (e) => {
    // Get file was chosen
    const file = e.target.files[0]

    // Create object form data to send data
    const formData = new FormData()
    formData.append('file', file)

    // Call api using axios
    axios.post(`/api/admin/movies/${movie.id}/upload-poster`, formData)
        .then(res => {
            // Show image was uploaded
            imagePreview.src = res.data

            toastr.success('Upload poster success.')
        })
        .catch(err => {
            console.log(err)
            toastr.error(err.response.data.message)
        })
})

// Delete poster
const btnDeletePoster = document.getElementById('delete-poster');
btnDeletePoster.addEventListener('click', function () {
    const isConfirm = confirm('Are you sure you want to delete this poster?')
      if (!isConfirm) {
          return
      }
      // Call api using axios
      axios.delete(`/api/admin/movies/${movie.id}/delete-poster`, movie.id)
        .then(function (response) {
          toastr.success('Delete poster success')
          setTimeout(function () { location.reload() }, 1500)
        })
        .catch(function (error) {
          toastr.error(error.response.data.message)
        })
})

// Create episode for SeriesMovie
const createSeriesEpisode = (event, episodeId) => {
  // Call api using axios
  axios.post(`/api/admin/episodes/${episodeId}/create-episode-series-movie`)
    .then(res => {
      toastr.success('Create episode success')
      setTimeout(() => {
          location.reload()
      }, 500)
    })
    .catch(err => {
      toastr.error(err.response.data.message)
    })
}

// Upload episode
const uploadVideo = (event, episodeId) => {
  //  Get file is chose
  const file = event.target.files[0]
  // Create formData to send to data
  const formData = new FormData()
  formData.append('video', file)
  // Call api using axios
  axios.post(`/api/admin/episodes/${episodeId}/upload-video`, formData)
    .then(res => {
      toastr.success('Upload video success')

      // Reload page after 1.5s
      setTimeout(() => {
          location.reload()
      }, 1500)
    })
    .catch(err => {
      console.log(err)
      toastr.error(err.response.data.message)
    })
}

// Preview video
const previewVideo = (episodeId) => {
  // Find episode by id
  const episode = episodeList.find(e => e.id === episodeId)

  // setting source src = episode.videoURL
  const videoEl = document.querySelector('#modal-preview-video video')
  videoEl.src = episode.videoURL

  // Show modal
  $('#modal-preview-video').modal('show')
}
// When close modal video will reset source src = empty
$('#modal-preview-video').on('hidden.bs.modal', function (event) {
        const videoEl = document.querySelector('#modal-preview-video video')
        videoEl.src = "";
})

// Delete video
const deleteVideo = (event, episodeId) => {
  const isConfirm = confirm('Are you sure you want to delete this video?')
  if (!isConfirm) {
      return
  }
  // Call api using axios
  axios.delete(`/api/admin/episodes/${episodeId}/delete-video`)
      .then(res => {
          toastr.success('Delete video success')
          // Reload page after 0.5s
          setTimeout(() => {
              location.reload()
          }, 500)
  })
  .catch(err => {
      console.log(err)
      toastr.error(err.response.data.message)
  })
}

// Delete episode
const deleteEpisode = (event, episodeId) => {
  const isConfirm = confirm('Are you sure you want to delete this episode?')
  if (!isConfirm) {
      return
  }
  // Call api using axios
  axios.delete(`/api/admin/episodes/${episodeId}/delete-episode`)
      .then(res => {
          toastr.success('Delete episode success')

          // Reload page after 0.5s
          setTimeout(() => {
              location.reload()
          }, 500)
  })
  .catch(err => {
      console.log(err)
      toastr.error(err.response.data.message)
  })
}