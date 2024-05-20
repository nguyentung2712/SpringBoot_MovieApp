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
        email: {
            required: true
        },
        password: {
            required: true
        },
        confirmPassword: {
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
        email: {
            required: "Email is empty!"
        },
        password: {
            required: "Password is empty!"
        },
        confirmPassword: {
            required: "Confirm password is empty!"
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

// Create user
const nameEl = document.getElementById('name');
const birthdayEl = document.getElementById('birthday');
const genderEl = document.getElementById('gender');
const phoneNumberEl = document.getElementById('phoneNumber');
const emailEl = document.getElementById('email');
const passwordEl = document.getElementById('password');
const confirmPasswordEl = document.getElementById('confirmPassword');

const btnCreate = document.getElementById('btn-create');
btnCreate.addEventListener('click', function() {

    // if form-create-user is not filled full => return
    if (!$('#form-create-user').valid()) {
        return;
    }

    // Send user's data to database
    const data = {
        name: nameEl.value,
        birthday: birthdayEl.value,
        gender: genderEl.value,
        phoneNumber: phoneNumberEl.value,
        email: emailEl.value,
        newPassword: passwordEl.value,
        confirmNewPassword: confirmPasswordEl.value
    }

    // Using axios to send data to server
    axios.post(`/api/admin/users/create-user`, data)
    // announce after send data
        .then(function (response) {
            // announce when create success
            toastr.success('Create user success')
            // go to location path
            setTimeout(function () { window.location.href = `/admin/users/${response.data.id}/detail` }, 1500)
        })
        .catch(function (error) {
            // send error announce when create fail
            toastr.error(error.response.data.message)
        })
})

function unHidePassword() {
    if(passwordEl.type === "password" || confirmPasswordEl.type === "password") {
        passwordEl.type = "text";
        confirmPasswordEl.type = "text";
    } else {
        passwordEl.type = "password";
        confirmPasswordEl.type = "password";
    }
}