// register handle
const inputName = document.getElementById('name');
const inputEmail = document.getElementById('email');
const inputPassword = document.getElementById('password');
const inputConfirmPassword = document.getElementById('confirmPassword')
const formRegister = document.getElementById('form-register');
formRegister.addEventListener("submit", (e) => {
    e.preventDefault();

    // Check validate
    // get data form input
    const data = {
        name: inputName.value,
        email: inputEmail.value,
        password: inputPassword.value,
        confirmPassword: inputConfirmPassword.value
    }

    // Call api using axios
    axios.post('/api/auth/register', data)
        .then((response) => {
            if (response.status === 200) {
                toastr.success('Register success');
                setTimeout(() => { window.location.href = '/login' }, 1000);
            } else {
                toastr.error('Register fail');
            }
        })
        .catch((error) => {
            console.log(error);
            toastr.error(error.response.data.message);
        })
})
