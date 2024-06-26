// Required and Messages
$('#form-update-director').validate({
    // required data for director
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
    // messages announce when director's data is empty
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

// Update director
const nameEl = document.getElementById('name');
const descriptionEl = document.getElementById('description');
const birthdayEl = document.getElementById('birthday');
const btnUpdate = document.getElementById('btn-update');
btnUpdate.addEventListener('click', function () {
    // if form-create-director is not filled full => return
    if (!$('#form-update-director').valid()) {
        return;
    }

    // Send data to server
    const data = {
        name: nameEl.value,
        description: descriptionEl.value,
        birthday: birthdayEl.value,
    }

    // Using axios to send data to server
    axios.put(`/api/admin/directors/${director.id}/update-director`, data)
        .then(function (response) {
            toastr.success('Update success')
            setTimeout(function () { location.reload(); }, 500)
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})

// Delete director
const btnDelete = document.getElementById('btn-delete-director')
btnDelete.addEventListener('click', function () {
    const isConfirm = confirm('Are you sure you want to delete this information of director?')
    if (!isConfirm) {
        return
    }
    axios.delete(`/api/admin/directors/${director.id}/delete-director`, director.id)
        .then(function (response) {
            toastr.success('Delete success')
            setTimeout(function () { window.location.href = '/admin/directors/homePage' }, 1500)
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
    axios.post(`/api/admin/directors/${director.id}/upload-avatar`, formData)
        .then(res => {
            // Show image was uploaded
            imagePreview.src = res.data

            // Using toastr to announce user that upload avatar success
            toastr.success('Upload avatar success!')
        })
        .catch(err => {
            // Using toastr to announce user that upload avatar success
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
    axios.delete(`/api/admin/directors/${director.id}/delete-avatar`, director.id)
        .then(function (response) {
          toastr.success('Delete avatar success')
          setTimeout(function() { location.reload() }, 1500)
        })
        .catch(function (error) {
          toastr.error(error.response.data.message)
        })
})