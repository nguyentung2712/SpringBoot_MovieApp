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
                toastr.success('Change password success, please login');
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
