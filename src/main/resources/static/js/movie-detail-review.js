// Rating star
const stars = document.querySelectorAll(".star");
const ratingValue = document.getElementById("rating-value");

let currentRating = null;

stars.forEach((star) => {
   star.addEventListener("mouseover", () => {
      resetStars();
      const rating = parseInt(star.getAttribute("data-rating"));
      highlightStars(rating);
   });

   star.addEventListener("mouseout", () => {
      resetStars();
      highlightStars(currentRating);
   });

   star.addEventListener("click", () => {
      currentRating = parseInt(star.getAttribute("data-rating"));
      ratingValue.textContent = `You are rating ${currentRating} stars.`;
      highlightStars(currentRating);
   });
});

function resetStars() {
   stars.forEach((star) => {
      star.classList.remove("active");
   });
}

function highlightStars(rating) {
   stars.forEach((star) => {
      const starRating = parseInt(star.getAttribute("data-rating"));
      if (starRating <= rating) {
         star.classList.add("active");
      }
   });
}

// Render reviews
// Format date/time
const formatDate = (dateString) => {
    let date = new Date(dateString)
    let year = date.getFullYear();
    // month in javascript from 0 to 11
    // transfer number to string then cut 2 value form the last of string
    // Ex: 01 -> 01; 012 -> 12
    let month = `0${date.getMonth() + 1}`.slice(-2);
    let day = `0${date.getDate()}`.slice(-2);
    return `${day}/${month}/${year}`;

}

const renderReviews = (reviews) => {
    // using map => return an array
    const htmlReviews = reviews.map((review) => {
        return `
            <div class="rating-item d-flex align-items-center mb-3">
                <div class="rating-avatar">
                    <img src="${review.user.avatar}" alt="${review.user.name}">
                </div>

                <div class="rating-info ms-3">
                    <div class="d-flex align-items-center">
                        <p class="rating-name mb-0">
                            <strong>${review.user.name}</strong>
                        </p>
                        <p class="rating-time mb-0 ms-2 text-muted fts-italic">${formatDate(review.updatedAt)}</p>
                    </div>
                    <div class="rating-star">
                        <p class="mb-0 fw-bold">
                            <span class="rating-icon"><i class="fa fa-star"></i></span>
                            <span>${review.rating}/10 ${review.ratingText}</span>
                        </p>
                    </div>
                    <div class="rating-content mb-0 mt-1 text-muted">${review.content}</div>

                    ${currentUser && currentUser.id === review.user.id || currentUser && currentUser.role.value === "ROLE_ADMIN"
                        ? `<div class="rating-action mt-2">
                              <a href="javascript:void(0)" class="text-primary text-decoration-underline me-2" onclick="openModalUpdate(${review.id})">Edit</a>
                              <a href="javascript:void(0)" class="text-danger text-decoration-underline" onclick="deleteReview(${review.id})">Delete</a>
                        </div>` : ''
                    }

                </div>
            </div>
        `
    // join become a string
    }).join('')

    document.querySelector('.review-list').innerHTML = htmlReviews
}

// HANDLES ABOUT REVIEW: DELETE, CREATE, UPDATE
let reviewIdUpdated = null;

// 1. Delete review
const deleteReview = (reviewId) => {
     // ask user want to delete or not?
    const isConfirmed = window.confirm('Are you sure you wanna delete this review?')
    if(!isConfirmed) return
    // Call API (fetch, axios, ajax, ...)
    const url = `/api/reviews/${reviewId}/delete-review`
    // delete review using call API
    axios.delete(url).then(function (response) {
            // using toastr library to announce user
            toastr.success('Delete review success.')

            // Delete review has id in reviews list
            reviews = reviews.filter(review => review.id !== reviewId)
            // render reviews
            renderReviews(reviews)

        }).catch(function (error) {
            console.log(error);
            // using toastr library to announce user
            toastr.error(error.response.data.message)
        });
}

const btnOpenModalReview = document.getElementById('btn-open-modal-review')
const modalReview = new bootstrap.Modal(document.getElementById('modal-review'), {
    keyboard: false
})

// Button open modal review
btnOpenModalReview.addEventListener('click',function() {
    // Open modal
    modalReview.show();
    // Change title modal
    document.getElementById("modal-title").innerHTML = "Write review"
})

// Reset information review when modal review close
document.getElementById('modal-review').addEventListener('hidden.bs.modal',function(event) {
    resetStars();
    // reset rating value
    ratingValue.textContent = "";
    // reset rating content
    ratingContentEl.value = "";
    // reset review update
    reviewIdUpdated = null;
    // current rating
    currentRating = 0;
})

// Button handle review
const btnHandleReview = document.getElementById('btn-handle-review')
const ratingContentEl = document.getElementById('rating-content')
btnHandleReview.addEventListener('click', function() {
    if(reviewIdUpdated) {
        // Update review
        updateReview()
    } else{
        // Create new Review
        createReview()
    }
})

// 2. Create new review
const createReview = () => {
    // Check user is rating or not
    if(!currentRating) {
        toastr.error('You have not rating yet')
        return;
    }

    // Check user is comment content or not
    if(!ratingContentEl.value.trim()) {
        toastr.error('You have not comment content yet')
        return;
    }

    // Create data to send to server
    const data = {
        rating: currentRating,
        content: ratingContentEl.value,
        movieId: movie.id
    }

    // Call API to create new content using AXIOS
    axios.post('/api/reviews/create-review', data)
        .then(function(response) {
            // Announce user that review was created success
            toastr.success('Create review success')
            // Hide modal review
            modalReview.hide()

            // Add review to reviews list
            reviews.unshift(response.data)
            renderReviews(reviews)
    })
    .catch(function(error) {
        console.log(error)
        toastr.error(error.response.data.message)
    })
}

// 3. Update review
const openModalUpdate = id => {
    // Find review want to update
    const review = reviews.find(review => review.id === id)

    // Change title modal review
    document.getElementById("modal-title").innerHTML = "Update review";

    // Update value review
    // rating star
    resetStars();
    currentRating = review.rating;
    ratingValue.textContent = `You are rating ${currentRating} stars.`;
    highlightStars(currentRating);
    // content
    ratingContentEl.value = review.content
    // update review id
    reviewIdUpdated = review.id

    // Open modal
    modalReview.show();
}
const updateReview = () => {
    // Check user is rating or not
    if(!currentRating) {
        toastr.error('You have not rating yet')
        return;
    }

    // Check user is comment content or not
    if(!ratingContentEl.value.trim()) {
        toastr.error('You have not comment content yet')
        return;
    }

    // Create data to send to server
    const data = {
        rating: currentRating,
        content: ratingContentEl.value,
        movieId: movie.id
    }

    // Call API to update content using AXIOS
    axios.put(`/api/reviews/${reviewIdUpdated}/update-review`, data)
        .then(function(response) {
            // Announce user that review was created success
            toastr.success('Update review success')
            // Hide modal review
            modalReview.hide()

            // Update review in reviews list
            // Find review by id then replace the old review to new one
            const index = reviews.findIndex(review => review.id === reviewIdUpdated)
            reviews[index] = response.data

            // Render reviews list
            renderReviews(reviews)
    })
    .catch(function(error) {
        console.log(error)
        toastr.error(error.response.data.message)
    })
}