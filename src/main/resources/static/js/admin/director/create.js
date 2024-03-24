$('#form-create-director').validate({
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

// Create director
const nameEl = document.getElementById('name');
const descriptionEl = document.getElementById('description');
const birthdayEl = document.getElementById('birthday');
const btnCreate = document.getElementById('btn-create');
btnCreate.addEventListener('click', function () {
    // if form-create-director is not filled full => return
    if (!$('#form-create-director').valid()) {
        return;
    }
    // Send director's data to database
    const data = {
        name: nameEl.value,
        description: descriptionEl.value,
        birthday: birthdayEl.value,
    }
    // Using axios to send data to server
    axios.post('/api/admin/directors/create-director', data)
        // announce after send data
        .then(function (response) {
            // announce when create success
            toastr.success('Create director success')
            // go to location path
            setTimeout(function () { window.location.href = `/admin/directors/${response.data.id}/detail`}, 1500) })
        .catch(function (error) {
            // send error announce when create fail
            console.log(error)
            toastr.error(error.response.data.message)
        })
})