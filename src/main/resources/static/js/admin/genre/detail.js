// Required and Messages
$('#form-update-genre').validate({
    // required data for genre
    rules: {
        name: {
            required: true
        }
    },
    // messages announce when genre's data is empty
    messages: {
        name: {
            required: "Name is empty"
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

// Update genre
const nameEl = document.getElementById('name');
const btnUpdate = document.getElementById('btn-update');
btnUpdate.addEventListener('click', function () {
// if form-create-genre is not filled full => return
    if (!$('#form-update-genre').valid()) {
        return;
    }
    const data = {
        name: nameEl.value
    }
    axios.put(`/api/admin/genres/${genre.id}/update-genre`, data)
        .then(function (response) {
            toastr.success('Update success')
            setTimeout(function () { location.reload(); }, 500)
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})

// Delete genre
const btnDeleteGenre = document.getElementById('btn-delete-genre')
btnDeleteGenre.addEventListener('click', function () {
    const isConfirm = confirm('Are you sure you want to delete this information of genre?')
    if (!isConfirm) {
        return
    }
    axios.delete(`/api/admin/genres/${genre.id}/delete-genre`, genre.id)
        .then(function (response) {
            toastr.success('Delete success')
            setTimeout(function () {  window.location.href = '/admin/genres/homePage' }, 1500)
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})