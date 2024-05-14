$('#form-create-user').validate({

    // required data for user
    rules: {
        name: {
            required: true
        },
        birthday: {
            required: true
        },
        gender: {
            required: true
        },
        phoneNumber: {
            required: true
        },
        enabled: {
            required: true
        }
    },

    // messages announce when user's data is empty
    messages: {
        name: {
            required: "Name is empty!"
        },
        birthday: {
            required: "Date is empty!"
        },
        gender: {
            required: "Gender is empty!"
        },
        phoneNumber: {
            required: "Phone number is empty!"
        },
        enabled: {
            required: "Enabled is empty!"
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

// Update info user
const nameEl = document.getElementById('name');
const birthdayEl = document.getElementById('birthday');
const genderEl = document.getElementById('gender');
const phoneNumberEl = document.getElementById('phoneNumber');
const enabledEl = document.getElementById('enabled');
const btnUpdate = document.getElementById('btn-update');
btnUpdate.addEventListener('click', function () {
    if (!$('#form-update-user').valid()) {
        return;
    }

    // Send data is updated to server
    const data = {
        name: nameEl.value,
        birthday: birthdayEl.value,
        gender: genderEl.value,
        phoneNumber: phoneNumberEl.value,
        enabled: enabledEl.value
    }

    // Using axios to send data to server
    axios.put(`/api/admin/users/${user.id}/change-info-user`, data)
        .then(function (response) {
            toastr.success('Update success')
            setTimeout(function () { location.reload(); }, 500)
        })
        .catch(function (error) {
            console.log(error)
            toastr.error(error.response.data.message)
        })
})