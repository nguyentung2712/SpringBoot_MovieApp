// Required and Messages
$('#form-update-actor').validate({
    // required data for actor
    rules: {
        name: {
            required: true
        },
        description: {
            required: true
        },
        birthday: {
            required: true
        }
    },
    // messages announce when actor's data is empty
    messages: {
        name: {
            required: "Name is empty"
        },
        description: {
            required: "Description is empty"
        },
        birthday: {
            required: "Birthday is empty"
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

// Update actor
const nameEl = document.getElementById('name');
const descriptionEl = document.getElementById('description');
const birthdayEl = document.getElementById('birthday');

const btnUpdate = document.getElementById('btn-update');
btnUpdate.addEventListener('click', function () {
// if form-create-actor is not filled full => return
    if (!$('#form-update-actor').valid()) {
        return;
    }
    const data = {
        name: nameEl.value,
        description: descriptionEl.value,
        birthday: birthdayEl.value,
    }
    axios.put(`/api/admin/actors/${actor.id}/update-actor`, data)
        .then(function (response) {
            toastr.success('Update success')
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})

// Delete actor
const btnDeleteActor = document.getElementById('btn-delete-actor')
btnDeleteActor.addEventListener('click', function () {
    const isConfirm = confirm('Are you sure you want to delete this information of actor?')
    if (!isConfirm) {
        return
    }
    axios.delete(`/api/admin/actors/${actor.id}/delete-actor`)
        .then(function (response) {
            toastr.success('Delete success')
            setTimeout(function () {
                window.location.href = '/admin/actors'
            }, 1500)
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})

// Upload avatar
const imagePreview = document.getElementById('image-preview')
const imageInput = document.getElementById('image')
imageInput.addEventListener("change", (e) => {
    // Get file was chosen
    const file = e.target.files[0]
    // Create object form data to send data
    const formData = new FormData()
    formData.append('file', file)
    // Call api using axios
    axios.post(`/api/admin/actors/${actor.id}/upload-avatar`, formData)
        .then(res => {
            // Show image was uploaded
            imagePreview.src = res.data
            // Using toastr to announce user that upload avatar success
            toastr.success('Upload avatar success!')
        })
        .catch(err => {
            // Using toastr to announce user that upload avatar fail
            toastr.error(err.response.data.message)
        })
})

// Delete avatar
const btnDeleteAvatar = document.getElementById('btn-delete-avatar');
btnDeleteAvatar.addEventListener('click', function () {
    const isConfirm = confirm('Are you sure you want to delete this avatar?')
    if (!isConfirm) {
       return
    }
    axios.delete(`/api/admin/actors/${actor.id}/delete-avatar`, )
        .then(res => {
          toastr.success('Delete avatar success!')
        })
        .catch(err => {
          toastr.error(err.response.data.message)
        })
})