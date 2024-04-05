// Change info handle
const inputName = document.getElementById('name');
const formInfo = document.getElementById('form-info');

formInfo.addEventListener("submit", (e) => {
    e.preventDefault();

    // Check name's condition
    // Get data from formInfo
    const data = {
        name: inputName.value
    }

    // Call api using axios
    axios.put(`/api/users/${user.id}/change-info`, data)
        .then((response) => {
            if (response.status === 200) {
                toastr.success('Change info success');
                setTimeout(() => {
                    location.reload()
                }, 500);
            } else {
                toastr.error('Change info fail');
            }
        })
        .catch((error) => {
            console.log(error);
            toastr.error(error.response.data.message);
        })
});

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
    axios.post(`/api/users/${user.id}/upload-avatar`, formData)
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
    axios.delete(`/api/users/${user.id}/delete-avatar`)
        .then(res => {
          toastr.success('Delete avatar success!')
        })
        .catch(err => {
          toastr.error(err.response.data.message)
        })
})

// Change password handle
const inputCurrentPassword = document.getElementById('current-password');
const inputNewPassword = document.getElementById('new-password');
const inputConfirmNewPassword = document.getElementById('confirm-new-password');
const formPassword = document.getElementById('form-password');

formPassword.addEventListener("submit", (e) => {
    e.preventDefault();

    // Check validate
    // get data from formPassword
    const data = {
        currentPassword: inputCurrentPassword.value,
        newPassword: inputNewPassword.value,
        confirmNewPassword: inputConfirmNewPassword.value
    }

    // Call api using axios
    axios.post(`/api/users/${user.id}/change-password`, data)
        .then((response) => {
            if (response.status === 200) {
                toastr.success('Change password success');
                setTimeout(() => {
                    location.reload()
                }, 500);
            } else {
                toastr.error('Change password fail');
            }
        })
        .catch((error) => {
            console.log(error);
            toastr.error(error.response.data.message);
        })
})

function unHidePassword() {
    if(inputCurrentPassword.type === "password" ||
       inputNewPassword.type === "password" ||
       inputConfirmNewPassword.type === "password") {

        inputCurrentPassword.type = "text";
        inputNewPassword.type = "text";
        inputConfirmNewPassword.type = "text";
    } else {
        inputCurrentPassword.type = "password";
        inputNewPassword.type = "password";
        inputConfirmNewPassword.type = "password";
    }
}
