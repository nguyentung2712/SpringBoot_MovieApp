// Required and Messages
$('#form-create-actor').validate({
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

// Create actor
const nameEl = document.getElementById('name');
const descriptionEl = document.getElementById('description');
const birthdayEl = document.getElementById('birthday');
const btnCreate = document.getElementById('btn-create');
btnCreate.addEventListener('click', function () {
    // if form-create-actor is not filled full => return
    if (!$('#form-create-actor').valid()) {
        return;
    }
    const data = {
        name: nameEl.value,
        description: descriptionEl.value,
        birthday: birthdayEl.value,
    }
    axios.post('/api/admin/actors/create-actor', data)
        .then(function (response) {
            toastr.success('Create actor success!')
            setTimeout(function () { window.location.href = `/admin/actors/${response.data.id}/detail` }, 1500)
        })
        .catch(function (error) {
            toastr.error(error.response.data.message)
        })
})
