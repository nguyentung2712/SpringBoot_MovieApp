// Required and Messages
$('#form-create-genre').validate({
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

// Create genre
const nameEl = document.getElementById('name');
const btnCreate = document.getElementById('btn-create');
btnCreate.addEventListener('click', function () {
// if form-create-genre is not filled full => return
if (!$('#form-create-genre').valid()) {
    return;
}
const data = {
    name: nameEl.value
}
axios.post('/api/admin/genres/create-genre', data)
    .then(function (response) {
        toastr.success('Create genre success!')
        setTimeout(function () { window.location.href = `/admin/genres/${response.data.id}/detail` }, 1500)
    })
    .catch(function (error) {
        toastr.error(error.response.data.message)
    })
})